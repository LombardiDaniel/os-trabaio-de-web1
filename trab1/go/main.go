package main

import (
	"fmt"
	"log/slog"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

func init() {
	common.InitSlogger()

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintln(w, "Hello, World!")
	})
}

func main() {
	slog.Info("Starting server on :8080...")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		slog.Error("Error starting server: %s", err.Error())
	}
}
