# Pasos para Solucionar Error 403 en Login de Admin

## ✅ Cambios Realizados

1. **Frontend (`api-client.ts`):**
   - Se eliminó el envío de token en rutas de login/registro
   - Se agregó limpieza automática de tokens inválidos
   - Mejor manejo de errores 403 y 401

2. **Backend:**
   - Se creó endpoint temporal para restablecer contraseña
   - Se actualizó `SecurityConfig` para permitir acceso

## 🚀 Pasos a Seguir

### Paso 1: Reiniciar el Backend
El backend necesita reiniciarse para cargar los nuevos cambios:

1. Detén el backend actual (Ctrl+C en la terminal donde corre)
2. Ejecuta:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

### Paso 2: Limpiar Datos del Navegador
1. Abre el navegador (Chrome, Firefox, etc.)
2. Presiona F12 para abrir DevTools
3. Ve a Application > Local Storage > http://localhost:3000
4. Elimina la clave `token` si existe
5. También puedes limpiar caché: Ctrl+Shift+Supr

### Paso 3: Verificar Estado de Contraseñas
Una vez reiniciado el backend, verifica si hay contraseñas inválidas:

**Usa Postman o curl:**
```bash
curl http://localhost:8080/api/admin/reset/check-users
```

**Respuesta esperada:**
```json
{
  "totalUsers": 1,
  "validBCryptPasswords": 1,
  "invalidPasswords": 0
}
```

Si `invalidPasswords > 0`, hay contraseñas en texto plano que necesitan corregirse.

### Paso 4: Restablecer Contraseña (si es necesario)
Si no puedes hacer login o las contraseñas son inválidas:

**Usa Postman o curl:**
```bash
curl -X POST http://localhost:8080/api/admin/reset/password \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"AireSeco@hotmail.com\", \"newPassword\": \"admin123456\"}"
```

**Respuesta exitosa:**
```json
{
  "message": "Contraseña actualizada correctamente"
}
```

### Paso 5: Hacer Login
Ahora intenta login en el frontend (`http://localhost:3000`):

- **Email:** `AireSeco@hotmail.com`
- **Contraseña:** `admin123456` (o la que hayas establecido)

### Paso 6: Limpieza de Seguridad (Importante)
Después de restablecer la contraseña, elimina el endpoint temporal:

1. Elimina el archivo:
   ```
   backend/src/main/java/com/example/backend/controllers/AdminResetController.java
   ```

2. Edita `backend/src/main/java/com/example/backend/config/SecurityConfig.java` y elimina esta línea:
   ```java
   .requestMatchers("/api/admin/reset/**").permitAll()  // Temporal para desarrollo
   ```

3. Reinicia el backend nuevamente.

## 🔍 Diagnóstico del Problema Original

El error 403 ocurría porque:
1. El `api-client.ts` enviaba un token JWT (posiblemente inválido) incluso en solicitudes de login
2. Las contraseñas en la base de datos pueden no estar en formato BCrypt válido
3. Spring Security rechazaba la autenticación por estas razones

## 📝 Notas Importantes

- El usuario admin en la BD tiene email: `AireSeco@hotmail.com`
- Su contraseña actual tiene hash BCrypt válido (comienza con `$2a$10$`)
- Si olvidaste la contraseña, usa el endpoint temporal para restablecerla
- Nunca almacenes contraseñas en texto plano en producción

## ❓ Si el Problema Persiste

1. Verifica que el backend esté corriendo en `http://localhost:8080`
2. Verifica que el frontend esté corriendo en `http://localhost:3000`
3. Revisa los logs del backend en `logs/proyecto-ceramica.log`
4. Asegúrate de que MySQL esté corriendo y accesible

## 🛠️ Comandos Útiles

**Ver logs del backend en tiempo real:**
```bash
tail -f logs/proyecto-ceramica.log
```

**Detener backend:**
```bash
# En la terminal donde corre, presiona Ctrl+C
```

**Reiniciar frontend:**
```bash
cd frontend
npm run dev