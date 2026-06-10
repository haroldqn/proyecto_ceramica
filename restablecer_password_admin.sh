#!/bin/bash
# Script para restablecer contraseña del admin
# Ejecutar en PowerShell o terminal

$body = @{
    email = "AireSeco@hotmail.com"
    newPassword = "admin123456"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/reset/password" -Method Post -Body $body -ContentType "application/json; charset=utf-8"
    Write-Host "✅ Éxito: $($response.message)" -ForegroundColor Green
    Write-Host ""
    Write-Host "Ahora puedes hacer login con:" -ForegroundColor Yellow
    Write-Host "  Email: AireSeco@hotmail.com" -ForegroundColor Cyan
    Write-Host "  Contraseña: admin123456" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Asegúrate de que:" -ForegroundColor Yellow
    Write-Host "  1. El backend esté corriendo en http://localhost:8080" -ForegroundColor Cyan
    Write-Host "  2. El endpoint temporal esté disponible" -ForegroundColor Cyan
}