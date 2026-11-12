# Flutter implementation audit

Date: 2026-11-10

## Current state

The Flutter client began as the standard counter template. The current working tree contains a small local-JWT integration in `lib/main.dart`, but it is not feature-first architecture and must be refactored before beta release. It currently includes secure token storage, local-auth login/register/logout, authenticated profile reads, and read-only health/nutrition lists. It does not contain Firebase packages or initialization, Drift, ARB localization, charting, image capture, notifications, or form-driven create/edit flows.

## Backend contract verified

The Spring Boot server runs on port `8090` and exposes only local JWT authentication, profile endpoints, health-measurement CRUD, nutrition food/meal CRUD, and a daily nutrition summary. Details are in `docs/mobile/backend-api-integration-map.md`.

Firebase Admin verification, Cloudinary upload, and OpenAI food analysis are absent from the server source. The Flutter app must not claim those flows work or send Firebase tokens to its protected routes.

## Risks requiring follow-up

- Current `main.dart` violates the requested separation of UI, providers, repositories, and networking.
- No offline cache or synchronization implementation exists.
- Current generated Firebase Android file is untracked; it must match the final Android application ID before Firebase is enabled.
- Health and nutrition list views are real API reads but no create/edit/delete UI has been implemented.
- The previous Android debug build did not return a final success/failure result before the execution window ended.
