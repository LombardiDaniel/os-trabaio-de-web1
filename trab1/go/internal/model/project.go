package models

import "time"

type Project struct {
	Name        string    `json:"project"`
	CreatedAt   time.Time `json:"createdAt"`
	Description string    `json:"description"`
	Users       []string  `json:"users"`
}
