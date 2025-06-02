package controllers

import (
	"fmt"
	"log/slog"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type TestSessionController struct {
	projectService  services.ProjectService
	authService     services.AuthService
	sessionService  services.TestSessionService
	strategyService services.StrategyService
	v               views.Views
}

func NewTestSessionController(projectService services.ProjectService, authService services.AuthService, sessionService services.TestSessionService, strategyService services.StrategyService, v views.Views) Controller {
	return &TestSessionController{
		projectService:  projectService,
		authService:     authService,
		sessionService:  sessionService,
		strategyService: strategyService,
		v:               v,
	}
}

func (c *TestSessionController) Static(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	projectName := r.PathValue("projectName")

	usr, err := AuthUser(c.authService, r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	allowed, err := UserInProject(c.projectService, usr.Email, projectName)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}
	if !usr.IsAdmin && !allowed && projectName != "" {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	if projectName == "" {
		projectName = "NIL"
	}

	strategies, err := c.strategyService.GetAll()
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	sn := []string{}
	for _, v := range strategies {
		sn = append(sn, v.Name)
	}

	var sessions []models.TestSession
	if usr.IsAdmin {
		sessions, err = c.sessionService.GetAll()
	} else {
		sessions, err = c.sessionService.GetFromUser(usr.Email)
	}
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	var spub []models.TestSession
	if projectName != "NIL" {
		for _, s := range sessions {
			if s.ProjectName == projectName {
				spub = append(spub, s)
			}
		}
	} else {
		spub = sessions
	}

	rest.HTML(w, 200, c.v.Sessions, views.HtmlCreateSessionVars{ProjectName: projectName, Strategies: sn, UserEmail: usr.Email, Admin: usr.IsAdmin, Sessions: spub})
}

func (c *TestSessionController) CreateSession(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	usr, err := AuthUser(c.authService, r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	testSession := models.TestSession{}
	err = rest.ReadBody(r, &testSession)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadRequest, err.Error())
		return
	}

	pjs, err := c.projectService.GetFromUser(usr.Email)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}
	pjsNames := []string{}
	for _, p := range pjs {
		pjsNames = append(pjsNames, p.Name)
	}
	if !common.IsSubset([]string{testSession.ProjectName}, pjsNames) {
		slog.Error("user not in project")
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	err = c.sessionService.CreateTestSession(testSession)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}
}

func (c *TestSessionController) ChangeSessionStatus(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	sessionId := r.PathValue("id")

	usr, err := AuthUser(c.authService, r)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	session, err := c.sessionService.Get(sessionId)
	if err != nil {
		slog.Error(err.Error())
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	if session.TesterEmail != usr.Email && !usr.IsAdmin {
		slog.Error(fmt.Sprintf("%s: admin?:%t", session.TesterEmail, usr.IsAdmin))
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	type input struct {
		Status string `json:"status"`
	}
	var i input
	err = rest.ReadBody(r, &i)
	if err != nil {
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}
	err = c.sessionService.ChangeStatus(sessionId, i.Status)
	if err != nil {
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}
}

func (c *TestSessionController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /sessions/{projectName}", c.Static)
	mux.HandleFunc("GET /sessions", c.Static)
	mux.HandleFunc("PUT /sessions", c.CreateSession)
	mux.HandleFunc("POST /sessions/{id}", c.ChangeSessionStatus)
}
