package controllers

import (
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type StrategyController struct {
	authService     services.AuthService
	strategyService services.StrategyService
	objService      services.ObjectService
	v               views.Views
}

func NewStrategyController(as services.AuthService, ss services.StrategyService, objService services.ObjectService, v views.Views) Controller {
	return &StrategyController{
		authService:     as,
		strategyService: ss,
		objService:      objService,
		v:               v,
	}
}

func (c *StrategyController) Static(w http.ResponseWriter, r *http.Request) {
	strategies, err := c.strategyService.GetAll()
	if err != nil {
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	usr, err := AuthUser(c.authService, r)
	if err != nil { // unauthorized or other error
		rest.HTML(w, http.StatusOK, c.v.Strategies, views.HtmlStrategiesVars{UserEmail: "", Admin: false, Strategies: strategies})
		return
	}

	rest.HTML(w, http.StatusOK, c.v.Strategies, views.HtmlStrategiesVars{UserEmail: usr.Email, Admin: usr.IsAdmin, Strategies: strategies})
}

func (c *StrategyController) CreateStrategy(w http.ResponseWriter, r *http.Request) {
	_, err := AuthAdmin(c.authService, r)
	if err != nil {
		rest.String(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	var strategy models.Strategy
	err = rest.ReadBody(r, &strategy)
	if err != nil {
		rest.String(w, http.StatusBadRequest, err.Error())
		return
	}

	err = c.strategyService.CreateStrategy(strategy)
	if err != nil {
		rest.String(w, http.StatusBadGateway, "BadGateway")
		return
	}

	rest.String(w, http.StatusOK, "OK")
}

func (c *StrategyController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /strategy", c.Static)
	mux.HandleFunc("PUT /strategy", c.CreateStrategy)
}
