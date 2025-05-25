package services

import (
	"context"
	"database/sql"

	models "github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/model"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type UserService struct {
	db *sql.DB
}

func NewUserServicePgImpl(db *sql.DB) UserService {
	return UserService{
		db: db,
	}
}

func (s *UserService) CreateUser(ctx context.Context, user models.User) error {
	p, err := common.HashPassword(user.Password)
	if err != nil {
		return err
	}

	user.Password = p

	_, err = s.db.ExecContext(
		ctx,
		`INSERT INTO users (email, password_hash, is_admin) VALUES ($1, $2, $3)`,
		user.Email,
		user.Password,
		user.IsAdmin,
	)
	return err
}
