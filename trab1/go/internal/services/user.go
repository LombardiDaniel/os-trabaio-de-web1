package services

import "database/sql"

type UserService struct {
	db *sql.DB
}

func NewUserServicePgImpl(db *sql.DB) UserService {
	return UserService{
		db: db,
	}
}
