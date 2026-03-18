@echo off
REM ========================================
REM  Army Game - GUI Version
REM ========================================

cd /d "%~dp0"

echo Compiling Army Game GUI...
javac -d out -sourcepath src src/MainGUI.java 2>nul

if %ERRORLEVEL% NEQ 0 (
    echo Error: Compilation failed!
    pause
    exit /b 1
)

echo.
echo ===================================
echo  Starting Army Game GUI...
echo ===================================
echo.

java -cp out MainGUI

pause

