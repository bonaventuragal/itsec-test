# Getting Started

## Prerequisites
* Java 17
* PostgreSQL 16

## Running the Development Server
* Clone the repo
* Create a PostgreSQL database

Running using maven:
* Create a file `application-local.properties` in `src/main/resources`
* Copy the content of `application.properties`, replacing the variable placeholder with the correct values
* Run `mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local`

Running using VS Code with Spring Boot Extension Pack
* Create a file `.env`
* copy the content of `.env.example`, fill the variable with the correct values
* Run the server through Spring Boot Dashboard

## Swagger UI
After running the server, Swagger UI is available on `http://localhost:8080/swagger-ui/index.html`

