package controllers

import (
	"fmt"
	"log/slog"
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type UserController struct {
	userService services.UserService
	authService services.AuthService
	v           views.Views
}

func NewUserController(userService services.UserService, authService services.AuthService, v views.Views) Controller {
	return &UserController{
		userService: userService,
		authService: authService,
		v:           v,
	}
}

func (c *UserController) CreateUser(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	u := models.User{}
	err := rest.ReadBody(r, &u)
	if err != nil {
		slog.Error(fmt.Sprintf("could not read body: %s", err.Error()))
		rest.String(w, http.StatusBadRequest, err.Error())
		return
	}

	u.IsAdmin = false

	err = c.userService.CreateUser(u)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	rest.String(w, http.StatusOK, "OK")
}

func (c *UserController) Login(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	u := models.User{}
	err := rest.ReadBody(r, &u)
	if err != nil {
		slog.Error(fmt.Sprintf("could not read body: %s", err.Error()))
		rest.String(w, http.StatusBadRequest, err.Error())
		return
	}

	token, err := c.authService.InitToken(u.Email, u.Password)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	rest.SetAuth(w, token)
	rest.String(w, http.StatusOK, "OK")
}

func (c *UserController) CheckAdmin(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	token, err := rest.GetAuth(r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	u, err := c.authService.ParseToken(token)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	rest.HTML(w, http.StatusOK, c.v.Home, u)
}

func (c *UserController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("PUT /user", c.CreateUser)
	mux.HandleFunc("POST /login", c.Login)
	mux.HandleFunc("GET /check", c.CheckAdmin)
}
