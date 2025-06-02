package controllers

import (
	"context"
	"fmt"
	"log/slog"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/constants"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type UserController struct {
	userService services.UserService
	authService services.AuthService
	objService  services.ObjectService
	v           views.Views
}

func NewUserController(userService services.UserService, authService services.AuthService, objService services.ObjectService, v views.Views) Controller {
	return &UserController{
		userService: userService,
		authService: authService,
		objService:  objService,
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

	// slog.Info(u.Email)

	rest.SetAuth(w, token)
	rest.String(w, 200, "OK")
}

func (c *UserController) GetUploadURL(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))

	_, err := AuthUser(c.authService, r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	s := uuid.NewString()
	url, err := c.objService.UploadUrl(context.Background(), constants.S3Bucket, s, time.Hour)
	if err != nil {
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	rest.String(w, http.StatusOK, fmt.Sprintf(`{"url":"%s","name":"%s"}`, url, s))
}

func (c *UserController) Logout(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	rest.SetAuth(w, "")
}

func (c *UserController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("PUT /user", c.CreateUser)
	mux.HandleFunc("POST /login", c.Login)
	mux.HandleFunc("POST /uploadurl", c.GetUploadURL)
	mux.HandleFunc("GET /logout", c.Logout)
}
