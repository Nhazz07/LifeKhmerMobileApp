# API error handling

All API failures use this JSON shape:

```json
{
  "timestamp": "2026-11-10T18:50:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/auth/register",
  "validationErrors": { "email": "Please provide a valid email" }
}
```

`validationErrors` is `null` when field-level validation does not apply. Controllers should throw the common exceptions for expected business failures: `ResourceNotFoundException` (404), `BadRequestException` (400), `UnauthorizedException` (401), `ForbiddenException` (403), and `ConflictException` or `DuplicateResourceException` (409).

Authentication and authorization failures raised by Spring Security are returned by dedicated security handlers, so they use the same payload before controller advice is reached. Unexpected errors are logged on the server and return only the generic message `An unexpected error occurred`.
