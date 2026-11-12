CREATE TABLE medications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    instructions VARCHAR(500),
    start_date DATE NOT NULL,
    end_date DATE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_medications_user_active ON medications(user_id, active);

CREATE TABLE medication_logs (
    id UUID PRIMARY KEY,
    medication_id UUID NOT NULL REFERENCES medications(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scheduled_time TIMESTAMP NOT NULL,
    taken_at TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'TAKEN',
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_medication_logs_user_scheduled ON medication_logs(user_id, scheduled_time DESC);
