package controllers

import (
	"fmt"
	"log/slog"
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type ExampleController struct {
}

func NewExampleController() Controller {
	return &ExampleController{}
}

func (c *ExampleController) buildUser(w http.ResponseWriter, r *http.Request) {
	s := r.PathValue("name")
	rest.JSON(w, http.StatusOK, models.User{Name: s})
}

func (c *ExampleController) hello(w http.ResponseWriter, r *http.Request) {
	rest.String(w, http.StatusOK, "Hello, World!")
}

func (c *ExampleController) echo(w http.ResponseWriter, r *http.Request) {
	s := r.PathValue("msg")
	slog.Info(fmt.Sprintf("echo was called with: '%s'", s))
	rest.String(w, http.StatusOK, s)
}

func (c *ExampleController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /example/user/{name}", c.buildUser)
	mux.HandleFunc("GET /example/hello", c.hello)
	mux.HandleFunc("GET /example/echo/{msg}", c.echo)
}
