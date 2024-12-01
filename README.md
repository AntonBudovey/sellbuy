# Project README

The goal of this project is to create a web service where people can buy and sell their items, similar to eBay, Shein or AliExpress.

## Technology stack
#### Backend

* Postgres Database 17.1
* Spring Boot 3.3.4
* Java 21
* Data JPA
* Liquibase
* JJWT 0.12.6 (for JSON Web Tokend)
* Swagger 2.6.0 (for API documentation)
* MapStruct 1.6.2 (for object mapping)

#### Other
* Docker
* Docker Compose

## Prerequisites

Before you begin, make sure you have the following installed on your machine(if you want to dockerize app):

- [Docker](https://www.docker.com/products/docker-desktop) (Docker Desktop for Windows/macOS, or Docker Engine for
  Linux)
## Getting Started

Before you start the application, you need to create a .env file in the project root directory containing the following environment variables:<br>
```env
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=bestuser
POSTGRES_HOST=postgres # (use 'localhost' if not using Docker)
JWT_SECRET=kc ndjsfnvksdvkbfksd bksdbkbkfvb 
DEBUG=false
```

You can customize the POSTGRES_USERNAME, POSTGRES_PASSWORD, and JWT_SECRET values.<br>
If you plan to use your own database (not in Docker), set POSTGRES_HOST=localhost.

## Building the Application

1. Clone the project from GitLab.<br> 
2. Make sure your database is working.
3. To build the application, run the following command in the root directory:
```bash
    .\mvnw  clean package
  ```
### Running the Backend Application
To start the backend, run:
```bash
    java -jar target/iti0302-2024-backend-0.0.1-SNAPSHOT.jar
  ```

Alternatively, you can use Maven to run the backend directly:

```bash
.\mvnw spring-boot:run
```

## Running Docker Containers for Both Backend and Frontend

### Step 1: Install Docker

If you don’t have Docker installed, download and install Docker from
the [official Docker website](https://www.docker.com/products/docker-desktop). Ensure that Docker is running after
installation.

### Step 2: Clone Projects

Clone the projects repositories using SSH or HTTPS from GitLab.

### Step 3: Dockerize application

In the root folder, we have both Dockerfile and docker-compose.yaml for containerizing the backend and frontend. <br>
To build the Docker image for both the frontend and backend:
```bash
docker-compose build
   ```
To start both the application (backend, frontend) and the PostgreSQL database, use:

```bash
docker-compose up
   ```

This command will:

 - Build the Docker image (if needed), <br>
 - Import and start the PostgreSQL database container, <br>
 - Start both the frontend and backend containers.