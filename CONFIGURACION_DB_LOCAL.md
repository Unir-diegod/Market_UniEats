# 🗄️ Base de Datos Local PostgreSQL - Configuración

## ✅ Estado Actual

**Base de datos creada:** `unieatsdb`  
**Motor:** PostgreSQL 17  
**Host:** localhost  
**Puerto:** 5432  
**Usuario:** postgres  
**Contraseña:** [La que configuraste durante la instalación]

---

## 📋 Configuración Aplicada

### **Archivo modificado:** `application.properties`

```properties
# Conexión a PostgreSQL LOCAL
spring.datasource.url=jdbc:postgresql://localhost:5432/unieatsdb
spring.datasource.username=postgres
spring.datasource.password=postgres  # ⚠️ CAMBIAR POR TU CONTRASEÑA
```

---

## 🚀 Pasos para Iniciar

### 1. **Actualizar Contraseña**

Abre `src/main/resources/application.properties` y cambia la línea:

```properties
spring.datasource.password=postgres
```

Por tu contraseña real de PostgreSQL.

### 2. **Iniciar la Aplicación**

```bash
./mvnw.cmd spring-boot:run
```

O desde tu IDE (IntelliJ/Eclipse/VS Code).

### 3. **Hibernate creará las tablas automáticamente**

Con `spring.jpa.hibernate.ddl-auto=update`, Spring Boot creará todas las tablas basándose en tus entidades JPA:

- ✅ usuarios
- ✅ roles
- ✅ tiendas
- ✅ productos
- ✅ pedidos
- ✅ detalle_pedido
- ✅ horarios
- ✅ categorias_opcion
- ✅ opciones
- ✅ Y todas las demás...

---

## 🔍 Verificar que Funcionó

### Desde pgAdmin 4:

1. Abre **pgAdmin 4**
2. Conecta a **localhost**
3. Navega a: **Servers → PostgreSQL 17 → Databases → unieatsdb → Schemas → public → Tables**
4. Deberías ver todas las tablas creadas

### Desde línea de comandos:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb -c "\dt"
```

---

## 📊 Diferencias con Supabase

| Aspecto | Supabase (Anterior) | PostgreSQL Local (Actual) |
|---------|---------------------|---------------------------|
| **Ubicación** | ☁️ Nube | 💻 Tu máquina |
| **Velocidad** | Depende de internet | ⚡ Muy rápido |
| **Acceso remoto** | ✅ Sí | ❌ No (solo localhost) |
| **Costo** | Plan gratuito limitado | 🆓 Totalmente gratis |
| **Backups** | Automáticos | Manual |
| **Migración a producción** | Fácil (mismo Supabase) | Requiere exportar/importar |

---

## 🔄 Restaurar Datos de Supabase (Opcional)

Si quieres migrar los datos existentes de Supabase a local:

### 1. Exportar desde Supabase:

```bash
# Desde pgAdmin o línea de comandos de Supabase
pg_dump -h db.lfvweearttrisbbhemld.supabase.co -U postgres -d postgres > backup_supabase.sql
```

### 2. Importar a local:

```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb -f backup_supabase.sql
```

---

## ⚙️ Configuración Avanzada (Opcional)

### Pool de Conexiones:

```properties
# Agregar a application.properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
```

### Timezone:

```properties
spring.jpa.properties.hibernate.jdbc.time_zone=America/Bogota
```

---

## 🔧 Solución de Problemas

### Error: "conexión rechazada"
- ✅ Verifica que PostgreSQL esté corriendo
- ✅ Verifica el puerto 5432 no esté bloqueado

### Error: "autenticación falló"
- ✅ Verifica la contraseña en `application.properties`
- ✅ Verifica que el usuario `postgres` exista

### Error: "base de datos no existe"
- ✅ Ejecuta nuevamente el comando de creación:
  ```powershell
  & "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -c "CREATE DATABASE unieatsdb;"
  ```

---

## 📝 Comandos Útiles

```powershell
# Listar bases de datos
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -c "\l"

# Conectarse a unieatsdb
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb

# Ver tablas
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb -c "\dt"

# Ver registros de usuarios
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb -c "SELECT * FROM usuarios;"
```

---

**Fecha de configuración:** 10 de noviembre de 2025  
**Versión PostgreSQL:** 17  
**Estado:** ✅ Configurado y listo para usar
