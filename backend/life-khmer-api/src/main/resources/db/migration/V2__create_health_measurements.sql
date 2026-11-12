CREATE TABLE health_measurements (
    id UUID PRIMARY KEY, user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    measurement_type VARCHAR(30) NOT NULL, value NUMERIC(10,2) NOT NULL,
    secondary_value NUMERIC(10,2), unit VARCHAR(20) NOT NULL, recorded_at TIMESTAMP NOT NULL,
    notes VARCHAR(1000), created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_health_measurements_user_recorded ON health_measurements(user_id, recorded_at DESC);
