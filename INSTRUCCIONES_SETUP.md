# Instrucciones para Ejecutar el Proyecto Cerámica

## Requisitos Previos

- **Node.js** (v18 o superior)
- **Java JDK 17+** (para el backend Spring Boot)
- **Docker** y **Docker Compose**
- **Git**
- **PostgreSQL** (opcional, si no usas Docker)

## Paso 1: Clonar el Repositorio

```bash
git clone https://github.com/haroldqn/proyecto_ceramica.git
cd proyecto_ceramica
git checkout itzair
```

## Paso 2: Configurar la Base de Datos

### Opción A: Usando Docker (Recomendado)

1. Iniciar los contenedores Docker:
```bash
docker-compose up -d
```

2. Verificar que los contenedores estén corriendo:
```bash
docker-compose ps
```

Deberías ver:
- `proyecto_ceramica-db` (PostgreSQL)
- Otros servicios si están configurados

3. Esperar a que PostgreSQL esté listo (aproximadamente 30 segundos)

### Opción B: Usando PostgreSQL Local

1. Crear una base de datos llamada `proyecto_ceramica`
2. Configurar las credenciales en `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/proyecto_ceramica
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

## Paso 3: Configurar el Backend (Spring Boot)

1. Navegar al directorio del backend:
```bash
cd backend
```

2. Compilar el proyecto:
```bash
./mvnw clean install
```
o en Windows:
```bash
mvnw.cmd clean install
```

3. Ejecutar la aplicación:
```bash
./mvnw spring-boot:run
```
o en Windows:
```bash
mvnw.cmd spring-boot:run
```

4. El backend estará disponible en: `http://localhost:8080`

## Paso 4: Configurar el Frontend (Next.js)

1. Abrir una nueva terminal y navegar al directorio frontend:
```bash
cd frontend
```

2. Instalar dependencias:
```bash
npm install
```

3. Crear archivo de variables de entorno:
```bash
cp .env.local .env.local.example
```

4. Editar `.env.local` si es necesario (generalmente no se requiere cambios)

5. Ejecutar el servidor de desarrollo:
```bash
npm run dev
```

6. El frontend estará disponible en: `http://localhost:3000`

## Paso 5: Cargar Datos Iniciales (Opcional)

El backend incluye un script SQL para datos iniciales:

1. Ubicar el archivo: `backend/src/main/resources/data.sql`
2. Los datos se cargan automáticamente al iniciar la aplicación si está configurado en `application.properties`:
```properties
spring.sql.init.mode=always
```

O ejecutar manualmente en PostgreSQL:
```bash
psql -U tu_usuario -d proyecto_ceramica -f backend/src/main/resources/data.sql
```

## Paso 6: Verificar la Instalación

1. **Frontend**: Abrir `http://localhost:3000` en el navegador
2. **Backend API**: Probar `http://localhost:8080/api/products`
3. **Base de datos**: Conectarse a PostgreSQL para verificar tablas

## Solución de Problemas Comunes

### Error: Puerto ya en uso
- Cambiar puertos en `application.properties` (backend) o `next.config.ts` (frontend)
- O matar procesos: `netstat -ano | findstr :8080` y `taskkill /PID <PID> /F`

### Error: Conexión a base de datos fallida
- Verificar que PostgreSQL esté corriendo
- Verificar credenciales en `application.properties`
- Verificar que la base de datos exista

### Error: Dependencias no encontradas
- Ejecutar `npm install` nuevamente en frontend
- Ejecutar `./mvnw clean install` en backend

### Error: Permisos en scripts
- En Linux/Mac: `chmod +x mvnw` y `chmod +x backend/mvnw`

## Estructura del Proyecto

```
proyecto_ceramica/
├── backend/           # Spring Boot API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── data.sql
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
├── frontend/          # Next.js Application
│   ├── src/
│   │   ├── app/
│   │   ├── components/
│   │   └── features/
│   ├── package.json
│   └── .env.local
├── database/          # Scripts de base de datos
│   └── backup/
├── docker-compose.yml # Configuración Docker
└── README.md
```

## Comandos Útiles

### Backend
```bash
# Compilar
./mvnw clean package

# Ejecutar tests
./mvnw test

# Ejecutar aplicación
./mvnw spring-boot:run
```

### Frontend
```bash
# Instalar dependencias
npm install

# Desarrollo
npm run dev

# Build de producción
npm run build

# Ejecutar en producción
npm start
```

### Docker
```bash
# Iniciar servicios
docker-compose up -d

# Detener servicios
docker-compose down

# Ver logs
docker-compose logs -f

# Reiniciar servicios
docker-compose restart
```

## Notas Importantes

1. **Variables de Entorno**: El frontend usa `.env.local` para configuración. No modificar a menos que sea necesario.

2. **Base de Datos**: El backend usa Hibernate para generar tablas automáticamente. No es necesario crear tablas manualmente.

3. **Puertos por Defecto**:
   - Frontend: 3000
   - Backend: 8080
   - PostgreSQL: 5432

4. **Credenciales por Defecto** (si aplica):
   - Usuario admin: admin@ceramica.com / admin123
   - (Verificar en `data.sql`)

5. **Logs**: Los logs del backend se guardan en `backend/logs/`

## Soporte

Si encuentras problemas:
1. Revisar logs del backend en `backend/logs/`
2. Revisar consola del frontend
3. Verificar que todos los servicios estén corriendo
4. Consultar documentación de Spring Boot y Next.js

## Actualizaciones Futuras

Para actualizar el proyecto:
```bash
git pull origin itzair
# Backend
cd backend && ./mvnw clean install
# Frontend
cd frontend && npm install
```

---

**Última actualización**: 10/06/2026
**Rama**: itzair
**Commit**: 29bd6e3