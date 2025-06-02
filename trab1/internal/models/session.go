package models

type TestSession struct {
	Id           int    `json:"id"`
	TesterEmail  string `json:"testerEmail"`
	Strategy     string `json:"strategy"`
	DurationMins int    `json:"durationMins"`
	Description  string `json:"description"`
	Status       string `json:"status"`
	ProjectName  string `json:"projectName"`
}
