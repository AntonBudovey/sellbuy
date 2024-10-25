# Project README

This project uses Docker for containerization. Follow the steps below to get your environment up and running using Docker Compose inside IntelliJ IDEA.

## Prerequisites

Before you begin, make sure you have the following installed on your machine:

- [Docker](https://www.docker.com/products/docker-desktop) (Docker Desktop for Windows/macOS, or Docker Engine for Linux)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Getting Started

Before you start, you need to create a file in project root directory `.env` containing:<br>
`POSTGRES_USERNAME=postgres`<br>
`POSTGRES_PASSWORD=bestuser`<br>
`POSTGRES_HOST=postgres` (if you want to use a Docker)<br> 

You can use your own username and password.<br>
If you want to run application without Docker with your own database so use `POSTGRES_HOST=localhost`

## How to build app
From app root directory write command:
     ```bash
    mvn clean package
    ```
## How to start application
Execute Iti03022024BackendApplication.jar file


### Step 1: Install Docker

If you don’t have Docker installed, download and install Docker from the [official Docker website](https://www.docker.com/products/docker-desktop). Ensure that Docker is running after installation.

### Step 2: Open the Project in IntelliJ IDEA

1. Open **IntelliJ IDEA** and navigate to the project directory.
2. Make sure the project contains a `docker-compose.yaml` file in its root directory.

### Step 3: Open IntelliJ Terminal

1. In IntelliJ IDEA, open the terminal by selecting the **Terminal** tab at the bottom of the IDE.


### Step 4: Run Docker Compose

1. In the IntelliJ terminal, ensure you are in the root directory of your project (where `docker-compose.yaml` is located).
2. To start your Docker containers, run the following command:

    ```bash
    docker-compose up
    ```

   This command will:
    - Build the Docker image (if required).
    - Pull any necessary dependencies.
    - Start the containers as defined in the `docker-compose.yaml` file.
