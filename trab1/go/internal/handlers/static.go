package handlers

import (
	"bytes"
	"html/template"
	"log/slog"
	"net/http"
	"path/filepath"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type StaticHandler struct {
	index *template.Template
}

func NewStaticHandler(templatesDir string) Handler {
	return &StaticHandler{
		index: common.LoadHTMLTemplate(filepath.Join(templatesDir, "index.html")),
	}
}

type indexVars struct {
	Ip string
}

func (h *StaticHandler) Index(w http.ResponseWriter, r *http.Request) {
	body := new(bytes.Buffer)
	err := h.index.Execute(body, indexVars{
		Ip: r.RemoteAddr,
	})

	if err != nil {
		slog.Error(err.Error())
		return
	}

	w.Write(body.Bytes())
}

func (h *StaticHandler) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /", h.Index)
}
