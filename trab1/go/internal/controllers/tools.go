package controllers

import (
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

func AuthUser(as services.AuthService, r *http.Request) (models.User, error) {
	token, err := rest.GetAuth(r)
	if err != nil {
		return models.User{}, err
	}

	u, err := as.ParseToken(token)
	if err != nil {
		return models.User{}, err
	}

	return u, nil
}
