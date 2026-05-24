-- 1. Tạo login mới (nếu chưa tồn tại)
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = N'app_user')
BEGIN
    CREATE LOGIN app_user WITH PASSWORD = 'JavaApp@2026', CHECK_POLICY = OFF;
    PRINT 'Created login: app_user';
END
ELSE
    PRINT 'Login already exists: app_user';

-- 2. Tạo database (nếu chưa tồn tại)
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'projectfinal')
BEGIN
    CREATE DATABASE projectfinal;
    PRINT 'Created database: projectfinal';
END
ELSE
    PRINT 'Database already exists: projectfinal';

-- 3. Chuyển sang database projectfinal
USE projectfinal;
GO

-- 4. Tạo user trong database cho login app_user
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = N'app_user')
BEGIN
    CREATE USER app_user FOR LOGIN app_user;
    PRINT 'Created user: app_user in projectfinal';
END
ELSE
    PRINT 'User already exists: app_user';

-- 5. Cấp quyền db_owner cho user
ALTER ROLE db_owner ADD MEMBER app_user;
PRINT 'Granted db_owner role to app_user';

-- 6. Kiểm tra lại
SELECT 
    sp.name AS LoginName,
    dp.name AS UserName,
    dp.type_desc AS UserType
FROM sys.server_principals sp
LEFT JOIN sys.database_principals dp ON sp.sid = dp.sid
WHERE sp.name = 'app_user';

-- =====================================================
-- SCRIPT RESET HOÀN TOÀN DATABASE - SQL SERVER
-- Chạy trong SQL Server Management Studio (SSMS)
-- =====================================================

-- ✅ Bước 1: Xóa toàn bộ tables (xóa theo thứ tự ngược lại để tránh lỗi FK)
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'project_invoices') DROP TABLE project_invoices;
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'project_order_tracking') DROP TABLE project_order_tracking;
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'project_orders') DROP TABLE project_orders;
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'project_customers') DROP TABLE project_customers;
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'project_products') DROP TABLE project_products;
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'project_users') DROP TABLE project_users;

-- ✅ Bước 2: Tạo lại tables

CREATE TABLE project_users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'ORDER_MANAGER'))
);

CREATE TABLE project_customers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    address VARCHAR(MAX)
);

CREATE TABLE project_products (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    unit VARCHAR(20) DEFAULT 'pcs',
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    stock_qty INT DEFAULT 0 CHECK (stock_qty >= 0)
);

CREATE TABLE project_orders (
    id INT IDENTITY(1,1) PRIMARY KEY,
    customer_id INT,
    order_date DATE DEFAULT CAST(GETDATE() AS DATE),
    created_by INT,
    total_amount DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES project_customers(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES project_users(id)
);

CREATE TABLE project_order_tracking (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL CHECK (quantity > 0),
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING','PROCESSING','SHIPPED','COMPLETED','CANCELLED')),
    FOREIGN KEY (order_id) REFERENCES project_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES project_products(id) ON DELETE SET NULL
);

CREATE TABLE project_invoices (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT UNIQUE,
    issue_date DATE DEFAULT CAST(GETDATE() AS DATE),
    issued_by INT,
    payment_status VARCHAR(20) DEFAULT 'UNPAID' CHECK (payment_status IN ('UNPAID','PAID')),
    FOREIGN KEY (order_id) REFERENCES project_orders(id) ON DELETE CASCADE,
    FOREIGN KEY (issued_by) REFERENCES project_users(id)
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