-- =====================================================
-- SCRIPT RESET HOÀN TOÀN DATABASE
-- Chạy trong DBeaver với PostgreSQL
-- =====================================================

-- ✅ Bước 1: Xóa toàn bộ tables (CASCADE để xóa khóa ngoại)
DROP TABLE IF EXISTS project_invoices CASCADE;
DROP TABLE IF EXISTS project_order_tracking CASCADE;
DROP TABLE IF EXISTS project_orders CASCADE;
DROP TABLE IF EXISTS project_customers CASCADE;
DROP TABLE IF EXISTS project_products CASCADE;
DROP TABLE IF EXISTS project_users CASCADE;

-- ✅ Bước 2: Tạo lại tables
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
    unit VARCHAR(20) DEFAULT 'cái',
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
('admin', 'admin123', 'ADMIN'),
('manager1', '123456', 'ORDER_MANAGER'),
('manager2', '123456', 'ORDER_MANAGER');

-- Customers
INSERT INTO project_customers(full_name, phone, email, address) VALUES 
('Nguyễn Văn A', '0901111111', 'a@mail.com', 'Hà Nội'),
('Trần Thị B', '0902222222', 'b@mail.com', 'TP.HCM'),
('Lê Văn C', '0903333333', 'c@mail.com', 'Đà Nẵng'),
('Phạm Thị D', '0904444444', 'd@mail.com', 'Hải Phòng'),
('Hoàng Văn E', '0905555555', 'e@mail.com', 'Cần Thơ');

-- Products
INSERT INTO project_products(name, unit, price, stock_qty) VALUES 
('Laptop Dell Inspiron 15', 'chiếc', 15000000, 10),
('Chuột Logitech M330', 'cái', 250000, 50),
('Bàn phím cơ Corsair K70', 'cái', 800000, 30),
('Màn hình LG 24 inch', 'chiếc', 4500000, 15),
('Ổ cứng SSD Samsung 500GB', 'cái', 1200000, 40),
('RAM DDR4 8GB', 'thanh', 650000, 60),
('Tai nghe Sony WH-1000XM4', 'cái', 3500000, 20);

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