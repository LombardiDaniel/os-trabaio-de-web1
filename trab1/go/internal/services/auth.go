package services

import (
	"context"

	"github.com/lombardidaniel/os-trab-de-web1/trab1/go/internal/domain"
)

type AuthService interface {
	InitToken(ctx context.Context, userId uint32, email string, organizationId *string) (string, error)

	// Permissions retrieves the permissions for user in organization.
	Permissions(ctx context.Context, userId uint32, organizationId *string) (map[string]domain.Permission, error)

	// ValidateToken checks the validity of a given JWT.
	ValidateToken(tokenString string) error

	// ParseToken extracts claims from a given JWT.
	ParseToken(tokenString string) (domain.JwtClaims, error)
}
