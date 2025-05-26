package views

import (
	"html/template"
	"path/filepath"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type Views struct {
	Index      *template.Template
	CreateUser *template.Template
	Home       *template.Template
}

func NewViews(templatesDir string) Views {
	return Views{
		Index:      common.LoadHTMLTemplate(filepath.Join(templatesDir, "index.html")),
		CreateUser: common.LoadHTMLTemplate(filepath.Join(templatesDir, "create_user.html")),
		Home:       common.LoadHTMLTemplate(filepath.Join(templatesDir, "home.html")),
	}
}

type HtmlHomeVars struct {
	Admin bool
}
