# Project README

the aim of that project is create to web service where people can buy and sell their stuff (like ebay or aliexpress)

## Technology stack

* Postgres Database 17.1
* Data JPA
* Liquibase
* React
* JJWT 0.12.6
* Spring Boot 3.3.4
* Java 21
* Swagger 2.6.0
* MapStruct 1.6.2

## Prerequisites

Before you begin, make sure you have the following installed on your machine(if you want to dockerize app):

- [Docker](https://www.docker.com/products/docker-desktop) (Docker Desktop for Windows/macOS, or Docker Engine for
  Linux)

## Getting Started

Before you start, you need to create a file in project root directory `.env` containing:<br>
`POSTGRES_USERNAME=postgres`<br>
`POSTGRES_PASSWORD=bestuser`<br>
`POSTGRES_HOST=postgres` (if you want to use a Docker)<br>
`JWT_SECRET=kc ndjsfnvksdvkbfksd bksdbkbkfvb` (maybe any long random char sequence)<br>
`DEBUG=false`<br>

You can use your own username and password.<br>
If you want to run application without Docker with your own database so use `POSTGRES_HOST=localhost`

## How to start application
create .env file with `POSTGRES_HOST=postgres`<br>
And in the same directory create docker-compose.yaml file:
```yaml
version: "3.8"
services:
  backend:
    image: anbudo2/iti0302-2024-backend
    container_name: iti0302-2024-backend
    env_file: .env
    ports:
      - "8080:8080"
    depends_on:
      - postgres
  postgres:
    container_name: iti0302-2024-backend-postgres
    image: postgres
    environment:
      POSTGRES_USER: ${POSTGRES_USERNAME}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
```

And run `docker-compose up`<br>
now backend work on port 8080 and you acn login swagger through http://localhost:8080/swagger-ui/index.html

## How to build  and run app

1. Firstly clone app from gitlab
2. make sure that your database is working
3. From app root directory write command to build app:
     ```bash
    .\mvnw  clean package
    ```
4. Then to start application
     ```bash
    java -jar target/iti0302-2024-backend-0.0.1-SNAPSHOT.jar
    ```

Or you can just write this command

```bash
.\mvnw spring-boot:run
```

## How to run docker container

### Step 1: Install Docker

If you don’t have Docker installed, download and install Docker from
the [official Docker website](https://www.docker.com/products/docker-desktop). Ensure that Docker is running after
installation.

### Step 2: Clone Project

Clone project with ssh or https from gitlab repo

### Step 3: Dockerize application

1. in root folder we have Dockerfile and docker_compose.yaml
2. You can just build docker application container but it would be without database(you need your own working postgres)
    ```bash
    docker build .
    ```
2. To start your Docker containers with app and db, run the following command:

    ```bash
    docker-compose up
    ```

   This command will:
    - Build the Docker image (if required).
    - import and start postgres db container
    - Start the containers as defined in the `docker-compose.yaml` file.
