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

#### Frontend
* React 18.3.1
* Vite 5.4.8
* Tailwind CSS 3.4.14
* Axios 1.7.7
* React Router 6.27.0

#### Other
* Docker
* Docker Compose

## Prerequisites

Before you begin, make sure you have the following installed on your machine(if you want to dockerize app):

- [Docker](https://www.docker.com/products/docker-desktop) (Docker Desktop for Windows/macOS, or Docker Engine for
  Linux)
- Node.js (for React frontend development)

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

## How to start application
1.	Create a .env file as mentioned above.<br>
2.	In the same directory, create a docker-compose.yaml file with the following content:
```yaml
version: "3.8"
services:
   backend:
      image: anbudo2/iti0302-2024-backend
      env_file: .env
      ports:
         - "8080:8080"
      networks:
         - app
      depends_on:
         - postgres

   frontend:
      image: madlinnov/front-application
      networks:
         - app
      ports:
         - "3000:3000"
      depends_on:
         - backend

   postgres:
      container_name: iti0302-2024-backend-postgres
      image: postgres
      networks:
         - app
      environment:
         POSTGRES_USER: ${POSTGRES_USERNAME}
         POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
networks:
   app:
      driver: bridge
```

And run `docker-compose up`<br>
Run the following command to start the application (both backend and frontend) along with the PostgreSQL database:
 ```bash
    docker-compose up
  ```
This will start: <br>
•	The backend on port 8080, <br>
•	The frontend on port 3000, <br>
•	PostgreSQL database for persistence. <br>

You can access the Swagger UI for the backend at http://localhost:8080/swagger-ui/index.html, and the React frontend will be available at http://localhost:3000.
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

### Running the Frontend (React with Vite)
1. Clone the project from GitLab.<br>
   https://gitlab.cs.taltech.ee/anbudo/iti0302-2024-frontend.git
2.	Install the necessary dependencies using npm or yarn:
```bash
npm install  # or yarn install
```
3. Run the frontend app locally in development mode:
```bash
npm run dev  # or yarn dev
```
The React app will be running on http://localhost:3000, and it will interact with the backend through the API URL provided in the .env file.
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
## Link to website
http://sellbuy.myvnc.com/
