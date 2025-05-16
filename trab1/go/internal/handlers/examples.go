package handlers

import (
	"fmt"
	"log/slog"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type user struct {
	Name string `json:"name"`
}

func BuildUser(w http.ResponseWriter, r *http.Request) {
	s := r.PathValue("name")
	rest.JSON(w, http.StatusOK, user{Name: s})
}

func Hello(w http.ResponseWriter, r *http.Request) {
	rest.String(w, http.StatusOK, "Hello, World!")
}

func Echo(w http.ResponseWriter, r *http.Request) {
	s := r.PathValue("msg")
	slog.Info(fmt.Sprintf("echo was called with: '%s'", s))
	rest.String(w, http.StatusOK, s)
}

func RegisterExamples(mux *http.ServeMux) {
	mux.HandleFunc("GET /user/{name}", BuildUser)
	mux.HandleFunc("GET /hello", Hello)
	mux.HandleFunc("GET /echo/{msg}", Echo)
}
