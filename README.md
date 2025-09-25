# CRUD API (Spring Boot)

A RESTful CRUD API for managing users, built with Spring Boot 3, Spring Data JPA, Bean Validation, and H2 in-memory database. Includes unit, slice, repository, and full integration tests. Example CI is provided via Jenkins running on AWS EC2.

## Features
- Create, Read, Update, Delete users
- Validation with `@NotBlank` and `@Email`
- Global exception handling
- H2 in-memory database (console enabled)
- Comprehensive test suite (JUnit 5, Mockito, MockMvc, DataJpaTest, Integration)

## Requirements
- Java 21+
- Maven 3.9+

## Getting Started

### Run locally
```bash
mvn spring-boot:run
```
App runs at `http://localhost:8081`. H2 console at `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:cruddb`).

### Build
```bash
mvn clean package
```
Artifact: `target/crudapi-0.0.1-SNAPSHOT.jar`

### Run jar
```bash
java -jar target/crudapi-0.0.1-SNAPSHOT.jar
```

## API Endpoints
Base path: `/api/users`

- POST `/` create user
- GET `/` list users
- GET `/{id}` get by id
- PUT `/{id}` update
- DELETE `/{id}` delete

Example (create):
```bash
curl -X POST http://localhost:8081/api/users \
  -H 'Content-Type: application/json' \
  -d '{"name":"Alice","email":"alice@example.com"}'
```

## Configuration
Key settings in `src/main/resources/application.properties`:
- `spring.datasource.url=jdbc:h2:mem:cruddb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.h2.console.enabled=true`
- `server.port=8081`

## Tests
Run all tests:
```bash
mvn test
```
Test types included:
- Unit: `UserServiceTest` with Mockito
- Controller slice: `UserControllerTest` with MockMvc
- Repository: `UserRepositoryTest` with H2 and `@DataJpaTest`
- Integration: `UserIntegrationTest` boots app on random port and calls real HTTP endpoints

## CI with Jenkins on AWS EC2
This project includes a `Jenkinsfile` pipeline. Typical steps:
1. Jenkins agent (on EC2) checks out repo
2. Validate/build: `mvn -q -DskipTests=false test` and `mvn -q clean package`
3. Archive test reports and `target/*.jar`

### Prerequisites
- EC2 instance with Java and Maven installed
- Jenkins installed on EC2 (or connected agent)
- Credentials (if needed) for repo access

### Minimal Jenkins setup
- Install plugins: Pipeline, JUnit, Git
- Create a Pipeline job pointing to this repo
- Ensure working directory has network access to fetch dependencies

### Jenkinsfile overview
- Uses Maven wrapper `mvnw` if present; otherwise system Maven
- Stages: Checkout → Build & Unit Tests → Integration Tests → Package → Archive
- Publishes JUnit reports from `target/surefire-reports/*.xml`

If your EC2 has limited RAM/CPU, you can speed up builds:
```bash
mvn -q -T1C -DskipITs=false -DskipTests=false clean verify
```

## Deployment (optional)
For a simple EC2 deployment after build:
```bash
scp target/crudapi-0.0.1-SNAPSHOT.jar ec2-user@<EC2_PUBLIC_IP>:/opt/crudapi/
ssh ec2-user@<EC2_PUBLIC_IP> "nohup java -jar /opt/crudapi/crudapi-0.0.1-SNAPSHOT.jar > /opt/crudapi/app.log 2>&1 &"
```
Consider using a service manager (systemd) or containerization for production.

## Project Structure
```
src/
  main/
    java/com/example/crudapi/...
    resources/application.properties
  test/
    java/com/example/crudapi/
      service/UserServiceTest.java
      controller/UserControllerTest.java
      repository/UserRepositoryTest.java
      integration/UserIntegrationTest.java
```


