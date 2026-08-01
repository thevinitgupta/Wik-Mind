# Backend Setup

```bash
cd infrastructure/docker
docker compose up -d
```

```bash
cd backend/api
./mvnw spring-boot:run
```

Responsibilities:

- Flyway migrations
- Object storage initialization
- REST API startup
