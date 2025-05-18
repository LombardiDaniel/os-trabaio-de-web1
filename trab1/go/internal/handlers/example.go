package handlers

import (
	"fmt"
	"log/slog"
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type ExampleHandler struct {
}

func NewExampleHandler() Handler {
	return &ExampleHandler{}
}

func (h *ExampleHandler) buildUser(w http.ResponseWriter, r *http.Request) {
	s := r.PathValue("name")
	rest.JSON(w, http.StatusOK, models.User{Name: s})
}

func (h *ExampleHandler) hello(w http.ResponseWriter, r *http.Request) {
	rest.String(w, http.StatusOK, "Hello, World!")
}

func (h *ExampleHandler) echo(w http.ResponseWriter, r *http.Request) {
	s := r.PathValue("msg")
	slog.Info(fmt.Sprintf("echo was called with: '%s'", s))
	rest.String(w, http.StatusOK, s)
}

func (h *ExampleHandler) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /example/user/{name}", h.buildUser)
	mux.HandleFunc("GET /example/hello", h.hello)
	mux.HandleFunc("GET /example/echo/{msg}", h.echo)
}
