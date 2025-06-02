package controllers

import (
	"errors"
	"net/http"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/services"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
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
	return u, err
}

func AuthAdmin(as services.AuthService, r *http.Request) (models.User, error) {
	usr, err := AuthUser(as, r)
	if err != nil {
		return usr, err
	}
	if !usr.IsAdmin {
		return usr, errors.New("not admin")
	}

	return usr, nil
}

func UserInProject(ps services.ProjectService, email string, projectName string) (bool, error) {
	pjs, err := ps.GetFromUser(email)
	if err != nil {
		return false, err
	}

	pjsNames := []string{}
	for _, p := range pjs {
		pjsNames = append(pjsNames, p.Name)
	}

	return common.IsSubset([]string{projectName}, pjsNames), nil
}
