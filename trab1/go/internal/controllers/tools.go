package controllers

import (
	"net/http"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/rest"
)

// AuthUser authenticates a user from the HTTP request using the provided AuthService.
// It extracts the authentication token from the request, parses it, and returns the corresponding user.
// Returns an error if the token is missing, invalid, or cannot be parsed.
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
