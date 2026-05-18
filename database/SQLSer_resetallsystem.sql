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