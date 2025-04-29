.PHONY: deploy build run

deploy:
	mvn deploy

build:
	docker build -t tomcat-app .

run:
	docker run --rm -p 8080:8080 tomcat-app

all: deploy build run