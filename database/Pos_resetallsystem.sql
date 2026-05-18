-- =====================================================
-- SCRIPT RESET HOÀN TOÀN DATABASE
-- Chạy trong DBeaver với PostgreSQL
-- =====================================================

-- Bước 1: Xóa toàn bộ tables (CASCADE để xóa khóa ngoại)
DROP TABLE IF EXISTS project_invoices CASCADE;
DROP TABLE IF EXISTS project_order_tracking CASCADE;
DROP TABLE IF EXISTS project_orders CASCADE;
DROP TABLE IF EXISTS project_customers CASCADE;
DROP TABLE IF EXISTS project_products CASCADE;
DROP TABLE IF EXISTS project_users CASCADE;

-- Bước 2: Tạo lại tables
CREATE TABLE project_users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('ADMIN', 'ORDER_MANAGER')) NOT NULL
);

CREATE TABLE project_customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    address TEXT
);

CREATE TABLE project_products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit VARCHAR(20) DEFAULT 'pcs',
    price DECIMAL(10,2) CHECK (price > 0) NOT NULL,
    stock_qty INTEGER DEFAULT 0 CHECK (stock_qty >= 0)
);

CREATE TABLE project_orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES project_customers(id) ON DELETE SET NULL,
    order_date DATE DEFAULT CURRENT_DATE,
    created_by INTEGER REFERENCES project_users(id),
    total_amount DECIMAL(10,2) DEFAULT 0
);

CREATE TABLE project_order_tracking (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES project_orders(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES project_products(id) ON DELETE SET NULL,
    quantity INTEGER CHECK (quantity > 0) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING','PROCESSING','SHIPPED','COMPLETED','CANCELLED'))
);

CREATE TABLE project_invoices (
    id SERIAL PRIMARY KEY,
    order_id INTEGER UNIQUE REFERENCES project_orders(id) ON DELETE CASCADE,
    issue_date DATE DEFAULT CURRENT_DATE,
    issued_by INTEGER REFERENCES project_users(id),
    payment_status VARCHAR(20) DEFAULT 'UNPAID' CHECK (payment_status IN ('UNPAID','PAID'))
);

-- ✅ Bước 3: Insert dữ liệu mẫu

-- Users
INSERT INTO project_users(username, password, role) VALUES 
('admin', '123', 'ADMIN'),
('manager1', '123456', 'ORDER_MANAGER'),
('manager2', '123456', 'ORDER_MANAGER');

-- Customers
INSERT INTO project_customers(full_name, phone, email, address) VALUES 
('Nesterov Sergey Alexandrovich', '0901111111', 'a@mail.com', 'Saint Petersburg, Russia'),
('Akimov Mir', '0902222222', 'b@mail.com', 'Saint Petersburg'),
('John Smith', '+1-212-555-0101', 'john.smith@email.com', 'New York, USA'),
('Jane Doe', '+44-20-7946-0958', 'jane.doe@email.com', 'London, UK'),
('Michael Brown', '+1-312-555-0198', 'm.brown@email.com', 'Chicago, USA'),
('Emily Davis', '+61-2-9876-5432', 'emily.d@email.com', 'Sydney, Australia'),
('Vu Manh Hung', '+84902242003', 'hehee@gmail.com', 'Hanoi, Vietnam'),
('David Wilson', '+1-416-555-0147', 'd.wilson@email.com', 'Toronto, Canada');

-- Products
INSERT INTO project_products(name, unit, price, stock_qty) VALUES 
('Dell Inspiron 15 Laptop', 'pcs', 15000000, 10),
('Logitech M330 Wireless Mouse', 'pcs', 250000, 50),
('Corsair K70 Mechanical Keyboard', 'pcs', 800000, 30),
('LG 24-inch Monitor', 'pcs', 4500000, 15),
('Samsung 500GB NVMe SSD', 'pcs', 1200000, 40),
('DDR4 8GB RAM Module', 'stick', 650000, 60),
('Sony WH-1000XM4 Headphones', 'pcs', 3500000, 20);

-- Orders (mẫu)
INSERT INTO project_orders(customer_id, created_by, total_amount) VALUES 
(1, 1, 15000000),
(2, 2, 500000),
(3, 1, 800000);

-- Order Tracking
INSERT INTO project_order_tracking(order_id, product_id, quantity, status) VALUES 
(1, 1, 1, 'COMPLETED'),
(2, 2, 2, 'PENDING'),
(3, 3, 1, 'PROCESSING');

-- Invoices
INSERT INTO project_invoices(order_id, issued_by, payment_status) VALUES 
(1, 1, 'PAID'),
(2, 2, 'UNPAID'),
(3, 1, 'UNPAID');

-- ✅ Bước 4: Kiểm tra kết quả
SELECT 'Users' as table_name, COUNT(*) as record_count FROM project_users
UNION ALL
SELECT 'Customers', COUNT(*) FROM project_customers
UNION ALL
SELECT 'Products', COUNT(*) FROM project_products
UNION ALL
SELECT 'Orders', COUNT(*) FROM project_orders
UNION ALL
SELECT 'Order_Tracking', COUNT(*) FROM project_order_tracking
UNION ALL
SELECT 'Invoices', COUNT(*) FROM project_invoices;