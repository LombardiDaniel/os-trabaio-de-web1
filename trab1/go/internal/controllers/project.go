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

type ProjectController struct {
	projectService services.ProjectService
	authService    services.AuthService
	v              views.Views
}

func NewProjectController(projectService services.ProjectService, authService services.AuthService, v views.Views) Controller {
	return &ProjectController{
		projectService: projectService,
		authService:    authService,
		v:              v,
	}
}

func (c *ProjectController) CreateProject(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	_, err := NeedAdmin(c.authService, r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	var p models.Project
	err = rest.ReadBody(r, &p)
	if err != nil {
		rest.String(w, http.StatusBadRequest, err.Error())
		return
	}

	err = c.projectService.Create(p)
	if err != nil {
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	rest.String(w, http.StatusOK, "OK")
}

func (c *ProjectController) GetProjects(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	usr, err := NeedAdmin(c.authService, r)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	ps, err := c.projectService.GetAll()
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	slog.Debug(fmt.Sprintf("%+v", ps))
	rest.HTML(w, http.StatusOK, c.v.Projects, views.HtmlProjectsVars{UserEmail: usr.Email, Projects: ps})
}

func (c *ProjectController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("PUT /project", c.CreateProject)
	mux.HandleFunc("GET /projects", c.GetProjects)
}
