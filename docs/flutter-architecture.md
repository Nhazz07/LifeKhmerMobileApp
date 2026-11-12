# Flutter architecture

The target structure is feature-first: `core/network` owns Dio and token interception; each implemented feature owns data source, repository, provider, and presentation files. Screens must depend on providers, never Dio directly. API contracts are documented in `mobile/backend-api-integration-map.md`.

Current implementation is transitional and requires this refactor before a beta release. No Firebase-specific repository should be activated until the backend accepts Firebase ID tokens.
