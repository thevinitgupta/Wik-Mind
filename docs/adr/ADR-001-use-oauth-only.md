# ADR-001: Use OAuth Providers Only

**Status:** Accepted

## Context

Wik Mind requires secure authentication while minimizing operational
complexity and security risks.

## Decision

Use OAuth providers (starting with Google) as the only authentication
mechanism for the MVP.

## Consequences

### Positive

-   No password storage
-   Reduced attack surface
-   Easier onboarding
-   Easy future support for GitHub, Notion, Microsoft

### Negative

-   Dependent on provider availability
-   Requires OAuth configuration
