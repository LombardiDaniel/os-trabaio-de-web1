package rest

import (
	"encoding/json"
	"errors"
	"io"
	"net/http"
	"time"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/constants"
)

var authCookieName = constants.AppName + "-userSessionId"

// UserSessionId gets the Session Id from the cookie in the user's request
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

// ReadBody reads the request body into the s struct as JSON
func ReadBody(r *http.Request, s any) error {
	b, err := io.ReadAll(r.Body)
	if err != nil {
		return err
	}

	err = json.Unmarshal(b, s)
	return err
}
