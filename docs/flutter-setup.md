# Flutter setup

From `mobile/life_khmer`:

```bash
flutter pub get
flutter run --dart-define=API_BASE_URL=http://10.0.2.2:8090/api/v1/
```

Start the Spring Boot API on port 8090 and configure its database and JWT environment first. A physical device must use a reachable HTTPS/LAN API URL; never use `localhost`.

Firebase configuration is intentionally not enabled until the backend supports Firebase token verification.
