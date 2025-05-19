package controllers

import (
	"bytes"
	"html/template"
	"log/slog"
	"net/http"
	"path/filepath"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type StaticController struct {
	index *template.Template
}

func NewStaticController(templatesDir string) Controller {
	return &StaticController{
		index: common.LoadHTMLTemplate(filepath.Join(templatesDir, "index.html")),
	}
}

func (c *StaticController) Index(w http.ResponseWriter, r *http.Request) {
	type idxVars struct {
		Ip string
	}

	body := new(bytes.Buffer)
	err := c.index.Execute(body, idxVars{
		Ip: r.RemoteAddr,
	})

	if err != nil {
		slog.Error(err.Error())
		return
	}

	w.Write(body.Bytes())
}

func (c *StaticController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /", c.Index)
}
