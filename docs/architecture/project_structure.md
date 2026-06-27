# WIK Mind - Project Structure & Architecture Roadmap

> Status: Living document (update as the project evolves)

## Purpose

This document describes the recommended repository structure,
architectural approach, and future evolution of WIK Mind.

------------------------------------------------------------------------

# Guiding Principles

-   Start as a **Modular Monolith**
-   Avoid premature microservices
-   Keep domain logic separated into modules
-   Dockerize every runtime dependency
-   Design so components can later be extracted into independent
    services

------------------------------------------------------------------------

# Technology Stack

## Frontend

-   Next.js (App Router)
-   React
-   TanStack Query
-   Zustand
-   Tailwind CSS
-   shadcn/ui
-   React Flow
-   Tiptap
-   Three.js (future)

## Backend

-   Spring Boot
-   Spring AI
-   Spring Security
-   PostgreSQL
-   Redis
-   Docker

Future:

-   pgvector
-   Neo4j
-   Elasticsearch/OpenSearch
-   Kafka (optional)
-   Ollama/OpenAI/Gemini providers

------------------------------------------------------------------------

# Repository Structure

``` text
wik-mind/
│
├── apps/
│   └── web/
│
├── backend/
│   └── api/
│
├── libraries/
│   ├── prompts/
│   └── shared/
│
├── infrastructure/
│   ├── docker/
│   ├── postgres/
│   ├── redis/
│   └── neo4j/          # future
│
├── docs/
│   ├── architecture/
│   ├── adr/
│   ├── diagrams/
│   └── roadmap/
│
├── docker-compose.yml
├── .env.example
└── README.md
```

------------------------------------------------------------------------

# apps/

Contains user-facing applications.

-   web (Next.js)
-   desktop (future)
-   mobile (future)

------------------------------------------------------------------------

# backend/

Single Spring Boot + Spring AI application.

Suggested package structure:

``` text
com.wikmind

    ai/
    auth/
    workspace/
    graph/
    search/
    ingestion/
    summarization/
    embeddings/
    users/
    common/
```

Each folder is a domain module rather than a separate application.

------------------------------------------------------------------------

# libraries/

Shared assets that are not tied to one module.

## prompts/

Stores prompt templates.

Examples:

-   summarize.st
-   contradiction.st
-   entity.st
-   relation.st
-   topic.st

Benefits:

-   version control
-   easier testing
-   reusable prompts

## shared/

Future location for:

-   utility classes
-   common DTOs
-   exceptions
-   logging
-   shared validation

------------------------------------------------------------------------

# infrastructure/

Contains runtime infrastructure instead of application logic.

Examples:

## docker/

Dockerfiles and compose fragments.

## postgres/

-   init.sql
-   seed.sql

## redis/

Redis configuration.

## neo4j/ (future)

Cypher scripts, constraints and indexes.

------------------------------------------------------------------------

# docs/

Suggested layout:

``` text
docs/

    architecture/
    adr/
    diagrams/
    roadmap/
```

## architecture/

Overall architecture documents.

## adr/

Architecture Decision Records.

Examples:

-   ADR-0001-use-nextjs.md
-   ADR-0002-modular-monolith.md
-   ADR-0003-postgresql-before-neo4j.md

## diagrams/

Architecture diagrams, sequence diagrams, ERDs.

## roadmap/

Feature roadmap and milestones.

------------------------------------------------------------------------

# Version Control

Branch strategy:

-   main
-   feature/\*
-   bugfix/\*
-   hotfix/\*

Commit format:

-   feat:
-   fix:
-   refactor:
-   docs:
-   chore:
-   perf:
-   build:

Semantic Versioning:

-   v0.1.0
-   v0.2.0
-   v0.3.0
-   ...
-   v1.0.0

------------------------------------------------------------------------

# Evolution Roadmap

## Phase 1

-   Authentication
-   Workspaces
-   Summarization
-   Docker
-   PostgreSQL

## Phase 2

-   Claim extraction
-   Topic extraction
-   Relation extraction
-   Prompt library

## Phase 3

-   Embeddings
-   pgvector
-   Semantic search

## Phase 4

-   Knowledge graph
-   Neo4j
-   React Flow visualization
-   3D graph exploration

## Phase 5

-   AI agents
-   Long-term memory
-   Citation validation
-   Research workflows

------------------------------------------------------------------------

# Possible Future Service Extraction

Initially everything runs inside one Spring Boot application.

If needed later, modules can become independent services.

``` text
services/

    api/
    ai/
    search/
    ingestion/
    auth/
```

Extract only when there is a real scalability or deployment need.

------------------------------------------------------------------------

# Long-Term Vision

WIK Mind aims to evolve from an AI-powered summarization application
into a knowledge platform featuring:

-   Knowledge extraction
-   Claim tracking
-   Contradiction detection
-   Semantic search
-   Knowledge graph visualization
-   AI-assisted research
-   Multi-workspace collaboration
-   Extensible AI provider support
-   Event-driven processing (future)

This document should be updated whenever major architectural decisions
are made.
