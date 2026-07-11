# ADR-005: Refresh Token Rotation

**Status:** Accepted

## Decision

Implement rotating refresh tokens with reuse detection.

## Flow

1.  Login issues Access + Refresh tokens.
2.  Refresh endpoint validates current refresh token.
3.  New access and refresh tokens are issued.
4.  Previous refresh token is revoked.
5.  Reuse of revoked token invalidates the session.

## Benefits

-   Improved session security
-   Detects stolen refresh tokens
