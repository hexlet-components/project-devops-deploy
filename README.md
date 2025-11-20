# Project DevOps Deploy

Bulletin board service.

The default `dev` profile uses an in-memory H2 database and seeds 10 sample bulletins through `DataInitializer`, so the API works immediately after startup.

API documentation is available via Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

## Requirements

- JDK 21+.
- Gradle 9.2.1.
- PostgreSQL only if you run the `prod` profile with an external database.
- Make.

## Running

### Local profile
1. Start the application:
    ```bash

    make run
    ```
3. Explore the API:
    - All bulletins: `GET http://localhost:8080/api/bulletins`
    - Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Production

Prepare your database and export connection parameters:

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bulletins
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
```

Build app:

```bash
make build
java -jar build/libs/project-devops-deploy-0.0.1-SNAPSHOT.jar
```

### Useful commands

See [Makefile](./Makefile)

## Frontend

### Development

1. Install Node.js 20 LTS (or newer) and npm 10.
2. Install dependencies and start the Vite dev server:
   ```bash
   cd frontend
   make install
   make start
   ```
3. The dev server proxies `/api` requests to `http://localhost:8080`, so keep the backend running via `make run` (or `./gradlew bootRun`) in another terminal.

### Image upload flow

1. Upload files via `POST /api/files/upload` (multipart form field named `file`).
2. The response contains `key` and a temporary `url`. Persist the `key` in the `imageKey` field when creating or updating bulletins; the backend stores only that identifier.
3. When you need a fresh link, call `GET /api/files/view?key=...` to receive a new URL (the backend issues presigned links on demand).

### Build and serve from the Java app

1. Build the production bundle:
   ```bash
   cd frontend
   make install      # run once
   make build    # outputs to frontend/dist
   ```
2. Copy the compiled assets into Spring Boot’s static resources (served from `src/main/resources/static`):
   ```bash
   rm -rf src/main/resources/static
   mkdir -p src/main/resources/static
   cp -R frontend/dist/* src/main/resources/static/
   ```
3. Restart the backend (`make run`) and open `http://localhost:8080/` — the React app will now be served directly by the Java application.
