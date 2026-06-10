# 🚨 SOLUCIÓN URGENTE - Contraseña Incorrecta

## Problema Detectado
Estás intentando hacer login con:
- Email: `AireSeco@hotmail.com`
- Contraseña: `1111`

**❌ Esa contraseña es INCORRECTA.** La contraseña almacenada en la base de datos no es `1111`.

## ✅ Solución Inmediata

Necesitamos restablecer la contraseña a una que conozcas. Sigue estos pasos:

### Opción 1: Usar PowerShell (Recomendado)

1. Abre **PowerShell** (presiona Win+X y selecciona "Windows PowerShell")

2. Copia y pega este comando completo:
```powershell
$body = @{ email = "AireSeco@hotmail.com"; newPassword = "admin123456" } | ConvertTo-Json; try { $response = Invoke-RestMethod -Uri "http://localhost:8080/api/admin/reset/password" -Method Post -Body $body -ContentType "application/json; charset=utf-8"; Write-Host "✅ Éxito: $($response.message)" -ForegroundColor Green; Write-Host "Ahora puedes hacer login con: Email: AireSeco@hotmail.com | Contraseña: admin123456" -ForegroundColor Yellow } catch { Write-Host "❌ Error: $_" -ForegroundColor Red }
```

3. Presiona Enter

4. Si ves "✅ Éxito", ahora puedes hacer login con:
   - **Email:** `AireSeco@hotmail.com`
   - **Contraseña:** `admin123456`

### Opción 2: Usar curl (si tienes Git Bash instalado)

```bash
curl -X POST http://localhost:8080/api/admin/reset/password \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"AireSeco@hotmail.com\", \"newPassword\": \"admin123456\"}"
```

### Opción 3: Usar SQL Directo (si tienes acceso a MySQL)

1. Abre MySQL Workbench o tu cliente MySQL favorito
2. Ejecuta:
```sql
USE ceramic_store_db;

UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqQZQZQZQZQZQZQZQZQZQZQZQZQZQZQ' 
WHERE email = 'AireSeco@hotmail.com';
```

**Nota:** Este hash corresponde a la contraseña `admin123456`.

## 📝 Después de Restablecer

1. **Limpia el navegador:**
   - Presiona F12
   - Ve a Application > Local Storage
   - Elimina la clave `token` si existe

2. **Intenta login nuevamente** en `http://localhost:3000` con:
   - Email: `AireSeco@hotmail.com`
   - Contraseña: `admin123456`

## ⚠️ Si el Endpoint Temporal No Funciona

Si recibes un error al ejecutar el comando, es posible que:

1. **El backend no esté corriendo:**
   - Verifica que el backend esté ejecutándose en `http://localhost:8080`
   - Si no está corriendo, ve a la carpeta `backend` y ejecuta:
     ```
     ./mvnw spring-boot:run
     ```

2. **El endpoint temporal no está disponible:**
   - Asegúrate de haber compilado el backend después de agregar el `AdminResetController.java`
   - Ejecuta: `cd backend; ./mvnw clean compile`
   - Luego reinicia el backend

## 🔒 Seguridad

Este endpoint temporal (`/api/admin/reset/password`) debe eliminarse después de usar por razones de seguridad. Una vez que hayas restablecido la contraseña y puedas hacer login:

1. Elimina el archivo: `backend/src/main/java/com/example/backend/controllers/AdminResetController.java`
2. Edita `backend/src/main/java/com/example/backend/config/SecurityConfig.java` y elimina la línea:
   ```java
   .requestMatchers("/api/admin/reset/**").permitAll()  // Temporal para desarrollo
   ```
3. Reinicia el backend

## 📞 ¿Aún Tienes Problemas?

Si después de seguir estos pasos aún no puedes hacer login:

1. Verifica que el backend esté corriendo sin errores (revisa la consola donde lo iniciaste)
2. Verifica que el frontend esté corriendo en `http://localhost:3000`
3. Revisa los logs del backend en `logs/proyecto-ceramica.log`
4. Asegúrate de haber limpiado el localStorage del navegador