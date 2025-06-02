package services

import (
	"database/sql"
	"errors"
	"time"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"
)

type AuthService interface {
	InitToken(email string, password string) (string, error)
	ParseToken(tokenString string) (models.User, error)
}

type AuthServiceImpl struct {
	db *sql.DB
}

func NewAuthService(db *sql.DB) AuthService {
	return &AuthServiceImpl{
		db: db,
	}
}

func (a *AuthServiceImpl) InitToken(email string, password string) (string, error) {
	var passwordHash string
	err := a.db.QueryRow(`SELECT password_hash FROM users WHERE email = $1`, email).Scan(&passwordHash)
	if err != nil {
		return "", errors.New("invalid email or password")
	}

	if !common.CheckPasswordHash(password, passwordHash) {
		return "", errors.New("invalid email or password")
	}

	sessionID, err := common.GenerateRandomString(32)
	if err != nil {
		return "", err
	}

	_, err = a.db.Exec(`INSERT INTO auth_sessions (session_id, user_email, exp) VALUES ($1, $2, NOW() + INTERVAL '1 hour')`, sessionID, email)
	if err != nil {
		return "", err
	}
	return sessionID, nil
}

func (a *AuthServiceImpl) ParseToken(tokenString string) (models.User, error) {
	var email string
	var exp time.Time

	err := a.db.QueryRow(`SELECT user_email, exp FROM auth_sessions WHERE session_id = $1`, tokenString).Scan(&email, &exp)
	if err != nil {
		return models.User{}, err
	}
	if time.Now().After(exp) {
		return models.User{}, errors.New("session expired")
	}

	var user models.User
	err = a.db.QueryRow(`SELECT email, is_admin FROM users WHERE email = $1`, email).Scan(&user.Email, &user.IsAdmin)
	if err != nil {
		return models.User{}, err
	}
	return user, nil
}
