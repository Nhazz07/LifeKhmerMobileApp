CREATE TABLE users (
    id UUID PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(30),
    height_cm NUMERIC(5, 2),
    weight_kg NUMERIC(5, 2),
    preferred_language VARCHAR(10) NOT NULL DEFAULT 'km',
    profile_image_key VARCHAR(512),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    provider VARCHAR(20) NOT NULL,
    provider_subject VARCHAR(320) NOT NULL,
    password_hash VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_account_provider_subject UNIQUE (provider, provider_subject),
    CONSTRAINT ck_local_account_password CHECK (provider <> 'LOCAL' OR password_hash IS NOT NULL)
);
CREATE INDEX idx_accounts_user_id ON accounts(user_id);

CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    replaced_by_token_id UUID REFERENCES refresh_tokens(id),
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires_at ON refresh_tokens(expires_at);
