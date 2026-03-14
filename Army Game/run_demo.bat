@echo off
REM === Thiết lập UTF-8 encoding cho CMD ===
chcp 65001 > nul 2>&1

REM === Đặt encoding cho Java ===
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

cd /d "%~dp0"

echo [*] Compiling DemoScriptRunner...
if not exist out mkdir out

REM === Biên dịch tất cả Java files ===
setlocal enabledelayedexpansion
set SOURCES=
for /r src %%f in (*.java) do set SOURCES=!SOURCES! "%%f"
javac -encoding UTF-8 -d out %SOURCES%

if %errorlevel% neq 0 (
    echo [!] Compile failed!
    pause
    exit /b 1
)

echo [*] Running Army Game Demo...
echo.
java -Dfile.encoding=UTF-8 -cp out DemoScriptRunner
pause
