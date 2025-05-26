package views

import (
	"html/template"
	"path/filepath"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type Views struct {
	Index      *template.Template
	CreateUser *template.Template
	Home       *template.Template
	Projects   *template.Template
}

func NewViews(templatesDir string) Views {
	return Views{
		Index:      common.LoadHTMLTemplate(filepath.Join(templatesDir, "index.html")),
		CreateUser: common.LoadHTMLTemplate(filepath.Join(templatesDir, "create_user.html")),
		Home:       common.LoadHTMLTemplate(filepath.Join(templatesDir, "home.html")),
		Projects:   common.LoadHTMLTemplate(filepath.Join(templatesDir, "projects.html")),
	}
}

type HtmlHomeVars struct {
	UserEmail string
	Admin     bool
}

type HtmlProjectsVars struct {
	UserEmail string
	Projects  []models.Project
}
