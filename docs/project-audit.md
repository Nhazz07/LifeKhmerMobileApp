# LifeKhmer Comprehensive Project Audit

**Date:** 2026-11-11  
**Auditors:** LifeKhmer Full-Stack Architecture Team  
**Scope:** Backend (`backend/life-khmer-api`), Mobile (`mobile/life_khmer`), Database (`PostgreSQL`), and Integrations  

---

## 1. Executive Summary

LifeKhmer is an application designed to empower Cambodian users with personal health tracking, nutrition logging, and AI-assisted food analysis. 

The audit evaluated the existing codebase against the target production-minded modular monolith architecture, strict guest exploration requirements, authenticated personalized health management, and high-quality UI/UX.

### Key Audit Findings:
1. **Working & Solid Foundation:**
   - Spring Boot 4.x / Java 21 backend builds cleanly with Maven.
   - JWT authentication architecture (BCrypt, access token + refresh token rotation with SHA-256 hashing) is implemented and verified by 20 unit tests.
   - PostgreSQL schema with Flyway migrations V1 (Identity), V2 (Health), and V3 (Nutrition) is well-formed.
   - Flutter 3.47.7 & Dart 3.13.5 analyze and pass basic tests.

2. **Critical Architecture & Functional Gaps:**
   - **Guest Mode Broken at Root:** 
     - Backend: `SecurityConfig` locks `/api/v1/nutrition/foods/**` behind authentication. Guests receive `401 Unauthorized` when browsing or searching foods.
     - Flutter: `GoRouter` in `lib/main.dart` forcefully redirects all unauthenticated requests to `/login`. No guest state, guest dashboard, or guest exploration exists.
   - **Monolithic Flutter Structure:** Flutter code is condensed into `lib/main.dart` with raw Dio calls, untyped maps, and no feature-first domain/data/presentation layering.
   - **Missing Backend Modules & APIs:**
     - Medication management & adherence logs.
     - Appointments management.
     - Symptom tracking.
     - Aggregated Dashboard endpoint (`/api/v1/dashboard/summary`).
     - AI Food Analysis pipeline (Cloudinary upload + OpenAI vision analysis with nutrition estimation).
     - Food detail retrieval (`GET /api/v1/nutrition/foods/{id}`) and food categories.
   - **Missing Flutter Features & Polish:**
     - Missing screens: Onboarding, Guest Dashboard, Authenticated Dashboard, AI Food Scanner, Medication Management, Appointments, Symptoms, Profile Editor, Settings.
     - Missing guest conversion flow (bottom sheet / dialog preserving context).
     - Missing ARB localization (English + Khmer `km`) and Khmer font handling.
     - Missing local caching (Drift / SQLite or shared preferences) for offline public content and preferences.
     - Code formatting issues: compressed 1-line classes in several backend files violating code quality standards.

---

## 2. Backend Inspection

### 2.1 Controllers & Endpoints
| Controller | Current Endpoints | Status | Issues / Deficiencies |
|---|---|---|---|
| `AuthController` | `/register`, `/login`, `/refresh`, `/logout` | Working | Missing password reset/recovery flow (`/forgot-password`, `/reset-password`). |
| `AccountController` | `/me` (GET, PUT) | Working | Compressed single-line formatting; lacks profile image upload/delete. |
| `HealthMeasurementController` | `/measurements` (POST, GET, PUT, DELETE, GET /{id}) | Working | Lacks latest measurement summary per type for dashboard. |
| `NutritionController` | `/foods` (POST, GET), `/meals` (POST, GET, PUT, DELETE), `/summary` (GET) | Incomplete | Lacks `GET /foods/{id}`; lacks category filtering; `/foods` endpoints require auth (breaking guest mode). |
| `DashboardController` | *None* | Missing | Required for personalized home dashboard and health summary. |
| `FoodAnalysisController` | *None* | Missing | Required for AI image upload and nutrition analysis. |
| `MedicationController` | *None* | Missing | Required for medication schedules and logs. |
| `AppointmentController` | *None* | Missing | Required for upcoming and past appointments. |
| `SymptomController` | *None* | Missing | Required for symptom logging and severity history. |

### 2.2 Security & Authorization
- **Security Filter Chain:** Correctly stateless with `JwtAuthenticationFilter`, `RestAuthenticationEntryPoint`, and `RestAccessDeniedHandler`.
- **Vulnerability / Defect:** `authorizeHttpRequests` restricts all requests except `/api/v1/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, and `/actuator/health`. Public endpoints like `GET /api/v1/nutrition/foods/**` and educational content must be made public (`permitAll`).
- **Resource Ownership:** `ResourceOwnershipService` ensures users only access their own measurements. This ownership pattern must be extended to Meals, Medications, Appointments, and Symptoms.
- **Passwords & Tokens:** Passwords hashed with BCrypt. Refresh tokens hashed with SHA-256 and rotated on use.

### 2.3 Database & Migrations
- Current Flyway migrations:
  - `V1__create_identity_tables.sql`: `users`, `accounts`, `refresh_tokens`.
  - `V2__create_health_measurements.sql`: `health_measurements`.
  - `V3__create_nutrition_tables.sql`: `food_categories`, `foods`, `meals`.
- Missing tables requiring migrations:
  - `medications`, `medication_schedules`, `medication_logs`.
  - `appointments`.
  - `symptom_records`.
  - `food_analyses`, `food_analysis_items`.
  - Cambodian food catalog seed data (`V7` or `V4` onward) to give guest and registered users immediate realistic food choices (e.g., Bai Sach Chrouk, Amok, Kuy Teav, Somlor Korko).

---

## 3. Flutter Inspection

### 3.1 Architecture & Code Organization
- **Current State:** A monolithic `lib/main.dart` (403 lines) with small helper screens `food_search_screen.dart` and `add_measurement_screen.dart`.
- **Architectural Violation:** Violates feature-first separation (`core/`, `features/`, `domain/`, `data/`, `presentation/`).
- **Networking:** Direct Dio calls inside UI widgets and notifier with raw map subscripting (`data['accessToken']`). No typed repositories or error mappers.
- **Storage:** Uses raw `FlutterSecureStorage` in UI code. No abstraction or caching layer.

### 3.2 Guest Mode Experience
- **Current State:** Completely absent.
- If no token is found in storage, router forces redirect to `/login`.
- There is no guest onboarding, no guest dashboard, no public catalog exploration, and no guest-to-user conversion flow.

### 3.3 UI/UX Design System
- **Current State:** Default Flutter Material3 with teal seed color, unstyled generic forms, and placeholder `Unavailable` cards.
- Missing:
  - Calm, health-centric design system (Deep Emerald/Teal `#0A6C60`, Soft Mint `#80CBC4`, Off-white light background `#F8FAF9`, Deep charcoal dark mode `#121B19`).
  - Reusable components: Metric cards, Nutrition breakdown bars, Food search list tiles, Medication cards, Appointment cards, Progress indicators, Empty states, Loading skeletons.
  - Interactive charts (`fl_chart`) for weight progress, blood pressure trends, and macro ratios.

### 3.4 Localization
- `pubspec.yaml` lacked `flutter_localizations` and ARB localization setup.
- App only displays hardcoded English strings. Must support Khmer (`km`) and English (`en`) with full coverage for all titles, buttons, dialogs, validation, and error messages.

---

## 4. Feature Implementation Matrix

| Feature | Backend Status | Flutter Status | Database Status | Integration Status | Required Work |
|---|---|---|---|---|---|
| **Guest Mode & Exploration** | Broken (auth required on foods) | Missing (forced login redirect) | Not applicable | Broken | Permit public food endpoints; implement Flutter guest state machine, guest dashboard, and conversion bottom sheet. |
| **Authentication (JWT)** | Complete (verified) | Partial (in main.dart) | Complete (V1) | Working | Re-architect Flutter auth feature, add token refresh interceptor, implement clean Login and Register screens. |
| **User Profile Management** | Complete (`/accounts/me`) | Partial (read-only in main.dart) | Complete (V1) | Incomplete | Build profile editing UI, metric preferences, language toggle. |
| **Health Measurements** | Complete (CRUD + validation) | Minimal (add & list only) | Complete (V2) | Working | Build measurement history, detail view, metric-specific cards, charts, and latest stats. |
| **Food Catalog & Search** | Partial (missing getById, category) | Minimal (basic text filter) | Complete (V3) | Working | Add `GET /foods/{id}`, category filters, seed Cambodian foods, enrich Flutter food search & detail cards. |
| **Nutrition Diary (Meals)** | Complete (CRUD + daily summary) | Minimal (basic list only) | Complete (V3) | Incomplete | Build Add Meal flow with portion calculator, meal type selector, and daily nutrition progress bars. |
| **Dashboard (Personalized)** | Missing | Missing (placeholder card) | Missing aggregates | Missing | Implement backend `/api/v1/dashboard/summary` endpoint; build rich Flutter dashboard with quick actions and metrics. |
| **AI Food Image Analysis** | Missing | Missing (placeholder card) | Missing tables | Missing | Add Flyway migration, Cloudinary/OpenAI service, controller, and Flutter camera/gallery scanner UI with confirmation. |
| **Medication Management** | Missing | Missing | Missing tables | Missing | Add Flyway migration, entity, repo, service, controller, and Flutter medication schedule and log UI. |
| **Appointment Management** | Missing | Missing | Missing tables | Missing | Add Flyway migration, entity, repo, service, controller, and Flutter appointment list and scheduling UI. |
| **Symptom Tracking** | Missing | Missing | Missing tables | Missing | Add Flyway migration, entity, repo, service, controller, and Flutter symptom diary with severity rating. |
| **Localization (Khmer/English)** | Not applicable | Missing | Not applicable | Missing | Add ARB localization, `flutter_localizations`, language provider, and switcher in Settings. |
| **Offline Caching & State** | Not applicable | Missing | Local SQLite/Drift | Missing | Implement local cache for public foods and guest preferences. |
| **Testing & CI Validation** | Complete unit tests (20) | Minimal widget test (1) | Verified | Working | Add backend integration tests for new modules, Flutter widget and provider tests. |

---

## 5. Strategic Execution Roadmap

1. **Phase 2 — Backend Configuration, Database, Authentication & Security:**
   - Add Flyway migrations for Medications, Appointments, Symptoms, Food Analysis, and Cambodian seed foods.
   - Configure public access in `SecurityConfig` for `GET /api/v1/nutrition/foods/**`, public educational endpoints, and actuator health.
   - Fix code formatting across existing backend classes.
2. **Phase 3 — Backend Core Modules and REST APIs:**
   - Implement `DashboardController` & `DashboardService`.
   - Implement `MedicationController`, `AppointmentController`, and `SymptomController`.
   - Implement `FoodAnalysisService` (Cloudinary + OpenAI estimation) and `FoodAnalysisController`.
   - Add unit and service tests for all new modules.
3. **Phase 4 & 5 — Flutter Foundation, Guest Mode, Routing, Authentication & API Client:**
   - Establish feature-first directory layout.
   - Implement robust authentication and guest state management (`initializing`, `guest`, `authenticated`, `unauthenticated`).
   - Implement GoRouter with guest routing and conversion bottom-sheet guards.
   - Set up Dio client with automatic JWT token attachment and refresh interceptor.
4. **Phase 6 — Health Tracking and Nutrition:**
   - Build health tracking screens (add, list, details, trends).
   - Build food catalog browsing (publicly accessible to guests), food details, and meal diary logging.
5. **Phase 7 — AI Food Analysis:**
   - Build scanner screen with image selection, backend upload, candidate preview, nutrition estimation disclaimer, and confirmation into meal diary.
6. **Phase 8 — Medication, Appointments, and Symptoms:**
   - Build medication schedules & adherence logging.
   - Build appointment management and reminder cards.
   - Build symptom logging with severity scale.
7. **Phase 9 — Dashboard, Localization & UI Redesign:**
   - Create guest dashboard (welcoming, educational, food discovery, feature previews).
   - Create authenticated dashboard (greeting, calorie rings, macro bars, upcoming meds/appointments, latest vitals).
   - Implement full English and Khmer ARB localization.
   - Polish Material 3 emerald/mint theme.
8. **Phase 10 — Verification & Documentation:**
   - Run backend `./mvnw clean test`.
   - Run `flutter analyze`, `flutter test`, and `flutter build apk --debug`.
   - Complete all required documentation in `docs/`.
