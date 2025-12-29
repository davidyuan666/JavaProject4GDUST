# Book Rating System Database Initialization Script
# Function: Create database, tables, and insert sample data

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Book Rating System - Database Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Configuration
$MySQLPath = ""
$MySQLHost = "localhost"
$MySQLPort = "3306"
$MySQLUser = "root"
$MySQLPassword = ""
$DatabaseName = "book_rating_db"
$SqlScriptFile = "database/book_rating.sql"

# Function to show database information
function Show-DatabaseInfo {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Database Information" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    
    # Check if database exists
    $checkDbSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$DatabaseName';"
    $dbExists = & $MySQLPath @mysqlArgs -e $checkDbSQL 2>$null
    
    if (-not $dbExists) {
        Write-Host "Database '$DatabaseName' does not exist" -ForegroundColor Red
        return
    }
    
    Write-Host "Database: $DatabaseName" -ForegroundColor Green
    Write-Host "Host: $MySQLHost`:$MySQLPort" -ForegroundColor Gray
    Write-Host "User: $MySQLUser" -ForegroundColor Gray
    Write-Host ""
    
    # Show tables
    Write-Host "=== Tables in Database ===" -ForegroundColor Yellow
    $showTablesSQL = "USE $DatabaseName; SHOW TABLES;"
    & $MySQLPath @mysqlArgs -e $showTablesSQL
    
    Write-Host ""
    
    # Show table details
    $tableDetailsSQL = @"
USE $DatabaseName;
SELECT 
    TABLE_NAME as 'Table',
    TABLE_ROWS as 'Rows',
    DATA_LENGTH as 'Data Size (bytes)',
    INDEX_LENGTH as 'Index Size (bytes)',
    CREATE_TIME as 'Created',
    UPDATE_TIME as 'Last Updated'
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = '$DatabaseName'
ORDER BY TABLE_NAME;
"@
    
    Write-Host "=== Table Details ===" -ForegroundColor Yellow
    & $MySQLPath @mysqlArgs -e $tableDetailsSQL
    
    Write-Host ""
    
    # Show record counts
    $recordCountSQL = @"
USE $DatabaseName;
SELECT 'Users' as 'Table', COUNT(*) as 'Records' FROM users
UNION ALL
SELECT 'Books', COUNT(*) FROM books
UNION ALL
SELECT 'Ratings', COUNT(*) FROM ratings;
"@
    
    Write-Host "=== Record Counts ===" -ForegroundColor Yellow
    & $MySQLPath @mysqlArgs -e $recordCountSQL
    
    Write-Host ""
    
    # Show sample data
    Write-Host "=== Sample Data ===" -ForegroundColor Yellow
    
    # Users sample
    $usersSampleSQL = @"
USE $DatabaseName;
SELECT 
    id as 'ID',
    username as 'Username',
    email as 'Email',
    role as 'Role',
    created_at as 'Created'
FROM users 
ORDER BY id 
LIMIT 5;
"@
    
    Write-Host "Users (first 5):" -ForegroundColor Gray
    & $MySQLPath @mysqlArgs -e $usersSampleSQL
    
    Write-Host ""
    
    # Books sample
    $booksSampleSQL = @"
USE $DatabaseName;
SELECT 
    id as 'ID',
    title as 'Title',
    author as 'Author',
    category as 'Category',
    average_rating as 'Avg Rating'
FROM books 
ORDER BY average_rating DESC 
LIMIT 5;
"@
    
    Write-Host "Top Rated Books:" -ForegroundColor Gray
    & $MySQLPath @mysqlArgs -e $booksSampleSQL
    
    Write-Host ""
    
    # Ratings sample
    $ratingsSampleSQL = @"
USE $DatabaseName;
SELECT 
    r.id as 'Rating ID',
    u.username as 'User',
    b.title as 'Book',
    r.rating as 'Rating',
    LEFT(r.comment, 50) as 'Comment (first 50 chars)',
    r.created_at as 'Rated At'
FROM ratings r
JOIN users u ON r.user_id = u.id
JOIN books b ON r.book_id = b.id
ORDER BY r.created_at DESC
LIMIT 5;
"@
    
    Write-Host "Recent Ratings:" -ForegroundColor Gray
    & $MySQLPath @mysqlArgs -e $ratingsSampleSQL
    
    Write-Host ""
    
    # Database size
    $dbSizeSQL = @"
SELECT 
    '$DatabaseName' as 'Database',
    ROUND(SUM(DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024, 2) as 'Size (MB)'
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_SCHEMA = '$DatabaseName';
"@
    
    Write-Host "=== Database Size ===" -ForegroundColor Yellow
    & $MySQLPath @mysqlArgs -e $dbSizeSQL
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
}

# Auto-find MySQL path
Write-Host "Looking for MySQL..." -ForegroundColor Yellow

$possiblePaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysql.exe",
    "C:\Program Files\MySQL\MySQL Server 5.6\bin\mysql.exe",
    "C:\xampp\mysql\bin\mysql.exe",
    "C:\wamp64\bin\mysql\mysql8.0.31\bin\mysql.exe",
    "C:\wamp\bin\mysql\mysql5.7.36\bin\mysql.exe"
)

foreach ($path in $possiblePaths) {
    if (Test-Path $path) {
        $MySQLPath = $path
        Write-Host "Found MySQL: $path" -ForegroundColor Green
        break
    }
}

if (-not $MySQLPath) {
    Write-Host "ERROR: MySQL not found" -ForegroundColor Red
    Write-Host "Please ensure MySQL is installed and added to PATH" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Or specify MySQL path manually:" -ForegroundColor Yellow
    $MySQLPath = Read-Host "Enter full path to mysql.exe"
    
    if (-not (Test-Path $MySQLPath)) {
        Write-Host "ERROR: Invalid path" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Check SQL script file
if (-not (Test-Path $SqlScriptFile)) {
    Write-Host "ERROR: SQL script file not found: $SqlScriptFile" -ForegroundColor Red
    Write-Host "Please ensure the script file exists" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Get MySQL password
if (-not $MySQLPassword) {
    $securePassword = Read-Host "Enter MySQL root password" -AsSecureString
    $MySQLPassword = [Runtime.InteropServices.Marshal]::PtrToStringAuto(
        [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    )
}

Write-Host ""
Write-Host "Select operation:" -ForegroundColor Yellow
Write-Host "1. Initialize database (create database and tables)"
Write-Host "2. Reset database (drop and recreate)"
Write-Host "3. Execute SQL script only"
Write-Host "4. View database information"
Write-Host "5. Exit"
Write-Host ""

$choice = Read-Host "Enter choice (1-5)"
Write-Host ""

# Build MySQL connection arguments
$mysqlArgs = @(
    "-h", $MySQLHost,
    "-P", $MySQLPort,
    "-u", $MySQLUser,
    "-p$MySQLPassword",
    "--default-character-set=utf8mb4"
)

switch ($choice) {
    "1" {
        # Initialize database
        Write-Host "Initializing database..." -ForegroundColor Yellow
        
        # Create database if not exists
        $createDbSQL = "CREATE DATABASE IF NOT EXISTS $DatabaseName CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        
        Write-Host "Creating database..." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs -e $createDbSQL
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to create database" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "Database created successfully" -ForegroundColor Green
        
        # Execute SQL script
        Write-Host "Executing SQL script..." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs $DatabaseName -e "source $SqlScriptFile"
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to execute SQL script" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "Database initialization completed" -ForegroundColor Green
        
        # Show database info after initialization
        Show-DatabaseInfo
    }
    
    "2" {
        # Reset database
        Write-Host "Resetting database..." -ForegroundColor Yellow
        Write-Host "WARNING: This will drop existing database and recreate!" -ForegroundColor Red
        
        $confirm = Read-Host "Confirm reset database? (type 'yes' to continue)"
        if ($confirm -ne "yes") {
            Write-Host "Operation cancelled" -ForegroundColor Yellow
            Read-Host "Press Enter to exit"
            exit 0
        }
        
        # Drop database if exists
        Write-Host "Dropping existing database..." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs -e "DROP DATABASE IF EXISTS $DatabaseName;"
        
        # Create new database
        Write-Host "Creating new database..." -ForegroundColor Gray
        $createDbSQL = "CREATE DATABASE $DatabaseName CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
        & $MySQLPath @mysqlArgs -e $createDbSQL
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to create database" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        # Execute SQL script
        Write-Host "Executing SQL script..." -ForegroundColor Gray
        & $MySQLPath @mysqlArgs $DatabaseName -e "source $SqlScriptFile"
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to execute SQL script" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "Database reset completed" -ForegroundColor Green
        
        # Show database info after reset
        Show-DatabaseInfo
    }
    
    "3" {
        # Execute SQL script only
        Write-Host "Executing SQL script..." -ForegroundColor Yellow
        
        # Check if database exists
        Write-Host "Checking database..." -ForegroundColor Gray
        $checkDbSQL = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '$DatabaseName';"
        $dbExists = & $MySQLPath @mysqlArgs -e $checkDbSQL 2>$null
        
        if (-not $dbExists) {
            Write-Host "ERROR: Database $DatabaseName does not exist" -ForegroundColor Red
            Write-Host "Please create database first or choose initialization option" -ForegroundColor Yellow
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        # Execute SQL script
        & $MySQLPath @mysqlArgs $DatabaseName -e "source $SqlScriptFile"
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Failed to execute SQL script" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "SQL script executed successfully" -ForegroundColor Green
        
        # Show database info after execution
        Show-DatabaseInfo
    }
    
    "4" {
        # View database information
        Show-DatabaseInfo
    }
    
    "5" {
        Write-Host "Exiting" -ForegroundColor Cyan
        exit 0
    }
    
    default {
        Write-Host "Invalid choice" -ForegroundColor Red
        Read-Host "Press Enter to exit"
        exit 1
    }
}

# Only show next steps if we performed an initialization operation
if ($choice -in @("1", "2", "3")) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "Next steps:" -ForegroundColor Yellow
    Write-Host "1. Update Java database connection:" -ForegroundColor Gray
    Write-Host "   File: src/main/java/com/bookrating/dao/DatabaseConnection.java" -ForegroundColor Gray
    Write-Host "   Update URL: jdbc:mysql://$($MySQLHost):$($MySQLPort)/$($DatabaseName)" -ForegroundColor Gray
    Write-Host "   Update username and password" -ForegroundColor Gray
    Write-Host ""
    Write-Host "2. Run the Book Rating System:" -ForegroundColor Gray
    Write-Host "   .\run.ps1" -ForegroundColor Gray
    Write-Host "========================================" -ForegroundColor Cyan
}

Read-Host "Press Enter to exit"