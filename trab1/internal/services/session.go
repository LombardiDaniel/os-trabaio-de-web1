package services

import (
	"database/sql"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
)

type TestSessionService interface {
	CreateTestSession(models.TestSession) error
	GetAll() ([]models.TestSession, error)
	ChangeStatus(session_id string, status string) error
	GetFromUser(email string) ([]models.TestSession, error)
	Get(id string) (models.TestSession, error)
}

type TestSessionServiceImpl struct {
	db *sql.DB
}

func NewTestSessionService(db *sql.DB) TestSessionService {
	return &TestSessionServiceImpl{
		db: db,
	}
}

func (s *TestSessionServiceImpl) CreateTestSession(ts models.TestSession) error {
	_, err := s.db.Exec(`
		INSERT INTO test_session (
			tester_email, strategy, duration_minutes, session_description, project_name
		) VALUES ($1, $2, $3, $4, $5)
	`,
		ts.TesterEmail,
		ts.Strategy,
		ts.DurationMins,
		ts.Description,
		ts.ProjectName,
	)
	return err
}

func (s *TestSessionServiceImpl) GetAll() ([]models.TestSession, error) {
	rows, err := s.db.Query(`SELECT session_id, tester_email, strategy, duration_minutes, session_description, session_status, project_name FROM test_session`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	sessions := []models.TestSession{}
	for rows.Next() {
		var ts models.TestSession
		err := rows.Scan(&ts.Id, &ts.TesterEmail, &ts.Strategy, &ts.DurationMins, &ts.Description, &ts.Status, &ts.ProjectName)
		if err != nil {
			return nil, err
		}
		sessions = append(sessions, ts)
	}
	return sessions, nil
}

func (s *TestSessionServiceImpl) ChangeStatus(session_id string, status string) error {
	_, err := s.db.Exec(`UPDATE test_session SET session_status = $1 WHERE session_id = $2`, status, session_id)
	return err
}

func (s *TestSessionServiceImpl) GetFromUser(email string) ([]models.TestSession, error) {
	rows, err := s.db.Query(`
		SELECT
			session_id,
			tester_email,
			strategy,
			duration_minutes,
			session_description,
			session_status,
			project_name
		FROM test_session
		WHERE tester_email = $1`, email)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	sessions := []models.TestSession{}
	for rows.Next() {
		var ts models.TestSession
		err := rows.Scan(&ts.Id, &ts.TesterEmail, &ts.Strategy, &ts.DurationMins, &ts.Description, &ts.Status, &ts.ProjectName)
		if err != nil {
			return nil, err
		}
		sessions = append(sessions, ts)
	}
	return sessions, nil
}

func (s *TestSessionServiceImpl) Get(id string) (models.TestSession, error) {
	var ts models.TestSession
	err := s.db.QueryRow(`SELECT
			session_id,
			tester_email,
			strategy,
			duration_minutes,
			session_description,
			session_status,
			project_name
		FROM test_session
		WHERE session_id = $1`, id).Scan(
		&ts.Id,
		&ts.TesterEmail,
		&ts.Strategy,
		&ts.DurationMins,
		&ts.Description,
		&ts.Status,
		&ts.ProjectName,
	)

	return ts, err
}
