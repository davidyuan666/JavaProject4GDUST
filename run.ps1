# Simple Java Project Runner
# 简单的Java项目运行脚本

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Java Project Runner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java environment
Write-Host "Checking Java environment..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Java environment OK" -ForegroundColor Green
        $javaVersion | Select-Object -First 3
    }
    else {
        throw "Java execution failed"
    }
}
catch {
    Write-Host "ERROR: Java not found. Please install JDK 17 or higher" -ForegroundColor Red
    Write-Host "Make sure Java is installed and added to PATH" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""

# Check project structure
Write-Host "Checking project structure..." -ForegroundColor Yellow
$hasJavaFiles = $false

if (Test-Path "src/main/java") {
    $javaFiles = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java" -ErrorAction SilentlyContinue
    if ($javaFiles.Count -gt 0) {
        $hasJavaFiles = $true
        Write-Host "Found Java files in src/main/java" -ForegroundColor Green
    }
}

if (-not $hasJavaFiles -and (Test-Path "src")) {
    $javaFiles = Get-ChildItem -Recurse -Path "src" -Filter "*.java" -ErrorAction SilentlyContinue
    if ($javaFiles.Count -gt 0) {
        $hasJavaFiles = $true
        Write-Host "Found Java files in src" -ForegroundColor Green
    }
}

if (-not $hasJavaFiles) {
    Write-Host "WARNING: No Java files found in src/ or src/main/java/" -ForegroundColor Yellow
}

Write-Host ""

Write-Host "Select operation:" -ForegroundColor Yellow
Write-Host "1. Compile and run"
Write-Host "2. Compile only"
Write-Host "3. Run only"
Write-Host "4. Clean compiled files"
Write-Host "5. Exit"
Write-Host ""

$choice = Read-Host "Enter choice (1-5)"
Write-Host ""

switch ($choice) {
    "1" {
        # Compile and run
        Write-Host "Compiling..." -ForegroundColor Yellow
        
        # Create bin directory
        if (-not (Test-Path "bin")) {
            New-Item -ItemType Directory -Path "bin" -Force | Out-Null
            Write-Host "Created bin directory" -ForegroundColor Gray
        }
        
        # Find Java files
        $javaFiles = @()
        if (Test-Path "src/main/java") {
            $javaFiles = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java" -ErrorAction SilentlyContinue
        }
        if ($javaFiles.Count -eq 0 -and (Test-Path "src")) {
            $javaFiles = Get-ChildItem -Recurse -Path "src" -Filter "*.java" -ErrorAction SilentlyContinue
        }
        
        if ($javaFiles.Count -eq 0) {
            Write-Host "ERROR: No Java files found" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "Found $($javaFiles.Count) Java files" -ForegroundColor Gray
        
        # Build classpath
        $classpath = "."
        if (Test-Path "lib") {
            $libFiles = Get-ChildItem -Path "lib" -Filter "*.jar" -ErrorAction SilentlyContinue
            if ($libFiles.Count -gt 0) {
                $classpath = ".;lib/*"
                Write-Host "Found $($libFiles.Count) JAR files in lib/" -ForegroundColor Gray
            }
        }
        
        # Compile
        Write-Host "Compiling..." -ForegroundColor Gray
        $javaFilesList = @()
        foreach ($file in $javaFiles) {
            $javaFilesList += $file.FullName
        }
        
        javac -cp $classpath -d bin -encoding UTF-8 $javaFilesList
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Compilation failed" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "Compilation successful" -ForegroundColor Green
        Write-Host ""
        
        # Run
        Write-Host "Starting program..." -ForegroundColor Yellow
        
        # Try to find main class
        $mainClass = $null
        
        # First, check if we have a Main class
        $possibleMainClasses = @("Main", "App", "Application", "com.bookrating.Main")
        
        foreach ($testClass in $possibleMainClasses) {
            Write-Host "Trying class: $testClass" -ForegroundColor DarkGray
            $output = java -cp "bin;$classpath" $testClass 2>&1
            if ($LASTEXITCODE -eq 0) {
                $mainClass = $testClass
                Write-Host "Found main class: $mainClass" -ForegroundColor Green
                break
            }
        }
        
        if (-not $mainClass) {
            # Try to find any class with main method
            $classFiles = Get-ChildItem -Recurse -Path "bin" -Filter "*.class" -ErrorAction SilentlyContinue
            if ($classFiles.Count -gt 0) {
                Write-Host "Trying to find main class automatically..." -ForegroundColor Gray
                foreach ($classFile in $classFiles) {
                    $className = $classFile.FullName.Replace("$PWD\bin\", "").Replace("\", ".").Replace(".class", "")
                    Write-Host "Testing: $className" -ForegroundColor DarkGray
                    $output = java -cp "bin;$classpath" $className 2>&1
                    if ($LASTEXITCODE -eq 0) {
                        $mainClass = $className
                        Write-Host "Found main class: $mainClass" -ForegroundColor Green
                        break
                    }
                }
            }
        }
        
        if ($mainClass) {
            Write-Host "Running: $mainClass" -ForegroundColor Cyan
            java -cp "bin;$classpath" $mainClass
        }
        else {
            Write-Host "ERROR: Could not find main class" -ForegroundColor Red
            Write-Host "Please enter main class name manually:" -ForegroundColor Yellow
            $manualClass = Read-Host "Main class name"
            if ($manualClass) {
                Write-Host "Running: $manualClass" -ForegroundColor Cyan
                java -cp "bin;$classpath" $manualClass
            }
        }
    }
    
    "2" {
        # Compile only
        Write-Host "Compiling..." -ForegroundColor Yellow
        
        if (-not (Test-Path "bin")) {
            New-Item -ItemType Directory -Path "bin" -Force | Out-Null
        }
        
        # Find Java files
        $javaFiles = @()
        if (Test-Path "src/main/java") {
            $javaFiles = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java" -ErrorAction SilentlyContinue
        }
        if ($javaFiles.Count -eq 0 -and (Test-Path "src")) {
            $javaFiles = Get-ChildItem -Recurse -Path "src" -Filter "*.java" -ErrorAction SilentlyContinue
        }
        
        if ($javaFiles.Count -eq 0) {
            Write-Host "ERROR: No Java files found" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        # Build classpath
        # 在编译和运行部分，确保类路径正确
        $classpath = "."
        if (Test-Path "lib") {
            $libFiles = Get-ChildItem -Path "lib" -Filter "*.jar"
            if ($libFiles.Count -gt 0) {
                # 构建类路径：当前目录 + 所有jar文件
                $classpath = "."
                foreach ($jar in $libFiles) {
                    $classpath += ";lib/$($jar.Name)"
                }
                Write-Host "Classpath: $classpath" -ForegroundColor Gray
            }
        }
        
        # Compile
        $javaFilesList = @()
        foreach ($file in $javaFiles) {
            $javaFilesList += $file.FullName
        }
        
        javac -cp $classpath -d bin -encoding UTF-8 $javaFilesList
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Compilation failed" -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        Write-Host "Compilation successful" -ForegroundColor Green
        Write-Host "Class files saved to bin/" -ForegroundColor Gray
    }
    
    "3" {
        # Run only
        if (-not (Test-Path "bin")) {
            Write-Host "ERROR: No compiled files found. Please compile first." -ForegroundColor Red
            Read-Host "Press Enter to exit"
            exit 1
        }
        
        # Build classpath
        $classpath = "."
        if (Test-Path "lib") {
            $libFiles = Get-ChildItem -Path "lib" -Filter "*.jar" -ErrorAction SilentlyContinue
            if ($libFiles.Count -gt 0) {
                $classpath = ".;lib/*"
            }
        }
        
        Write-Host "Starting program..." -ForegroundColor Yellow
        
        # Try common main classes
        $mainClass = $null
        $possibleMainClasses = @("Main", "App", "Application", "com.bookrating.Main")
        
        foreach ($testClass in $possibleMainClasses) {
            Write-Host "Trying: $testClass" -ForegroundColor DarkGray
            $output = java -cp "bin;$classpath" $testClass 2>&1
            if ($LASTEXITCODE -eq 0) {
                $mainClass = $testClass
                Write-Host "Found main class: $mainClass" -ForegroundColor Green
                break
            }
        }
        
        if (-not $mainClass) {
            Write-Host "ERROR: Could not find main class automatically" -ForegroundColor Red
            Write-Host "Please enter main class name:" -ForegroundColor Yellow
            $manualClass = Read-Host "Main class name"
            if ($manualClass) {
                $mainClass = $manualClass
            }
            else {
                Read-Host "Press Enter to exit"
                exit 1
            }
        }
        
        Write-Host "Running: $mainClass" -ForegroundColor Cyan
        java -cp "bin;$classpath" $mainClass
    }
    
    "4" {
        # Clean
        Write-Host "Cleaning..." -ForegroundColor Yellow
        if (Test-Path "bin") {
            Remove-Item -Recurse -Force "bin"
            Write-Host "Cleaned bin directory" -ForegroundColor Green
        }
        else {
            Write-Host "Nothing to clean, bin directory does not exist" -ForegroundColor Yellow
        }
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

Write-Host ""
Write-Host "Done" -ForegroundColor Cyan
Read-Host "Press Enter to exit"