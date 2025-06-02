package controllers

import "net/http"

// Controller is an interface for handling HTTP calls.
type Controller interface {
	// RegisterRoutes register all controller routes on the request multiplexer
	RegisterRoutes(mux *http.ServeMux)
}
