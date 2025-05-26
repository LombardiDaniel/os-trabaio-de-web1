package services

import (
	"database/sql"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
)

type ProjectService interface {
	Create(p models.Project) error
	Get(name string) (models.Project, error)
	GetAll() ([]models.Project, error)
}

type ProjectServiceImpl struct {
	db *sql.DB
}

func NewProjectService(db *sql.DB) ProjectService {
	return &ProjectServiceImpl{
		db: db,
	}
}

func (s *ProjectServiceImpl) Create(p models.Project) error {
	tx, err := s.db.Begin()
	if err != nil {
		return err
	}
	defer func() {
		if err != nil {
			tx.Rollback()
		} else {
			tx.Commit()
		}
	}()

	_, err = tx.Exec(`INSERT INTO projects (project_name, project_description) VALUES ($1, $2)`, p.Name, p.Description)
	if err != nil {
		return err
	}

	for _, email := range p.Users {
		_, err = tx.Exec(`INSERT INTO project_users (project_name, user_email) VALUES ($1, $2)`, p.Name, email)
		if err != nil {
			return err
		}
	}
	return nil
}

func (s *ProjectServiceImpl) Get(name string) (models.Project, error) {
	var p models.Project
	err := s.db.QueryRow(`SELECT project_name, project_description, created_at FROM projects WHERE project_name = $1`, name).Scan(&p.Name, &p.Description, &p.CreatedAt)
	if err != nil {
		return p, err
	}

	rows, err := s.db.Query(`SELECT user_email FROM project_users WHERE project_name = $1`, name)
	if err != nil {
		return p, err
	}
	defer rows.Close()

	var userEmails []string
	for rows.Next() {
		var email string
		if err := rows.Scan(&email); err != nil {
			return p, err
		}
		userEmails = append(userEmails, email)
	}
	p.Users = userEmails
	return p, nil
}

func (s *ProjectServiceImpl) GetAll() ([]models.Project, error) {
	rows, err := s.db.Query(`SELECT project_name, project_description, created_at FROM projects`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var projects []models.Project
	for rows.Next() {
		var p models.Project
		err := rows.Scan(&p.Name, &p.Description, &p.CreatedAt)
		if err != nil {
			return nil, err
		}

		userRows, err := s.db.Query(`SELECT user_email FROM project_users WHERE project_name = $1`, p.Name)
		if err != nil {
			return nil, err
		}
		var userEmails []string
		for userRows.Next() {
			var email string
			if err := userRows.Scan(&email); err != nil {
				userRows.Close()
				return nil, err
			}
			userEmails = append(userEmails, email)
		}
		userRows.Close()
		p.Users = userEmails
		projects = append(projects, p)
	}
	return projects, nil
}

func (s *ProjectServiceImpl) GetUsers(name string) ([]models.User, error) {
	rows, err := s.db.Query(`SELECT user_email FROM project_users WHERE project_name = $1`, name)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var users []models.User
	for rows.Next() {
		var email string
		if err := rows.Scan(&email); err != nil {
			return nil, err
		}
		users = append(users, models.User{Email: email})
	}
	return users, nil
}
