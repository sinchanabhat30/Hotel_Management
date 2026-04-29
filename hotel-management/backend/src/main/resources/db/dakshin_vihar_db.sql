-- Dakshin Vihar Hotel Management - Create tables + sample data
-- Database: dakshin_vihar_db

CREATE TABLE IF NOT EXISTS menu_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  image_url TEXT,
  price DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type VARCHAR(100) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  image_url TEXT
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  customer_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  room_type VARCHAR(100) NOT NULL,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS contacts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  message TEXT NOT NULL
);

-- Seed menu items (South Indian dishes)
INSERT INTO menu_items (name, image_url, price) VALUES
('Masala Dosa', 'https://images.unsplash.com/photo-1743517894265-c86ab035adef?auto=format&fit=crop&w=1200&q=80', 129.00),
('Mysore Masala Dosa', 'https://images.unsplash.com/photo-1708146464361-5c5ce4f9abb6?auto=format&fit=crop&w=1200&q=80', 159.00),
('Rava Dosa', 'https://images.unsplash.com/photo-1662174485500-6d32a13c060e?auto=format&fit=crop&w=1200&q=80', 119.00),
('Idli (2 pcs) with Sambar', 'https://images.unsplash.com/photo-1741376509253-221ac18fac0f?auto=format&fit=crop&w=1200&q=80', 89.00),
('Medu Vada', 'https://images.unsplash.com/photo-1756757077703-26dc3ba7e853?auto=format&fit=crop&w=1200&q=80', 99.00),
('Sambar', 'https://www.indianhealthyrecipes.com/wp-content/uploads/2021/05/sambar-recipe.jpg', 59.00),
('Uttapam', 'https://pipingpotcurry.com/wp-content/uploads/2026/01/Uttapam-2.jpg', 109.00),
('Filter Coffee', 'https://images.unsplash.com/photo-1741376509187-0b683c764294?auto=format&fit=crop&w=1200&q=80', 79.00),
('Coconut Chutney', 'https://cookilicious.com/wp-content/uploads/2022/01/Coconut-Chutney-5.jpg', 49.00),
('Ghee Roast', 'https://images.unsplash.com/photo-1743517894265-c86ab035adef?auto=format&fit=crop&w=1200&q=80', 149.00);

-- Seed rooms
INSERT INTO rooms (type, image_url, price) VALUES
('AC Deluxe', 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80', 2499.00),
('Non-AC Deluxe', 'https://images.unsplash.com/photo-1618773928121-c32242e63f39?auto=format&fit=crop&w=1200&q=80', 1899.00),
('Family Suite', 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=1200&q=80', 3499.00),
('Executive Double', 'https://images.unsplash.com/photo-1631049552057-403cdb8f0658?auto=format&fit=crop&w=1200&q=80', 2799.00);

