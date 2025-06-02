package services

import (
	"database/sql"
	"strings"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/models"
)

type StrategyService interface {
	CreateStrategy(models.Strategy) error
	GetAll() ([]models.Strategy, error)
}

type StrategyServiceImpl struct {
	db *sql.DB
}

func NewStrategyServiceImpl(db *sql.DB) StrategyService {
	return &StrategyServiceImpl{
		db: db,
	}
}

func (s *StrategyServiceImpl) CreateStrategy(strategy models.Strategy) error {
	images := ""
	if len(strategy.Images) > 0 {
		images = strings.Join(strategy.Images, ",")
	}
	_, err := s.db.Exec(`
		INSERT INTO strategies (strategy_name, strategy_description, examples, hints, images_urls_comma_sep)
		VALUES ($1, $2, $3, $4, $5)
	`,
		strategy.Name,
		strategy.Description,
		strategy.Examples,
		strategy.Hints,
		images,
	)
	return err
}

func (s *StrategyServiceImpl) GetAll() ([]models.Strategy, error) {
	rows, err := s.db.Query(`SELECT strategy_name, strategy_description, examples, hints, images_urls_comma_sep FROM strategies`)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var strategies []models.Strategy
	for rows.Next() {
		var st models.Strategy
		var imagesComma string
		err := rows.Scan(&st.Name, &st.Description, &st.Examples, &st.Hints, &imagesComma)
		if err != nil {
			return nil, err
		}
		if imagesComma != "" {
			st.Images = strings.Split(imagesComma, ",")
		} else {
			st.Images = []string{}
		}
		strategies = append(strategies, st)
	}
	return strategies, nil
}
