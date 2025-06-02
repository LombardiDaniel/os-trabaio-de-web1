package services

import (
	"database/sql"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type UserService interface {
	CreateUser(user models.User) error
}

type UserServiceImpl struct {
	db *sql.DB
}

func NewUserServiceImpl(db *sql.DB) UserService {
	return &UserServiceImpl{
		db: db,
	}
}

func (s *UserServiceImpl) CreateUser(user models.User) error {
	p, err := common.HashPassword(user.Password)
	if err != nil {
		return err
	}

	user.Password = p

	_, err = s.db.Exec(
		`INSERT INTO users (email, password_hash, is_admin) VALUES ($1, $2, $3)`,
		user.Email,
		user.Password,
		user.IsAdmin,
	)
	return err
}
