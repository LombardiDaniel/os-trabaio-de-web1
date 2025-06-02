package views

import (
	"html/template"
	"path/filepath"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
)

type Views struct {
	Index      *template.Template
	CreateUser *template.Template
	Projects   *template.Template
	Sessions   *template.Template
	Strategies *template.Template
}

func NewViews(templatesDir string) Views {
	return Views{
		Index:      template.Must(template.ParseFiles(filepath.Join(templatesDir, "index.html"), filepath.Join(templatesDir, "main.css"))),
		Projects:   template.Must(template.ParseFiles(filepath.Join(templatesDir, "projects.html"), filepath.Join(templatesDir, "main.css"))),
		Strategies: template.Must(template.ParseFiles(filepath.Join(templatesDir, "strategies.html"), filepath.Join(templatesDir, "main.css"))),
		Sessions:   template.Must(template.ParseFiles(filepath.Join(templatesDir, "sessions.html"), filepath.Join(templatesDir, "main.css"))),
		CreateUser: template.Must(template.ParseFiles(filepath.Join(templatesDir, "create-user.html"), filepath.Join(templatesDir, "main.css"))),
	}
}

type HtmlHomeVars struct {
	UserEmail string
	Admin     bool
}

type HtmlProjectsVars struct {
	UserEmail string
	Admin     bool
	Projects  []models.Project
}

type HtmlIdxVars struct {
	UserEmail string
	Admin     bool
}

type HtmlCreateSessionVars struct {
	UserEmail   string
	Admin       bool
	ProjectName string
	Strategies  []string
	Sessions    []models.TestSession
}

type HtmlStrategiesVars struct {
	UserEmail  string
	Admin      bool
	Strategies []models.Strategy
}
