# LifeKhmer mobile/backend integration map

_Audited from the Spring Boot controllers and DTOs on 2026-11-10. The API listens on port `8090`; all paths below are exact controller paths._

## Transport and envelope

- Protected endpoints expect `Authorization: Bearer <application access JWT>`. Firebase ID tokens are **not** supported by the current backend.
- Successful JSON responses use `{ "success": true, "message": "…", "data": … }`.
- Errors use `{ timestamp, status, error, message, path, validationErrors }`.
- List endpoints return Spring `Page` data under `data` (`content`, `totalElements`, `totalPages`, and related paging fields).
- Android emulator development base URL: `http://10.0.2.2:8090/api/v1/`. This is configurable in the mobile app and is not a production URL.

## Implemented endpoints

| Module | Endpoint | Method | Authentication | Implementation status |
|---|---|---:|---|---|
| Local auth | `/api/v1/auth/register` | POST | No | Implemented |
| Local auth | `/api/v1/auth/login` | POST | No | Implemented |
| Local auth | `/api/v1/auth/refresh` | POST | No | Implemented |
| Local auth | `/api/v1/auth/logout` | POST | No | Implemented; revokes supplied refresh token |
| Profile | `/api/v1/accounts/me` | GET | Access JWT | Implemented |
| Profile | `/api/v1/accounts/me` | PUT | Access JWT | Implemented |
| Health measurements | `/api/v1/health/measurements` | POST | Access JWT | Implemented |
| Health measurements | `/api/v1/health/measurements` | GET | Access JWT | Implemented; paginated/filterable |
| Health measurements | `/api/v1/health/measurements/{id}` | GET | Access JWT | Implemented |
| Health measurements | `/api/v1/health/measurements/{id}` | PUT | Access JWT | Implemented |
| Health measurements | `/api/v1/health/measurements/{id}` | DELETE | Access JWT | Implemented |
| Nutrition catalogue | `/api/v1/nutrition/foods` | GET | Access JWT | Implemented; paginated search |
| Nutrition catalogue | `/api/v1/nutrition/foods` | POST | Access JWT | Implemented |
| Nutrition diary | `/api/v1/nutrition/meals` | POST | Access JWT | Implemented |
| Nutrition diary | `/api/v1/nutrition/meals` | GET | Access JWT | Implemented; paginated/date filter |
| Nutrition diary | `/api/v1/nutrition/meals/{id}` | PUT | Access JWT | Implemented |
| Nutrition diary | `/api/v1/nutrition/meals/{id}` | DELETE | Access JWT | Implemented |
| Nutrition summary | `/api/v1/nutrition/summary` | GET | Access JWT | Implemented; daily only |

## Actual request and response contracts

### Local authentication

`POST /auth/register`

```json
{"fullName":"Sokha","email":"sokha@example.com","password":"at-least-8-characters","dateOfBirth":"1995-04-15"}
```

`POST /auth/login`

```json
{"email":"sokha@example.com","password":"at-least-8-characters"}
```

Login returns `data` with `userId`, `accountId`, `fullName`, `email`, `accessToken`, `refreshToken`, `tokenType`, `expiresIn`, `createdAt`, and `updatedAt`. `POST /auth/refresh` and `POST /auth/logout` receive `{ "refreshToken": "…" }`.

### Profile

`PUT /accounts/me` accepts:

```json
{"fullName":"Sokha","dateOfBirth":"1995-04-15","gender":"FEMALE","heightCm":160.0,"weightKg":55.5,"preferredLanguage":"km"}
```

`data` from the profile endpoints includes `id`, `fullName`, `dateOfBirth`, `gender`, `heightCm`, `weightKg`, `preferredLanguage`, `profileImageKey`, `status`, `createdAt`, and `updatedAt`.

### Health measurements

`measurementType` is one of the backend `MeasurementType` enum values. The mobile client must retrieve/keep values compatible with the deployed backend enum rather than inventing new types. Create/update accepts:

```json
{"measurementType":"WEIGHT","value":55.5,"secondaryValue":null,"unit":"kg","recordedAt":"2026-11-10T09:30:00","notes":"Optional"}
```

For blood pressure, `secondaryValue` is required; for every other type it must be absent/null. List query parameters are `type`, `from`, `to`, `page` (default `0`), and `size` (default `20`, server-capped at `100`).

### Nutrition

Food creation accepts `name`, `caloriesPer100g`, `proteinPer100g`, `carbohydratesPer100g`, `fatPer100g`, `sugarPer100g`, and `fiberPer100g`. It is globally catalogued, so the mobile UI must not present it as a private user-created food.

Meal create/update accepts:

```json
{"foodId":"UUID","mealType":"BREAKFAST","portionGrams":250,"consumedAt":"2026-11-10T08:00:00","notes":"Optional"}
```

The server calculates the meal nutrients. Meal lists accept `from`, `to`, `page`, and `size`; `/nutrition/summary` accepts optional `date` (`YYYY-MM-DD`) and is daily only.

## Missing APIs and smallest required backend changes

| Mobile requirement | Current status | Smallest backend change required |
|---|---|---|
| Firebase login/registration/synchronization | Missing | Firebase Admin token verification, `FIREBASE` account provider, sync/me contract, and protected Firebase bearer filter. |
| Dashboard aggregate/latest values | Missing | A read-only authenticated dashboard endpoint, or explicit mobile aggregation agreement; no fabricated dashboard data. |
| Profile image upload/delete | Missing | Authenticated multipart endpoints plus Cloudinary metadata storage. |
| Food image upload/AI analysis/history | Missing | Authenticated multipart and analysis/history endpoints; return explicit estimate/confidence fields. |
| Weekly nutrition summary | Missing | Aggregated date-range summary endpoint, or mobile must label its client aggregation as local presentation only. |
| Medication/reminder synchronization | Missing | Medication, schedule, adherence, and device-timezone APIs. |
| FCM device registration/notification centre | Missing | Device-token and notification-history APIs. |
| Appointments | Missing | Appointment CRUD endpoints. |
| Symptoms | Missing | Symptom CRUD endpoints. |
| Family sharing | Missing | Family/group/invitation/permission endpoints with owner-scoped authorization. |

## Mobile integration policy

The Flutter application must use the local JWT flow for the implemented endpoints today. Firebase UI and Firebase bearer tokens are kept behind interfaces until the backend Firebase migration exists. Features without an endpoint show an explicit unavailable/offline state and never report fabricated successful server actions.
