package main

import (
	"database/sql"
	"errors"
	"fmt"
	"log/slog"
	"net/http"

	_ "github.com/lib/pq"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/controllers"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/logger"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

var (
	mux *http.ServeMux

	hs []controllers.Controller
)

func init() {
	logger.InitSlogger()

	mux = http.NewServeMux()

	pgConnStr := common.GetEnvVarDefault("POSTGRES_URI", "postgres://user:password@localhost:5432/db?sslmode=disable")
	db, err := sql.Open("postgres", pgConnStr)
	if err != nil {
		panic(errors.Join(err, errors.New("could not connect pgsql")))
	}
	if err := db.Ping(); err != nil {
		panic(errors.Join(err, errors.New("could not ping pgsql")))
	}

	userService := services.NewUserServicePgImpl(db)

	hs = []controllers.Controller{
		controllers.NewExampleController(),
		controllers.NewStaticController("./internal/views/"),
		controllers.NewUserController(userService),
	}

	for _, h := range hs {
		h.RegisterRoutes(mux)
	}
}

func main() {
	mux.HandleFunc("GET /healthcheck", func(w http.ResponseWriter, r *http.Request) {
		rest.String(w, http.StatusOK, "OK")
	})

	// http.HandleFunc("GET /", func(w http.ResponseWriter, r *http.Request) {
	// 	http.Redirect(w, r, "/index.html", http.StatusMovedPermanently)
	// })

	slog.Info("Starting server on :8080...")
	if err := http.ListenAndServe(":8080", mux); err != nil {
		slog.Error(fmt.Sprintf("Error starting server: %s", err.Error()))
	}
}
