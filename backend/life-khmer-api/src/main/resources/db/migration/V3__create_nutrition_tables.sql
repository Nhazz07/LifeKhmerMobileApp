CREATE TABLE food_categories (id UUID PRIMARY KEY, name VARCHAR(100) NOT NULL UNIQUE, created_at TIMESTAMP NOT NULL);
CREATE TABLE foods (
    id UUID PRIMARY KEY, category_id UUID REFERENCES food_categories(id) ON DELETE SET NULL,
    name VARCHAR(150) NOT NULL, normalized_name VARCHAR(150) NOT NULL UNIQUE, serving_unit VARCHAR(20) NOT NULL DEFAULT 'g',
    calories_per_100g NUMERIC(10,2) NOT NULL, protein_per_100g NUMERIC(10,2) NOT NULL,
    carbohydrates_per_100g NUMERIC(10,2) NOT NULL, fat_per_100g NUMERIC(10,2) NOT NULL,
    sugar_per_100g NUMERIC(10,2) NOT NULL, fiber_per_100g NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_foods_normalized_name ON foods(normalized_name);
CREATE TABLE meals (
    id UUID PRIMARY KEY, user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    food_id UUID REFERENCES foods(id) ON DELETE SET NULL, meal_type VARCHAR(20) NOT NULL, food_name VARCHAR(150) NOT NULL,
    portion_grams NUMERIC(10,2) NOT NULL, calories NUMERIC(10,2) NOT NULL, protein NUMERIC(10,2) NOT NULL,
    carbohydrates NUMERIC(10,2) NOT NULL, fat NUMERIC(10,2) NOT NULL, sugar NUMERIC(10,2) NOT NULL, fiber NUMERIC(10,2) NOT NULL,
    consumed_at TIMESTAMP NOT NULL, notes VARCHAR(1000), created_at TIMESTAMP NOT NULL, updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_meals_user_consumed_at ON meals(user_id, consumed_at DESC);
