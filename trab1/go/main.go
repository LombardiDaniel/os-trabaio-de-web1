package main

import (
	"fmt"
	"log/slog"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/handlers"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

var mux *http.ServeMux

func init() {
	common.InitSlogger()

	mux = http.NewServeMux()
	handlers.RegisterExamples(mux)

	mux.HandleFunc("GET /healthcheck", func(w http.ResponseWriter, r *http.Request) {
		rest.String(w, http.StatusOK, "OK")
	})
}

func main() {
	slog.Info("Starting server on :8080...")
	if err := http.ListenAndServe(":8080", mux); err != nil {
		slog.Error(fmt.Sprintf("Error starting server: %s", err.Error()))
	}
}
