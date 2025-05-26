#### - DEV - ####
FROM golang:1.24.2 AS dev

WORKDIR /app

COPY ./go.mod go.mod
COPY ./go.sum go.sum
RUN go mod download

COPY ./ ./

CMD ["go", "run", "."]
