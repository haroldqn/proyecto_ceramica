# Solución al Error 403 en Login de Admin

## Problema Identificado
El error 403 en `/api/auth/login` ocurre porque las contraseñas en la base de datos no están en formato BCrypt válido, o estás usando credenciales incorrectas.

**Evidencia en logs:**
```
Encoded password does not look like BCrypt
```

## Solución Rápida (Desarrollo)

He creado un endpoint temporal para restablecer la contraseña del admin.

### Paso 1: Reiniciar el Backend
```bash
# Detener el backend si está corriendo
# Luego reiniciar
cd backend
./mvnw spring-boot:run
```

### Paso 2: Verificar Estado de Contraseñas
Usa este endpoint para ver cuántos usuarios tienen contraseñas inválidas:

**GET** `http://localhost:8080/api/admin/reset/check-users`

Respuesta esperada:
```json
{
  "totalUsers": 1,
  "validBCryptPasswords": 1,
  "invalidPasswords": 0
}
```

### Paso 3: Restablecer Contraseña del Admin
Si el login falla, usa este endpoint para establecer una nueva contraseña:

**POST** `http://localhost:8080/api/admin/reset/password`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "email": "AireSeco@hotmail.com",
  "newPassword": "admin123456"
}
```

**Respuesta exitosa:**
```json
{
  "message": "Contraseña actualizada correctamente"
}
```

### Paso 4: Login con Nueva Contraseña
Ahora puedes hacer login en el frontend con:
- **Email:** `AireSeco@hotmail.com`
- **Contraseña:** `admin123456` (o la que hayas establecido)

## Solución Alternativa (SQL Directo)

Si prefieres usar SQL directamente:

```sql
USE ceramic_store_db;

-- Actualizar contraseña (ejemplo: admin123456)
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqQZQZQZQZQZQZQZQZQZQZQZQZQZQZQ' 
WHERE email = 'AireSeco@hotmail.com';
```

**Nota:** El hash de arriba es de ejemplo. Para generar un hash BCrypt real:

1. Crea una clase Java temporal:
```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "tu_contraseña";
        String hash = encoder.encode(password);
        System.out.println(hash);
    }
}
```

2. Usa el hash generado en el UPDATE SQL.

## Limpieza (Importante)

Después de restablecer la contraseña, **elimina el endpoint temporal** por seguridad:

1. Elimina el archivo:
   ```
   backend/src/main/java/com/example/backend/controllers/AdminResetController.java
   ```

2. Revierte el cambio en `SecurityConfig.java`:
   ```java
   // Elimina esta línea:
   .requestMatchers("/api/admin/reset/**").permitAll()  // Temporal para desarrollo
   ```

3. Reinicia el backend.

## Prevención Futura

Para evitar este problema:
1. Siempre usa `passwordEncoder.encode()` al guardar contraseñas
2. Nunca almacenes contraseñas en texto plano
3. Usa migraciones de base de datos para actualizar contraseñas existentes

## Verificación

Después de aplicar la solución, verifica que el login funcione:

1. Inicia sesión en el frontend (`http://localhost:3000`)
2. Usa las credenciales del admin
3. Deberías recibir un token JWT y ser redirigido al dashboard

Si el problema persiste, revisa los logs del backend para más detalles.