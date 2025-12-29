Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   图书打分系统 - 启动脚本" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 检查Java环境
Write-Host "检查Java环境..." -ForegroundColor Yellow
$javaCheck = java -version 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "错误: 未找到Java环境，请安装JDK 17或更高版本" -ForegroundColor Red
    Read-Host "按回车键退出"
    exit 1
}

Write-Host "Java环境检查通过" -ForegroundColor Green

Write-Host ""
Write-Host "请选择操作:" -ForegroundColor Yellow
Write-Host "1. 编译并运行程序"
Write-Host "2. 仅编译程序"
Write-Host "3. 仅运行程序"
Write-Host "4. 清理编译文件"
Write-Host "5. 退出"
Write-Host ""

$choice = Read-Host "请输入选项 (1-5)"
Write-Host ""

switch ($choice) {
    "1" {
        # 编译并运行
        Write-Host "正在编译程序..." -ForegroundColor Yellow
        if (-not (Test-Path "bin")) {
            New-Item -ItemType Directory -Path "bin" -Force | Out-Null
        }
        
        # 编译所有Java文件
        $javaFiles = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java"
        $classpath = "lib/*"
        
        javac -cp $classpath -d bin -encoding UTF-8 $javaFiles.FullName
        if ($LASTEXITCODE -ne 0) {
            Write-Host "编译失败" -ForegroundColor Red
            Read-Host "按回车键退出"
            exit 1
        }
        
        Write-Host "编译成功" -ForegroundColor Green
        Write-Host ""
        Write-Host "正在启动程序..." -ForegroundColor Yellow
        java -cp "bin;lib/*" com.bookrating.Main
    }
    
    "2" {
        # 仅编译
        Write-Host "正在编译程序..." -ForegroundColor Yellow
        if (-not (Test-Path "bin")) {
            New-Item -ItemType Directory -Path "bin" -Force | Out-Null
        }
        
        $javaFiles = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java"
        $classpath = "lib/*"
        
        javac -cp $classpath -d bin -encoding UTF-8 $javaFiles.FullName
        if ($LASTEXITCODE -ne 0) {
            Write-Host "编译失败" -ForegroundColor Red
            Read-Host "按回车键退出"
            exit 1
        }
        
        Write-Host "编译成功" -ForegroundColor Green
    }
    
    "3" {
        # 仅运行
        if (-not (Test-Path "bin")) {
            Write-Host "错误: 未找到编译文件，请先编译程序" -ForegroundColor Red
            Read-Host "按回车键退出"
            exit 1
        }
        
        Write-Host "正在启动程序..." -ForegroundColor Yellow
        java -cp "bin;lib/*" com.bookrating.Main
    }
    
    "4" {
        # 清理
        Write-Host "正在清理编译文件..." -ForegroundColor Yellow
        if (Test-Path "bin") {
            Remove-Item -Recurse -Force "bin"
            Write-Host "清理完成" -ForegroundColor Green
        } else {
            Write-Host "无需清理，bin目录不存在" -ForegroundColor Yellow
        }
    }
    
    "5" {
        Write-Host "退出" -ForegroundColor Cyan
        exit 0
    }
    
    default {
        Write-Host "无效选项，请重新运行" -ForegroundColor Red
        Read-Host "按回车键退出"
        exit 1
    }
}

Write-Host ""
Write-Host "程序执行完毕" -ForegroundColor Cyan
Read-Host "按回车键退出"