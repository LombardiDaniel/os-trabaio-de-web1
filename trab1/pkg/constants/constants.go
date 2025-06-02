package constants

import "github.com/lombardidaniel/os-trab-de-web1/trab1/go/pkg/common"

var (
	AppName        string = "quack-app"
	AuthCookieName        = AppName + "-userSessionId"
	S3Bucket       string = common.GetEnvVarDefault("S3_BUCKET", AppName+"-dump")
)
