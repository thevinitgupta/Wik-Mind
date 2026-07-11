# ADR-006: PostgreSQL as Primary Database

**Status:** Accepted

## Context

Wik Mind requires transactional consistency and rich relational
querying.

## Decision

Use PostgreSQL as the primary operational database.

## Rationale

-   ACID compliance
-   Strong relational modeling
-   JSONB support
-   pgvector integration
-   Mature indexing and extensions
