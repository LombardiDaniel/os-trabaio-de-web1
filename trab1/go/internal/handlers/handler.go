package handlers

import "net/http"

// Handler is an interface for handling HTTP calls.
type Handler interface {
	// RegisterRoutes register all handler routes on the request multiplexer
	RegisterRoutes(mux *http.ServeMux)
}
