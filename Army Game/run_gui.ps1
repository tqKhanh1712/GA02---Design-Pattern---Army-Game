#!/usr/bin/env powershell

# ========================================
#  Army Game - GUI Version (PowerShell)
# ========================================

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

Write-Host "Compiling Army Game GUI..." -ForegroundColor Cyan

javac -d out -sourcepath src src/MainGUI.java 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "Error: Compilation failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "===================================" -ForegroundColor Yellow
Write-Host "  Starting Army Game GUI..." -ForegroundColor Yellow
Write-Host "===================================" -ForegroundColor Yellow
Write-Host ""

java -cp out MainGUI

