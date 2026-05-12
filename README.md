# Warehouse Management System

A Java desktop application with JDBC for warehouse inventory management. Supports PostgreSQL and MySQL databases.

## 📋 Requirements

- Java 11 or higher
- Eclipse IDE (recommended) or other Java IDE
- PostgreSQL or MySQL database
- Git

## 🗄️ Database Setup

1. Create database in your preferred DBMS (PostgreSQL or MySQL)
2. Run the SQL scripts in database/ folder:
   - create_tables.sql - creates tables, constraints, and keys
   - insert_data.sql - inserts test data (minimum 5 records per table)

## ⚙️ Configuration

1. Open resources/db.properties (or your config file location)
2. Update the following settings for your environment:

For PostgreSQL: