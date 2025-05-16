package common

import (
	"log/slog"
	"os"
	"strings"
)

// LOG_LEVEL defines the logging level for the application, defaulting to "INFO".
var LOG_LEVEL string = strings.ToUpper(GetEnvVarDefault("LOG_LEVEL", "INFO"))

// InitSlogger initializes the global logger with the specified log level.
func InitSlogger() {
	levelsMap := map[string]slog.Level{
		"DEBUG":   slog.LevelDebug,
		"INFO":    slog.LevelInfo,
		"WARN":    slog.LevelWarn,
		"WARNING": slog.LevelWarn,
		"ERROR":   slog.LevelError,
	}

	logger := slog.New(slog.NewTextHandler(
		os.Stdout,
		&slog.HandlerOptions{
			AddSource: true,
			Level:     levelsMap[LOG_LEVEL],
		},
	))

	slog.SetDefault(logger)
}
