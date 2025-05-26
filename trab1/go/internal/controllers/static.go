package controllers

import (
	"bytes"
	"fmt"
	"html/template"
	"log/slog"
	"net/http"
	"path/filepath"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type StaticController struct {
	index       *template.Template
	create_user *template.Template
}

func NewStaticController(templatesDir string) Controller {
	return &StaticController{
		index:       common.LoadHTMLTemplate(filepath.Join(templatesDir, "index.html")),
		create_user: common.LoadHTMLTemplate(filepath.Join(templatesDir, "create_user.html")),
	}
}

func (c *StaticController) Index(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	type idxVars struct {
		UserEmail string
		Ip        string
	}

	body := new(bytes.Buffer)
	err := c.index.Execute(body, idxVars{
		UserEmail: "",
		Ip:        r.RemoteAddr,
	})

	if err != nil {
		slog.Error(err.Error())
		return
	}

	w.Write(body.Bytes())
}

func (c *StaticController) CreateUser(w http.ResponseWriter, r *http.Request) {
	slog.Info(fmt.Sprintf("[%s]::%s", r.Method, r.RequestURI))
	body := new(bytes.Buffer)
	err := c.create_user.Execute(body, nil)

	if err != nil {
		slog.Error(err.Error())
		return
	}
	w.Write(body.Bytes())
}

func (c *StaticController) RegisterRoutes(mux *http.ServeMux) {
	mux.HandleFunc("GET /", c.Index)
	mux.HandleFunc("GET /create-user", c.CreateUser)
}
