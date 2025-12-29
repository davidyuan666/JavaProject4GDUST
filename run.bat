@echo off
chcp 65001 >nul
echo ========================================
echo   图书打分系统 - 启动脚本
echo ========================================
echo.

REM 检查Java环境
echo 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java环境，请安装JDK 17或更高版本
    pause
    exit /b 1
)

echo Java环境检查通过

echo.
echo 请选择操作:
echo 1. 编译并运行程序
echo 2. 仅编译程序
echo 3. 仅运行程序
echo 4. 清理编译文件
echo 5. 退出
echo.
set /p choice=请输入选项 (1-5): 

echo.

if "%choice%"=="1" goto compile_and_run
if "%choice%"=="2" goto compile_only
if "%choice%"=="3" goto run_only
if "%choice%"=="4" goto clean
if "%choice%"=="5" goto exit

echo 无效选项，请重新运行
pause
exit /b 1

:compile_and_run
echo 正在编译程序...
if not exist "bin" mkdir bin
javac -cp "lib/*" -d bin -encoding UTF-8 src/main/java/com/bookrating/**/*.java
if %errorlevel% neq 0 (
    echo 编译失败
    pause
    exit /b 1
)
echo 编译成功

echo.
echo 正在启动程序...
java -cp "bin;lib/*" com.bookrating.Main
goto end

:compile_only
echo 正在编译程序...
if not exist "bin" mkdir bin
javac -cp "lib/*" -d bin -encoding UTF-8 src/main/java/com/bookrating/**/*.java
if %errorlevel% neq 0 (
    echo 编译失败
    pause
    exit /b 1
)
echo 编译成功
goto end

:run_only
echo 正在启动程序...
if not exist "bin" (
    echo 错误: 未找到编译文件，请先编译程序
    pause
    exit /b 1
)
java -cp "bin;lib/*" com.bookrating.Main
goto end

:clean
echo 正在清理编译文件...
if exist "bin" (
    rmdir /s /q bin
    echo 清理完成
) else (
    echo 无需清理，bin目录不存在
)
goto end

:exit
echo 退出
pause
exit /b 0

:end
echo.
echo 程序执行完毕
pause