# Warehouse Management System

A Java desktop application with JDBC for warehouse inventory management. Supports PostgreSQL and MySQL databases.

📋Requirements

* Java 11 or higher
* Eclipse IDE (recommended) or other Java IDE
* PostgresSQL or SQLServer database
* Git
* JDBC drivers (PostgreSQL and/or MySQL)

## 🗄️Database Setup

### Step 1: Create Database

Choose one of the supported databases and create a new database:
For PostgreSQL/SQLServer:

CREATE DATABASE warehouse\_db;

### Step 2: Run SQL Scripts

Choose the type of database and execute database/Pos\_resetallsystem database/SQLSer\_resetallsystem to create tables, constraints and insert test data

## 🔧 Configuration

### Step 1: Add JDBC Drivers

**In Eclipse:**

Right-click on the project → Properties

Go to **Java Build Path** → **Libraries** tab

Click **Add External JARs...**

Select all JAR files from the lib folder

Click **Apply and Close**

### Step 2: Configure Database Connection

Open src/config.properties and update the settings:

**For PostgreSQL:**

db.url=jdbc:postgresql://localhost:5432/warehouse\_db

db.user=postgres

db.password=your\_password

db.driver=org.postgresql.Driver

**For SQLServer:**

db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver

db.url= jdbc:sqlserver://localhost:1433;databaseName=projectfinal;encrypt=false

db.username=app\_user

db.password=JavaApp@2026

**Important:**

* Change db.user and db.password to match your database credentials
* Update db.url if your database is on a different host or port
* To switch between databases, simply change the db.driver and db.url values

## 🚀 Running the Application

### Using Eclipse:

#### 1\. Import Project:

Open Eclipse

Go to **File** → **Import** → **Select Existing Projects into Workspace**

Click **Next**

Browse to the project folder

Click Finish

#### 2\. Build Path Setup:

Right-click on the project → Properties

Go to **Java Build Path** → **Libraries**

Add JDBC drivers from the lib folder (if not already added)

#### 3\. Run Application:

Navigate to src/application/ folder

Find the main class (usually named Main.java or Application.java)

Right-click on the main class

Select Run As → Java Application

### Using Command Line:

#### Compile the project

javac -cp "lib/*:src" -d bin src/application/*.java src/project1/\*.java

#### Run the application

java -cp "bin:lib/\*" application.Main



Note for Windows users: Use ; instead of : in classpath

javac -cp "lib/*;src" -d bin src/application/*.java src/project1/\*.java

java -cp "bin;lib/\*" application.Main

