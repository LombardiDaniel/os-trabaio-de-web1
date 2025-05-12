package common

import (
	"encoding/json"
	"fmt"
	"log/slog"
	"net/http"
)

func String(w http.ResponseWriter, code int, r string) {
	defer setStatus(w, code)

	_, err := fmt.Fprint(w, r)
	if err != nil {
		slog.Error(fmt.Sprintf("Could not write response:%s", err))
		return
	}
	w.Header().Add("Content-Type", "text/plain; charset=utf-8")
}

func JSON(w http.ResponseWriter, code int, obj any) {
	defer setStatus(w, code)

	if !bodyAllowedForStatus(code) {
		return
	}

	b, err := json.Marshal(obj)
	if err != nil {
		slog.Error(fmt.Sprintf("Could not Marshal obj: %s", err))
		return
	}

	_, err = fmt.Fprint(w, string(b))
	if err != nil {
		slog.Error(fmt.Sprintf("Could not write response: %s", err))
		return
	}

	w.Header().Add("Content-Type", "application/json; charset=utf-8")
}

func setStatus(w http.ResponseWriter, status int) {
	w.WriteHeader(status)
}

func bodyAllowedForStatus(status int) bool {
	switch {
	case status >= 100 && status <= 199:
		return false
	case status == http.StatusNoContent:
		return false
	case status == http.StatusNotModified:
		return false
	}
	return true
}
