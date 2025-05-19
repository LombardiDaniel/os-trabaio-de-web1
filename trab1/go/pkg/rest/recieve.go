package rest

import (
	"errors"
	"net/http"
	"time"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/constants"
)

var authCookieName = constants.AppName + "-userSessionId"

// UserSessionId gets the Session Id from the user cookie
func UserSessionId(req *http.Request) (string, error) {
	c, err := req.Cookie(authCookieName)
	if err != nil {
		return "", err
	}

	if c.Expires.After(time.Now()) {
		return "", errors.New("cookie expired")
	}

	return c.String(), nil
}
