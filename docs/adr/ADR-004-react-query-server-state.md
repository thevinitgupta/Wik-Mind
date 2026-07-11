# ADR-004: React Query for Authentication State

**Status:** Accepted

## Decision

Authentication state is treated as server state using TanStack Query.

## Design

-   Source of truth: GET /api/auth/me
-   Query key: \['user'\]
-   No isLoggedIn flag
-   No JWT stored in frontend

## Benefits

-   Centralized user cache
-   Reduced API calls
-   Automatic synchronization
