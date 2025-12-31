Personal Expense Tracker 💰
An Android app to track daily expenses with local PHP/MySQL backend.

🚀 Quick Setup
1. Install Requirements
Android Studio (latest version)

XAMPP (for local server)

Java JDK 11+

2. Database Setup
Start XAMPP (Apache + MySQL)

Open phpMyAdmin: http://localhost/phpmyadmin

Create database: expense_tracker

Run these SQL commands:

sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category VARCHAR(100) NOT NULL,
    date DATETIME NOT NULL
);
3. PHP API Setup
Go to: C:\xampp\htdocs\

Create folder: expense_api

Add these 3 files inside:

config.php

auth.php

expenses.php

config.php:

php
<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST, GET, PUT, DELETE");

$db_host = 'localhost';
$db_user = 'root';
$db_pass = '';
$db_name = 'expense_tracker';

try {
    $conn = new PDO("mysql:host=$db_host;dbname=$db_name", $db_user, $db_pass);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    echo json_encode(["success" => false, "message" => "Database connection failed"]);
    exit();
}
?>
auth.php:

php
<?php
require_once 'config.php';

$data = json_decode(file_get_contents("php://input"));
$response = ["success" => false, "message" => "Invalid request"];

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if ($data->action === 'register') {
        $hashedPassword = password_hash($data->password, PASSWORD_BCRYPT);
        $stmt = $conn->prepare("INSERT INTO users (email, password) VALUES (?, ?)");
        
        if ($stmt->execute([$data->email, $hashedPassword])) {
            $response = ["success" => true, "message" => "Registered", "user_id" => $conn->lastInsertId()];
        }
    }
    elseif ($data->action === 'login') {
        $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
        $stmt->execute([$data->email]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);
        
        if ($user && password_verify($data->password, $user['password'])) {
            $response = ["success" => true, "message" => "Login successful", "user_id" => $user['id']];
        }
    }
}

echo json_encode($response);
?>
4. Android App Setup
Open project in Android Studio

Update app/build.gradle:

gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Room Database
    implementation "androidx.room:room-runtime:2.5.2"
    annotationProcessor "androidx.room:room-compiler:2.5.2"
    
    // Retrofit for API calls
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
}
Update API URL in ApiClient.java:

java
private static final String BASE_URL = "http://10.0.2.2/expense_api/"; // For emulator
5. Run the App
Start XAMPP services

Build and run in Android Studio

Use emulator or real device

📱 App Features
✅ User registration/login

✅ Add/View expenses

✅ Expense categories

✅ Date tracking

✅ Local database

🔧 Troubleshooting
White Screen on Launch
Check if XAMPP is running

Verify PHP files are in correct location

Test API in browser: http://localhost/expense_api/auth.php

Database Connection Error
Check XAMPP MySQL is running

Verify database credentials

Test connection in phpMyAdmin

App Not Connecting to Server
Emulator: Use http://10.0.2.2/
Real Device: Use your computer's IP address

📂 File Structure
text
PersonalExpenseTracker/
├── app/src/main/java/
│   ├── activities/     # Screens (Login, Main, Register)
│   ├── api/           # API calls
│   ├── auth/          # Authentication
│   └── models/        # Data models
├── htdocs/expense_api/
│   ├── config.php
│   ├── auth.php
│   └── expenses.php
