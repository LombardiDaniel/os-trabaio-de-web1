package models

import "time"

type Project struct {
	Name        string    `json:"name"`
	CreatedAt   time.Time `json:"createdAt"`
	Description string    `json:"description"`
	Users       []string  `json:"users"`
}
