package main

import (
	"fmt"
	"log/slog"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

type A struct {
	Name string `json:"name"`
}

func init() {
	common.InitSlogger()

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		rest.JSON(w, http.StatusOK, A{Name: "Daniel"})
		fmt.Print("hi")
	})
}

func main() {
	slog.Info("Starting server on :8080...")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		slog.Error(fmt.Sprintf("Error starting server: %s", err.Error()))
	}
}
