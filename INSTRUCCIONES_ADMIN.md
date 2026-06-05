# 📋 Instrucciones para Acceder al Panel de Administración

## 🔐 Credenciales de Administrador

El sistema ahora incluye un usuario administrador por defecto:

| Campo | Valor |
|-------|-------|
| **Email** | `admin@ceramica.com` |
| **Contraseña** | `admin123` |
| **Rol** | `ADMIN` |

## 🚀 Pasos para Acceder

### 1. Iniciar el Backend
```bash
cd backend
./mvnw spring-boot:run
# o en Windows: mvnw.cmd spring-boot:run
```

El backend se ejecutará en `http://localhost:8080`

### 2. Iniciar el Frontend
```bash
cd frontend
npm install  # Solo la primera vez
npm run dev
```

El frontend se ejecutará en `http://localhost:3000`

### 3. Iniciar Sesión
1. Abre `http://localhost:3000` en tu navegador
2. Haz clic en cualquier botón que requiera autenticación (ej: "Ver productos destacados")
3. Se abrirá el modal de autenticación
4. Usa las credenciales:
   - Email: `admin@ceramica.com`
   - Contraseña: `admin123`
5. Haz clic en "Entrar con correo"

### 4. Acceder al Panel Admin
Una vez autenticado, el sistema detectará que tienes rol `ADMIN` y podrás:
- Acceder directamente a `http://localhost:3000/admin`
- O navegar usando el menú lateral del panel

## 📁 Archivos Modificados/Creados

Para habilitar el acceso admin, se realizaron los siguientes cambios:

### 1. `backend/src/main/resources/application.properties`
Se agregaron las líneas:
```properties
spring.sql.init.mode=always
spring.sql.init.continue-on-error=true
```

### 2. `backend/src/main/resources/data.sql`
Se configuraron los datos iniciales:
- Roles: `CLIENTE` y `ADMIN`
- Persona del administrador
- Datos de ejemplo (categorías, productos, tallas)

### 3. `backend/src/main/java/com/example/backend/controller/TestAdminController.java` ⭐ **NUEVO**
Se creó un componente que se ejecuta automáticamente al iniciar la aplicación:
- **Verifica** si ya existe un usuario con rol ADMIN
- **Si no existe**, lo crea automáticamente con:
  - Email: `admin@ceramica.com`
  - Contraseña: `admin123` (encriptada con BCrypt dinámicamente)
  - Rol: ADMIN
- **Muestra** un mensaje en consola confirmando la creación

### ¿Por qué usar un componente Java en lugar de SQL directo?
El hash de BCrypt **no puede ser hardcodeado** en SQL porque:
1. BCrypt genera un hash diferente cada vez (usa salt aleatorio)
2. El hash en SQL podría no coincidir con la contraseña
3. El componente Java usa `PasswordEncoder` de Spring Security para generar el hash correcto

**Flujo automático:**
```
1. Inicias el backend
2. TestAdminController se ejecuta
3. Verifica si existe admin en la base de datos
4. Si no existe → lo crea con hash BCrypt válido
5. Si ya existe → no hace nada
```

## 🗄️ Base de Datos

El script `data.sql` crea automáticamente:

### Tabla `roles`
| id_role | name |
|---------|------|
| 1 | CLIENTE |
| 2 | ADMIN |

### Tabla `users` (usuario admin)
| id_user | email | role |
|---------|-------|------|
| 1 | admin@ceramica.com | ADMIN |

### Otros datos
- 1 categoría: "Animales"
- 2 productos de ejemplo
- 2 tallas disponibles

## ⚠️ Notas Importantes

1. **Contraseña por defecto**: La contraseña `admin123` es solo para desarrollo. **Cámbiala en producción**.

2. **Base de datos existente**: Si ya tienes una base de datos con datos, el script usa `INSERT IGNORE` para no duplicar registros.

3. **Reiniciar la aplicación**: Después de los cambios, reinicia el backend para que se ejecuten los scripts SQL.

4. **Problemas de conexión**: Asegúrate de que MySQL esté corriendo y la base de datos `ceramic` exista.

## 🔧 Solución de Problemas

### Error: "Database not found"
Crea la base de datos manualmente:
```sql
CREATE DATABASE ceramic;
```

### Error: "Access denied"
Verifica las credenciales en `application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=12345678
```

### El usuario admin no puede iniciar sesión
1. **Verifica que el componente se haya ejecutado:**
   - Revisa los logs del backend al iniciar
   - Deberías ver: "✅ Usuario administrador creado exitosamente!"
   
2. **Verifica en la base de datos:**
```sql
SELECT u.email, r.name 
FROM users u 
JOIN roles r ON u.id_role = r.id_role 
WHERE u.email = 'admin@ceramica.com';
```
Debe mostrar: `admin@ceramica.com | ADMIN`

3. **Si no existe, reinicia el backend** para que se ejecute el componente nuevamente.

### El panel admin muestra "Acceso Denegado"
1. Limpia el localStorage del navegador
2. Vuelve a iniciar sesión
3. Verifica que el rol sea `ADMIN` en las herramientas de desarrollador

### Ver logs del componente admin
Al iniciar el backend, busca en la consola:
```
🔧 Creando usuario administrador por defecto...
✅ Usuario administrador creado exitosamente!
   Email: admin@ceramica.com
   Contraseña: admin123
   Rol: ADMIN
   ID Usuario: X
```
1. **Cambiar contraseña del admin** por seguridad
2. **Crear más usuarios admin** si es necesario
3. **Configurar roles y permisos** adicionales
4. **Implementar gestión de usuarios** desde el panel admin
5. **Agregar validaciones** de seguridad adicionales

---

**¿Problemas?** Revisa los logs del backend para más detalles.