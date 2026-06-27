# System Overview

## Vision

WIK Mind is an AI-assisted knowledge platform that transforms
unstructured information into an explorable knowledge base. The platform
extracts claims, entities, topics, relationships and evidence from
documents, enabling semantic search, contradiction detection and
interactive graph exploration.

## High-Level Architecture

-   Frontend: Next.js (App Router), TanStack Query, Zustand,
    TailwindCSS.
-   Backend: Modular Spring Boot + Spring AI.
-   Data: PostgreSQL, Redis, pgvector (future), Neo4j (future).
-   AI: Prompt workflows, embeddings, summarization, extraction.
-   Deployment: Docker Compose locally, CI/CD for production.

## Core Principles

-   Modular monolith first
-   Domain-driven modules
-   Event-driven expansion when needed
-   AI-provider abstraction
-   Infrastructure as code
