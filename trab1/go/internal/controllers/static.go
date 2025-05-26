package controllers

import (
	"fmt"
	"log/slog"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type StaticController struct {
	userService services.UserService
	authService services.AuthService
	objService  services.ObjectService
	v           views.Views
}

func NewStaticController(userService services.UserService, authService services.AuthService, objService services.ObjectService, v views.Views) Controller {
	return &StaticController{
		userService: userService,
		authService: authService,
		objService:  objService,
		v:           v,
	}
}

func (c *StaticController) Index(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))

	usr, err := AuthUser(c.authService, r)
	if err != nil {
		// rest.String(w, http.StatusUnauthorized, "Unauthorized")
		// return
		usr.Email = ""
		usr.IsAdmin = false
	}

	rest.HTML(w, http.StatusOK, c.v.Index, views.HtmlIdxVars{UserEmail: usr.Email, IsAdmin: usr.IsAdmin})
}

func (c *StaticController) CreateUser(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	rest.HTML(w, http.StatusOK, c.v.CreateUser, nil)
}

func (c *StaticController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /", c.Index)
	mux.HandleFunc("GET /create-user", c.CreateUser)
}
