package models

type Strategy struct {
	Name        string   `json:"name"`
	Description string   `json:"description"`
	Examples    string   `json:"examples"`
	Hints       string   `json:"hints"`
	Images      []string `json:"images"`
}
