# Authentication API

## Endpoints

  Method   Endpoint                       Description
  -------- ------------------------------ ----------------------------
  GET      /oauth2/authorization/google   Start OAuth login
  
  GET      /api/user                   Current authenticated user
  POST     /api/auth/refresh              Rotate refresh token
  POST     /api/auth/logout               Logout user

## Authentication

JWTs are transported using Secure HttpOnly cookies. Clients never read
or store tokens directly.
