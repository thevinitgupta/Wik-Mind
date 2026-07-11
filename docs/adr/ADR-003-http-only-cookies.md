# ADR-003: HttpOnly Cookie Strategy

**Status:** Accepted

## Decision

Store Access and Refresh JWTs in Secure, HttpOnly cookies.

## Rationale

-   Protects tokens from JavaScript (XSS)
-   Browser automatically includes cookies
-   Supports SameSite protection
-   Keeps frontend token-agnostic

## Cookie Strategy

  Cookie          Purpose
  --------------- --------------------
  access_token    API authentication
  refresh_token   Token refresh only
