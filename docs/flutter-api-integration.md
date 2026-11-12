# API integration

Set the development base URL with `--dart-define=API_BASE_URL=http://10.0.2.2:8090/api/v1/`. The backend uses bearer application access JWTs, not Firebase ID tokens. Tokens are stored in Flutter Secure Storage and attached only to protected API requests.

Implemented mobile reads: `GET /accounts/me`, `GET /health/measurements`, and `GET /nutrition/meals`. Authentication calls use `/auth/register`, `/auth/login`, and `/auth/logout`. Refer to the integration map for request and response schemas.
