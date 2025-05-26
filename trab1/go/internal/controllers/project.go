package controllers

import (
	"fmt"
	"log/slog"
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
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
	usr, err := AuthUser(c.authService, r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}
	if !usr.IsAdmin {
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
	usr, err := AuthUser(c.authService, r)
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

	if usr.IsAdmin {
		rest.HTML(w, http.StatusOK, c.v.Projects, views.HtmlProjectsVars{UserEmail: usr.Email, Projects: ps, Admin: true})
		return
	}

	usrPs := []models.Project{}
	for _, p := range ps {
		if common.IsSubset([]string{usr.Email}, p.Users) {
			usrPs = append(usrPs, p)
		}
	}
	rest.HTML(w, http.StatusOK, c.v.Projects, views.HtmlProjectsVars{UserEmail: usr.Email, Projects: usrPs, Admin: false})
}

func (c *ProjectController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("PUT /project", c.CreateProject)
	mux.HandleFunc("GET /projects", c.GetProjects)
}
