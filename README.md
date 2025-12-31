Personal Expense Tracker 📱💰


A comprehensive Android application for managing personal finances built with Java and Android Studio, featuring a PHP/MySQL backend using XAMPP.

✨ Features
📋 Core Features
User Authentication - Secure registration and login system

Expense Management - Add, view, edit, and delete expenses

Expense Categorization - Organize expenses by categories (Food, Transportation, etc.)

Location Tracking - Tag expenses with location using GPS

Analytics Dashboard - Visual spending insights with charts and graphs

Dark/Light Mode - Customizable theme for user preference

🔧 Technical Features
Local Database - MySQL database using XAMPP and phpMyAdmin

RESTful API - PHP backend with JSON API endpoints

Android Native - Built with Java and Android SDK

Material Design - Modern UI following Material Design guidelines

Room Database - Local caching for offline access

🏗️ Architecture
text
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Android App   │────▶│   PHP API       │────▶│   MySQL DB      │
│   (Java)        │     │   (XAMPP)       │     │   (phpMyAdmin)  │
└─────────────────┘     └─────────────────┘     └─────────────────┘

🚀 Getting Started
Prerequisites
Android Studio (Latest Version)

Java JDK 11+

XAMPP (Apache + MySQL)

Android SDK (API 23+)

Android Emulator or Physical Device

Installation
1. Clone the Repository
bash
git clone https://github.com/yourusername/personal-expense-tracker.git
cd personal-expense-tracker
2. Database Setup
Install and start XAMPP

Open phpMyAdmin (http://localhost/phpmyadmin)

Create a new database: expense_tracker

Import the SQL file from database/schema.sql

Update database credentials in php_api/config.php

3. PHP API Setup
Copy the php_api folder to C:\xampp\htdocs\

Configure database connection in php_api/config.php

4. Android App Setup
Open the project in Android Studio

Sync Gradle dependencies

Update API base URL in ApiClient.java:

java
private static final String BASE_URL = "http://10.0.2.2/expense_api/";
Build and run the application

Configuration
Backend Configuration (config.php)
php
$db_host = 'localhost';
$db_user = 'root';      // Default XAMPP username
$db_pass = '';          // Default XAMPP password
$db_name = 'expense_tracker';
Android Configuration
Update server IP for real device testing

Configure location permissions

Set minimum SDK version (API 23)

📁 Project Structure
text
PersonalExpenseTracker/
├── app/
│   ├── src/main/java/com/example/personalexpensetracker/
│   │   ├── activities/          # Android Activities
│   │   ├── fragments/           # Android Fragments
│   │   ├── adapters/            # RecyclerView Adapters
│   │   ├── api/                 # API Client & Services
│   │   ├── auth/                # Authentication Helper
│   │   ├── database/            # Room Database
│   │   ├── models/              # Data Models
│   │   └── viewmodels/          # ViewModels
│   └── src/main/res/
│       ├── layout/              # XML Layouts
│       ├── drawable/            # Icons & Images
│       ├── values/              # Strings, Colors, Styles
│       └── menu/                # Menu Resources
├── php_api/
│   ├── config.php               # Database Configuration
│   ├── auth.php                 # Authentication API
│   └── expenses.php             # Expenses CRUD API
└── database/
    └── schema.sql              # Database Schema
🛠️ Technologies Used
Frontend (Android)
Java - Primary programming language

Android SDK - Native Android development

Material Design - UI components and theming

Room Database - Local data persistence

Retrofit - HTTP client for API calls

MPAndroidChart - Chart and graph library

Google Maps API - Location services

Backend
PHP - Server-side scripting

MySQL - Database management

XAMPP - Local development server

REST API - JSON-based API endpoints

📊 Database Schema
Users Table
sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
Expenses Table
sql
CREATE TABLE expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    date DATETIME NOT NULL,
    location VARCHAR(255),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
🔌 API Endpoints
Authentication
POST /auth.php - User registration/login

Request Body:

json
{
  "action": "register|login",
  "email": "user@example.com",
  "password": "password123"
}
Expenses
GET /expenses.php?user_id={id} - Get all expenses

POST /expenses.php - Add new expense

PUT /expenses.php - Update expense

DELETE /expenses.php - Delete expense

📱 Usage
Adding an Expense
Navigate to "Add Expense" tab

Enter expense details (title, amount, category)

Select date and time

Add location (optional)

Save expense

Viewing Expenses
View all expenses in list format

Filter by category or date

See total spending per category

Analytics
Pie chart showing expense distribution

Monthly spending trends

Category-wise analysis

🔧 Development
Building the Project
bash
# Clean project
./gradlew clean

# Build project
./gradlew build

# Run tests
./gradlew test
Adding New Features
Create feature branch

Implement changes

Update database schema if needed

Test thoroughly

Create pull request

Code Style
Follow Java naming conventions

Use meaningful variable names

Add comments for complex logic

Maintain consistent indentation

🧪 Testing
Unit Tests
Run Android unit tests with ./gradlew test

Test individual components in isolation

API Testing
Use Postman to test API endpoints

Test all CRUD operations

Verify error handling

UI Testing
Test on multiple screen sizes

Verify dark/light mode switching

Test location permissions
