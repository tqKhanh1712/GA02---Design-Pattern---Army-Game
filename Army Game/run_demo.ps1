# Script chạy demo Army Game với UTF-8 encoding tốt
# Cách dùng: .\run_demo.ps1

$ErrorActionPreference = "Continue"

# Thiết lập encoding cho PowerShell console
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Chuyển vào thư mục hiện tại
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath

Write-Host "[*] Compiling DemoScriptRunner..." -ForegroundColor Cyan

# Tạo thư mục out nếu chưa có
if (-not (Test-Path "out")) {
    New-Item -ItemType Directory -Name "out" | Out-Null
}

# Tìm tất cả file .java và biên dịch
Write-Host "[*] Finding Java files..." -ForegroundColor Yellow
$javaFiles = @(Get-ChildItem -Recurse -Filter "*.java" -File | Select-Object -ExpandProperty FullName)
Write-Host "Found $($javaFiles.Count) Java files" -ForegroundColor Yellow

if ($javaFiles.Count -eq 0) {
    Write-Host "[!] No Java files found!" -ForegroundColor Red
    Read-Host "Press any key to continue"
    exit 1
}

# Biên dịch
Write-Host "[*] Compiling..." -ForegroundColor Yellow
& javac -encoding UTF-8 -d out @javaFiles 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host "[!] Compile failed!" -ForegroundColor Red
    Read-Host "Press any key to continue"
    exit 1
}

Write-Host "[✓] Compile successful!" -ForegroundColor Green
Write-Host ""
Write-Host "[*] Running Army Game Demo..." -ForegroundColor Cyan
Write-Host ""

# Chạy demo (không dùng JAVA_TOOL_OPTIONS vì PowerShell cắt parameter)
& java -Dfile.encoding=UTF-8 -cp out DemoScriptRunner

Write-Host ""
Write-Host "[*] Demo completed!" -ForegroundColor Green
Read-Host "Press any key to continue"



