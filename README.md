# Getting Started

## Prerequisites
* Java 17
* PostgreSQL 16
* Redis
* Gmail account with app password setup

## Running the Development Server
* Clone the repo
* Create a PostgreSQL database

Running using maven:
* Create a file `application-local.properties` in `src/main/resources`
* Copy the content of `application.properties`, replacing the variable placeholder with the correct values
* Run `mvnw spring-boot:run -Dspring-boot.run.profiles=local`

## Running Unit Test
* Run `mvnw test`
* The coverage result is available in `target/site/jacoco/index.html`

## Running on Docker
* Create `.env` file with this content
```
DB_HOST=db  # keep as `db`, in sync with `docker-compose.yml`
DB_PORT=5432
DB_NAME=db_name
DB_USERNAME=db_user
DB_PASSWORD=db_password

JWT_SECRET=secret

REDIS_HOST=redis  # keep as `redis`, in sync with `docker-compose.yml`
REDIS_PORT=6379
REDIS_PASSWORD=redis_password

MAIL_USERNAME=email
MAIL_PASSWORD=email_app_password
```
> The email is used to send OTP. App Password can be generated on https://myaccount.google.com/apppasswords
* Run `docker-compose up -d`
  * The application will run on port `8080`
  * PostgreSQL instance will run on port `5433`
  * Redis instance will run on port `6379`

> The image for this app is available on https://hub.docker.com/r/bonaventuragal/itsec-test

## Swagger UI
After running the server, Swagger UI is available on `http://localhost:8080/swagger-ui/index.html`

## Auth Flow
* Make a register request. Upon completion, an OTP will be sent to the entered email.
* Enter the OTP together with the registered email/username to verify account.
* Make a login request. An OTP will be sent to the entered email.
* Enter the OTP together with the accoun't email/username to verify. A JWT will be generated and can be used to access other endpoints.
> The application includes some dummy data seeded during initialization. However, OTP verification cannot be completed because the seeded email addresses are not real.