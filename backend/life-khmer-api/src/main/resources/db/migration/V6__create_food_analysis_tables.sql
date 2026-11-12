CREATE TABLE food_analyses (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    image_url VARCHAR(1024),
    detected_food_name VARCHAR(150) NOT NULL,
    confidence NUMERIC(5,2),
    status VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    estimated_calories NUMERIC(10,2) NOT NULL,
    estimated_protein NUMERIC(10,2) NOT NULL,
    estimated_carbohydrates NUMERIC(10,2) NOT NULL,
    estimated_fat NUMERIC(10,2) NOT NULL,
    suggested_portion_grams NUMERIC(10,2) NOT NULL DEFAULT 200,
    raw_ai_response TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_food_analyses_user_created ON food_analyses(user_id, created_at DESC);
