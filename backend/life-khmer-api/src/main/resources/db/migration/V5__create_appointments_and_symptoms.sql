CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    doctor_name VARCHAR(150) NOT NULL,
    healthcare_facility VARCHAR(200) NOT NULL,
    appointment_time TIMESTAMP NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    notes VARCHAR(1000),
    reminder_minutes_before INT NOT NULL DEFAULT 60,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_appointments_user_time ON appointments(user_id, appointment_time);

CREATE TABLE symptom_records (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    symptom_name VARCHAR(150) NOT NULL,
    severity INT NOT NULL CHECK (severity BETWEEN 1 AND 10),
    notes VARCHAR(1000),
    recorded_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_symptom_records_user_recorded ON symptom_records(user_id, recorded_at DESC);
