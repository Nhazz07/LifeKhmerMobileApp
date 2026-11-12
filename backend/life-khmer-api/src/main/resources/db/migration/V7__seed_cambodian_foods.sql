-- Seed initial food categories
INSERT INTO food_categories (id, name, created_at)
VALUES 
  ('a1111111-1111-1111-1111-111111111101', 'Traditional Khmer Dishes', CURRENT_TIMESTAMP),
  ('a1111111-1111-1111-1111-111111111102', 'Soups & Noodles', CURRENT_TIMESTAMP),
  ('a1111111-1111-1111-1111-111111111103', 'Rice & Stir-fries', CURRENT_TIMESTAMP),
  ('a1111111-1111-1111-1111-111111111104', 'Fruits & Desserts', CURRENT_TIMESTAMP)
ON CONFLICT (name) DO NOTHING;

-- Seed authentic Cambodian foods (per 100g)
INSERT INTO foods (
    id, category_id, name, normalized_name, serving_unit,
    calories_per_100g, protein_per_100g, carbohydrates_per_100g, fat_per_100g, sugar_per_100g, fiber_per_100g,
    created_at, updated_at
)
VALUES
(
    'b1111111-1111-1111-1111-111111111101',
    'a1111111-1111-1111-1111-111111111103',
    'Bai Sach Chrouk (Pork with Broken Rice)',
    'bai sach chrouk pork with broken rice',
    'g',
    185.00, 9.50, 24.00, 5.80, 2.10, 0.80,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111102',
    'a1111111-1111-1111-1111-111111111101',
    'Fish Amok (Amok Trey)',
    'fish amok amok trey',
    'g',
    145.00, 12.00, 4.50, 9.00, 1.50, 1.20,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111103',
    'a1111111-1111-1111-1111-111111111102',
    'Kuy Teav Phnom Penh (Rice Noodle Soup)',
    'kuy teav phnom penh rice noodle soup',
    'g',
    95.00, 5.20, 14.50, 2.00, 1.00, 0.60,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111104',
    'a1111111-1111-1111-1111-111111111102',
    'Somlor Korko (Khmer Vegetable Stew)',
    'somlor korko khmer vegetable stew',
    'g',
    72.00, 4.80, 7.50, 2.50, 1.80, 2.80,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111105',
    'a1111111-1111-1111-1111-111111111103',
    'Beef Lok Lak',
    'beef lok lak',
    'g',
    168.00, 16.50, 6.20, 8.80, 3.00, 0.90,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111106',
    'a1111111-1111-1111-1111-111111111102',
    'Num Banh Chok (Khmer Noodles with Curry)',
    'num banh chok khmer noodles with curry',
    'g',
    110.00, 4.50, 18.00, 2.20, 1.20, 1.50,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111107',
    'a1111111-1111-1111-1111-111111111104',
    'Fresh Khmer Mango (Svaay)',
    'fresh khmer mango svaay',
    'g',
    60.00, 0.80, 15.00, 0.40, 13.70, 1.60,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
),
(
    'b1111111-1111-1111-1111-111111111108',
    'a1111111-1111-1111-1111-111111111101',
    'Banana Flower Salad (Gnoam Chrouk)',
    'banana flower salad gnoam chrouk',
    'g',
    85.00, 4.20, 8.50, 3.80, 3.50, 3.00,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
)
ON CONFLICT (normalized_name) DO NOTHING;
