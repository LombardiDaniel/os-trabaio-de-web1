package controllers

import (
	"fmt"
	"log/slog"
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type UserController struct {
	userService services.UserService
	authService services.AuthService
}

func NewUserController(userService services.UserService, authService services.AuthService) Controller {
	return &UserController{
		userService: userService,
		authService: authService,
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

func (c *UserController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("PUT /user", c.CreateUser)
	mux.HandleFunc("POST /login", c.Login)
}
