package main

import (
	"context"
	"database/sql"
	"errors"
	"fmt"
	"log/slog"
	"net/http"

	_ "github.com/lib/pq"
	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/controllers"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/views"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/constants"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/it"
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
	db := it.Must(sql.Open("postgres", pgConnStr))
	if err := db.Ping(); err != nil {
		panic(errors.Join(err, errors.New("could not ping pgsql")))
	}

	minioClient := it.Must(minio.New(
		"localhost:9000",
		&minio.Options{
			Creds: credentials.NewStaticV4(
				"minioadmin",
				"minioadmin",
				"",
			),
			Region: "us-east-1",
			Secure: false,
		},
	))

	if !it.Must(minioClient.BucketExists(context.TODO(), constants.S3Bucket)) {
		err := minioClient.MakeBucket(context.TODO(), constants.S3Bucket, minio.MakeBucketOptions{})
		if err != nil {
			slog.Error(errors.Join(err, errors.New("could not make bucket")).Error())
		}
	}

	userService := services.NewUserServiceImpl(db)
	authService := services.NewAuthService(db)
	s3Service := services.NewObjectServiceMinioImpl(minioClient)
	projectService := services.NewProjectService(db)
	testSessionService := services.NewTestSessionService(db)
	strategyService := services.NewStrategyServiceImpl(db)

	views := views.NewViews("./internal/views/")

	hs = []controllers.Controller{
		controllers.NewExampleController(),
		controllers.NewStaticController(userService, authService, s3Service, views),
		controllers.NewUserController(userService, authService, s3Service, views),
		controllers.NewProjectController(projectService, authService, views),
		controllers.NewTestSessionController(projectService, authService, testSessionService, strategyService, views),
		controllers.NewStrategyController(authService, strategyService, s3Service, views),
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
