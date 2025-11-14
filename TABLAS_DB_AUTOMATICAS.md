# 📊 Tablas que se Crearán Automáticamente en unieatsdb

## ✅ Cuando inicies la aplicación, Hibernate creará estas tablas:

### 🔐 **Sistema de Usuarios y Autenticación**
1. **usuarios** - Usuarios del sistema (estudiantes, vendedores, admin)
2. **roles** - Roles de usuario (ESTUDIANTE, VENDEDOR, ADMIN)
3. **usuario_roles** - Tabla intermedia (relación many-to-many)

### 🏪 **Gestión de Tiendas**
4. **tiendas** - Información de las tiendas de vendedores
5. **horarios** - Horarios de atención de cada tienda

### 📦 **Productos y Opciones**
6. **productos** - Catálogo de productos de cada tienda
7. **categorias_opciones** - Categorías de opciones (salsas, adiciones, etc.)
8. **opciones** - Opciones específicas dentro de cada categoría
9. **productos_potenciados** - Sistema de promoción de productos

### 🛒 **Sistema de Pedidos**
10. **pedidos** - Pedidos realizados por estudiantes
11. **detalles_pedido** - Items específicos de cada pedido
12. **detalle_pedido_opciones** - Opciones seleccionadas por item

### 📧 **Sistema CRM y Marketing**
13. **clientes** - Perfiles de clientes para CRM
14. **segmentos** - Segmentos de clientes (frecuentes, nuevos, etc.)
15. **campanas** - Campañas de marketing
16. **email_templates** - Plantillas de correo
17. **notificaciones_marketing** - Historial de notificaciones enviadas
18. **interacciones_cliente** - Interacciones de clientes con el sistema

### 📢 **Sistema de Anuncios**
19. **anuncios** - Anuncios generales
20. **tipo_anuncio** - Tipos de anuncios (enum)
21. **anuncios_personalizados** - Anuncios personalizados por segmento

### 🤖 **Machine Learning y Recomendaciones**
22. **recomendacion_ml** - Recomendaciones de ML completas
23. **recomendacion_ml_minima** - Recomendaciones ML simplificadas
24. **usuario_comportamiento** - Comportamiento de usuarios para ML

### 📊 **Integraciones**
25. **powerbi_config** - Configuración de Power BI por vendedor

### 🔗 **Tablas Adicionales (automáticas)**
- **spring_session** (si está habilitado)
- **flyway_schema_history** (si usas Flyway)
- Tablas de auditoría (si están configuradas)

---

## 🎯 Total: ~25-30 tablas

### ¿Cómo funciona?

Cuando inicies la aplicación:

1. **Spring Boot arranca**
2. **Hibernate detecta todas las clases `@Entity`**
3. **Lee las anotaciones `@Table`, `@Column`, `@ManyToOne`, etc.**
4. **Genera automáticamente los comandos SQL CREATE TABLE**
5. **Ejecuta los comandos en la base de datos `unieatsdb`**
6. **Crea índices, foreign keys, constraints, etc.**

### Configuración clave:

```properties
spring.jpa.hibernate.ddl-auto=update
```

**Opciones disponibles:**
- `create` - Borra y crea las tablas cada vez (⚠️ pierdes datos)
- `create-drop` - Crea al inicio, borra al cerrar
- `update` - **Actualiza solo lo necesario (RECOMENDADO)** ✅
- `validate` - Solo valida, no modifica
- `none` - No hace nada

### Ver el SQL que se ejecuta:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Con esto verás en la consola todos los comandos SQL que Hibernate ejecuta.

---

## 🔍 Verificar las Tablas Creadas

### Opción 1: pgAdmin 4
1. Abre pgAdmin 4
2. Navega a: **Servers → PostgreSQL 17 → Databases → unieatsdb → Schemas → public → Tables**
3. Verás todas las tablas listadas

### Opción 2: Línea de comandos
```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb -c "\dt"
```

### Opción 3: Desde psql interactivo
```powershell
& "C:\Program Files\PostgreSQL\17\bin\psql.exe" -U postgres -d unieatsdb
```
Luego ejecuta:
```sql
\dt              -- Listar todas las tablas
\d+ usuarios     -- Ver estructura de tabla usuarios
\d+ productos    -- Ver estructura de tabla productos
```

---

## 📝 Datos Iniciales

**⚠️ IMPORTANTE:** Las tablas se crearán vacías. Necesitarás:

1. **Crear roles iniciales** (ESTUDIANTE, VENDEDOR, ADMIN)
2. **Crear usuario admin**
3. **Poblar datos de prueba**

### Script SQL para datos iniciales:

```sql
-- Insertar roles
INSERT INTO roles (nombre) VALUES ('ESTUDIANTE'), ('VENDEDOR'), ('ADMIN');

-- Crear usuario admin (contraseña: admin123)
INSERT INTO usuarios (correo, password, nombre, apellido, activo) 
VALUES ('admin@unieats.com', '$2a$10$...', 'Admin', 'Sistema', true);

-- Asignar rol ADMIN
INSERT INTO usuario_roles (usuario_id, rol_id) 
VALUES (1, 3);
```

---

## 🚀 Próximo Paso

1. **Actualiza la contraseña** en `application.properties`
2. **Inicia la aplicación:** `./mvnw.cmd spring-boot:run`
3. **Verifica que las tablas se crearon**
4. **Inserta datos iniciales** (roles y admin)

---

**Fecha:** 10 de noviembre de 2025  
**Estado:** ✅ Listo para crear tablas automáticamente
