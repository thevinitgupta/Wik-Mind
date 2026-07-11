# User API

## User Model

``` text
User
- id (UUID)
- email
- displayName
- avatarUrl
- createdAt
```

## Current User Response

``` json
{
  "id": "uuid",
  "displayName": "John Doe",
  "avatarUrl" : "https://ggle.com/url",
  "email": "john@example.com",
  "roles": ["USER"]
}
```

The authenticated user is retrieved through `GET /api/user`.
