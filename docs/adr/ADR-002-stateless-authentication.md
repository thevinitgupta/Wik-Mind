# ADR-002: Stateless Authentication

**Status:** Accepted

## Context

The backend should scale horizontally without session replication.

## Decision

Use stateless JWT authentication with short-lived access tokens and
rotating refresh tokens.

## Consequences

-   Horizontal scalability
-   No server-side HTTP session
-   Authentication handled via JWT validation
