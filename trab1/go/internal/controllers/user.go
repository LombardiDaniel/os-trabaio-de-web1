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
}

func NewUserController(userService services.UserService) Controller {
	return &UserController{
		userService: userService,
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

	err = c.userService.CreateUser(r.Context(), u)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
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

	u.IsAdmin = false

	err = c.userService.CreateUser(r.Context(), u)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
	}

	rest.String(w, http.StatusOK, "OK")
}

func (c *UserController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("PUT /user", c.CreateUser)
	mux.HandleFunc("POST /login", c.CreateUser)
}
