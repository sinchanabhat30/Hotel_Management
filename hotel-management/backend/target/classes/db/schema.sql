-- Dakshin Vihar Hotel Management schema
-- Uses: menu_items, rooms, bookings, contacts

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

