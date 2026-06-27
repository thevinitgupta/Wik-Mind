# Backend Architecture

## Technology

-   Spring Boot
-   Spring AI
-   Spring Security
-   Spring Data JPA
-   Validation
-   Docker

## Domain Modules

-   auth
-   users
-   workspace
-   ingestion
-   ai
-   summarization
-   search
-   graph
-   embeddings
-   common

Each module contains controller, service, repository, dto and model
packages where applicable.

## Responsibilities

Controllers expose REST APIs. Services implement business logic.
Repositories persist data. AI package orchestrates prompt workflows.
