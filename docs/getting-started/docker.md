# Docker Infrastructure

## Infrastructure Location

```
infrastructure/docker
```

## Core Services

| Service | Purpose | Port |
|----------|---------|------|
| PostgreSQL | Metadata Database | 5432 |
| SeaweedFS S3 | Object Storage | 8333 |
| SeaweedFS Master UI | Cluster UI | 9333 |
| SeaweedFS Filer UI | File Browser | 8888 |
| SeaweedFS Admin UI | Administration | 23646 |

## Start Infrastructure

```bash
cd infrastructure/docker
docker compose \
  -f compose.yaml \
  -f compose.storage.yaml \
  up -d
```

## Verify

```bash
docker ps
```

Expected containers:

- wikmind-postgres
- wikmind-storage

Verify S3 endpoint:

```bash
curl http://localhost:8333
```

Expected:

```xml
<ListAllMyBucketsResult>...</ListAllMyBucketsResult>
```

Master UI: http://localhost:9333

Filer UI: http://localhost:8888

Admin UI: http://localhost:23646

## Stop

```bash
docker compose down
```

> Avoid `docker compose down -v` unless you intentionally want to delete local data.
