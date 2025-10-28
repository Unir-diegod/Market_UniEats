# 🎯 GUÍA COMPLETA: Sistema CRM de Marketing - UniEats

## 📚 ÍNDICE RÁPIDO
1. [¿Qué es el CRM y para qué sirve?](#qué-es)
2. [¿Cómo funciona automáticamente?](#cómo-funciona)
3. [Características principales](#características)
4. [Cómo probarlo PASO A PASO](#cómo-probarlo)
5. [URLs y accesos](#urls)

---

## 🎯 ¿Qué es el CRM y para qué sirve? {#qué-es}

El **CRM (Customer Relationship Management)** es un sistema de marketing inteligente que:

### 📊 **Gestiona Clientes Automáticamente**
```
👤 Usuario se registra
    ↓
🎯 CRM crea perfil automático
    ↓
📈 Rastrea todas sus compras
    ↓
⭐ Asigna nivel (BRONCE, PLATA, ORO, PLATINUM)
    ↓
🎁 Otorga puntos de fidelidad
```

### 📧 **Envía Campañas de Marketing Dirigidas**
```
🎯 Seleccionas un segmento de clientes
    ↓
📝 Creas una campaña de email
    ↓
📧 Envías emails personalizados masivos
    ↓
📊 Mides resultados (aperturas, clics, ventas)
    ↓
💰 Calculas ROI automáticamente
```

### 🔍 **Segmenta Clientes Inteligentemente**
```
Crea grupos automáticos como:
- 🥇 Clientes VIP (>$1,000,000 en compras)
- 😴 Clientes inactivos (60+ días sin comprar)
- 🆕 Clientes nuevos (primera compra)
- 🏆 Compradores frecuentes (10+ pedidos)
- 🍕 Fans de categoría (ej: pizza lovers)
```

---

## ⚙️ ¿Cómo funciona automáticamente? {#cómo-funciona}

### 🔄 **Integración Automática con el Sistema Existente**

#### Cuando un USUARIO se registra:
```
1. Usuario completa registro
2. CRM crea perfil de cliente automáticamente
3. Cliente comienza en nivel BRONCE con 0 puntos
4. Puede recibir emails de marketing (si acepta)
```

#### Cuando se completa un PEDIDO:
```
1. Usuario hace un pedido de $50,000
2. CRM actualiza estadísticas:
   - Valor total compras: +$50,000
   - Número de pedidos: +1
   - Puntos de fidelidad: +50 pts (1 punto por cada $1000)
3. CRM verifica si sube de nivel:
   - $0-$200k      → BRONCE 🥉
   - $200k-$500k   → PLATA  🥈
   - $500k-$1M     → ORO    🥇
   - $1M+          → PLATINUM 💎
4. CRM actualiza segmentos automáticamente
```

#### Tareas Automáticas Programadas:
```
⏰ Cada 5 minutos:
   → Envía emails de campañas programadas

⏰ Cada hora:
   → Reintenta emails fallidos

⏰ Cada día a las 2 AM:
   → Actualiza todos los segmentos
   → Recalcula niveles de clientes
```

---

## 🌟 Características Principales {#características}

### 1️⃣ **Dashboard de Marketing** 📊
**URL**: `http://localhost:8092/crm/dashboard`

**Muestra:**
- 👥 Total de clientes
- ✅ Clientes activos
- 💰 Valor total de ventas
- 🎫 Ticket promedio
- 📧 Campañas activas
- 📈 ROI total
- 🆕 Clientes nuevos este mes
- 😴 Clientes inactivos

**Gráficos:**
- 📊 Distribución de clientes por nivel
- 📈 Evolución de ventas
- 🎯 Rendimiento de campañas

---

### 2️⃣ **Gestión de Clientes** 👥
**URL**: `http://localhost:8092/crm/clientes`

**Funciones:**
- ✅ Ver lista completa de clientes
- 🔍 Buscar por nombre, email, nivel
- 📊 Ver estadísticas detalladas:
  - Valor total de compras
  - Número de pedidos
  - Ticket promedio
  - Última compra
  - Puntos de fidelidad
  - Nivel actual
- 🎯 Filtrar por segmento
- 📝 Editar preferencias de marketing

**Ejemplo de Cliente:**
```
👤 Diego Vargas
📧 diego@unieats.com
💎 Nivel: PLATINUM
💰 Total compras: $1,500,000
🛒 Pedidos: 45
🎫 Ticket promedio: $33,333
⭐ Puntos: 1,500
📅 Última compra: Hace 3 días
✅ Acepta marketing: Sí
```

---

### 3️⃣ **Campañas de Marketing** 📧
**URL**: `http://localhost:8092/crm/campanas`

**Tipos de Campañas:**
- 📧 **EMAIL**: Emails masivos personalizados
- 📱 **SMS**: Mensajes de texto (preparado para integración)
- 🔔 **PUSH**: Notificaciones push (preparado)
- 🎁 **PROMOCION**: Ofertas especiales

**Estados de Campaña:**
```
📝 BORRADOR → En diseño, no se envía
📅 PROGRAMADA → Agendada para envío
✅ ACTIVA → Enviándose actualmente
⏸️ PAUSADA → Temporalmente detenida
🏁 FINALIZADA → Completada, con métricas finales
```

**Métricas que Rastrea:**
- 📊 **Envíos totales**: Cuántos emails se enviaron
- 👁️ **Tasa de apertura**: % que abrieron el email
- 🖱️ **Tasa de clics**: % que hicieron clic
- 🛒 **Conversiones**: Cuántas ventas generó
- 💰 **Ingresos generados**: Total de ventas
- 💹 **ROI**: (Ingresos - Costo) / Costo × 100

**Ejemplo de Campaña:**
```
📧 Campaña: "Promoción Black Friday"
🎯 Segmento: Clientes VIP (250 personas)
📅 Fecha: 2025-11-29
💰 Presupuesto: $500,000

Resultados:
✅ Enviados: 250
👁️ Aperturas: 175 (70%)
🖱️ Clics: 87 (35%)
🛒 Conversiones: 23 (9%)
💰 Ingresos: $2,300,000
💹 ROI: 360%
```

---

### 4️⃣ **Segmentación** 🎯
**URL**: `http://localhost:8092/crm/segmentos`

**Segmentos Automáticos Creados:**

#### Por Nivel:
```
🥉 Clientes BRONCE (nuevos)
🥈 Clientes PLATA (regulares)
🥇 Clientes ORO (frecuentes)
💎 Clientes PLATINUM (VIP)
```

#### Por Actividad:
```
✅ Clientes Activos (compra <30 días)
😴 Clientes Inactivos (>60 días sin comprar)
```

#### Por Valor:
```
💎 Clientes VIP (>$1M en compras)
🏆 Alto Valor ($500k-$1M)
📊 Valor Medio ($200k-$500k)
🆕 Nuevos (<$200k)
```

#### Por Frecuencia:
```
🔥 Super Frecuentes (20+ pedidos)
🏆 Compradores Frecuentes (10-19 pedidos)
👤 Compradores Ocasionales (3-9 pedidos)
🆕 Primera Compra (1-2 pedidos)
```

#### Personalizados:
```
🍕 Fans de Pizza
🍔 Fans de Hamburguesas
🥗 Fans de Saludable
🍰 Fans de Postres
```

**Criterios JSON Personalizables:**
```json
{
  "nivel": "ORO",
  "valorMinimo": 500000,
  "pedidosMinimos": 10,
  "categoriaFavorita": "Pizza"
}
```

---

### 5️⃣ **Templates de Email** 📝

**Templates Incluidos:**
1. **Bienvenida VIP** 💎
   - Para clientes que alcanzan PLATINUM
   - Incluye puntos y beneficios exclusivos

2. **Carrito Abandonado** 🛒
   - Recordatorio automático
   - Incluye descuento del 5%

3. **Promoción Mensual** 🎁
   - Template genérico para ofertas
   - Personalizable con variables

**Variables Disponibles:**
```html
{nombre}      → Diego
{apellido}    → Vargas
{correo}      → diego@unieats.com
{nivel}       → PLATINUM
{puntos}      → 1,500
{valorTotal}  → $1,500,000
{descuento}   → 10%
```

---

## 🧪 Cómo Probarlo PASO A PASO {#cómo-probarlo}

### 📋 **PRUEBA 1: Ver el Dashboard de CRM**

#### Paso 1: Crear un usuario con rol MARKETING o ADMIN
```sql
-- Ejecutar en tu base de datos (Supabase)

-- Crear rol de marketing si no existe
INSERT INTO roles (nombre) 
VALUES ('ROLE_MARKETING') 
ON CONFLICT DO NOTHING;

-- Asignar rol MARKETING a tu usuario
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'dvdavid2509vargs@gmail.com'  -- Tu email
AND r.nombre = 'ROLE_MARKETING'
ON CONFLICT DO NOTHING;
```

#### Paso 2: Iniciar la aplicación
```powershell
.\mvnw.cmd spring-boot:run
```

#### Paso 3: Login
```
URL: http://localhost:8092/login
Usuario: dvdavid2509vargs@gmail.com
Contraseña: [tu contraseña]
```

#### Paso 4: Acceder al Dashboard CRM
```
URL: http://localhost:8092/crm/dashboard
```

**Verás:**
- 📊 Cards con estadísticas generales
- 📈 Gráficos de distribución de clientes
- 💰 Métricas de ventas
- 📧 Estado de campañas

---

### 📋 **PRUEBA 2: Crear Perfiles de Cliente Automáticos**

#### Paso 1: Registrar un nuevo usuario
```
URL: http://localhost:8092/registro

Datos:
- Nombre: Juan
- Apellido: Pérez
- Email: juan@test.com
- Contraseña: test123
```

#### Paso 2: Hacer un pedido con ese usuario
```
1. Login como juan@test.com
2. Navegar tiendas
3. Agregar productos al carrito
4. Completar pedido por $50,000
```

#### Paso 3: Ver el perfil CRM creado
```
URL: http://localhost:8092/crm/clientes

Buscar: Juan Pérez

Verás:
- ✅ Perfil creado automáticamente
- 🥉 Nivel: BRONCE
- 💰 Valor total: $50,000
- 🛒 Pedidos: 1
- ⭐ Puntos: 50 (1 punto por cada $1000)
```

---

### 📋 **PRUEBA 3: Crear y Enviar una Campaña de Email**

#### Paso 1: Crear un segmento
```
URL: http://localhost:8092/crm/segmentos

1. Clic en "Crear Segmento"
2. Nombre: "Clientes de Prueba"
3. Descripción: "Todos los clientes para testing"
4. Tipo: MANUAL
5. Guardar
```

#### Paso 2: Crear template de email
```
URL: http://localhost:8092/api/email-templates

POST JSON:
{
  "nombre": "Oferta Especial",
  "asunto": "¡Descuento del 20% para ti!",
  "contenidoHtml": "<h1>¡Hola {nombre}!</h1><p>Tienes un descuento del 20%</p>",
  "activo": true
}
```

#### Paso 3: Crear campaña
```
URL: http://localhost:8092/crm/campanas

1. Clic en "Nueva Campaña"
2. Datos:
   - Nombre: "Promoción de Prueba"
   - Tipo: EMAIL
   - Segmento: "Clientes de Prueba"
   - Template: "Oferta Especial"
   - Presupuesto: $100,000
   - Fecha inicio: Hoy
   - Fecha fin: En 7 días
3. Guardar
```

#### Paso 4: Activar campaña
```
1. En la lista de campañas
2. Clic en "Activar" en tu campaña
3. El sistema enviará emails automáticamente
```

#### Paso 5: Ver resultados
```
1. Esperar 5 minutos (el scheduler envía cada 5 min)
2. Refrescar la página de campañas
3. Ver métricas actualizadas:
   - Envíos totales
   - Tasa de apertura
   - Tasa de clics
   - Conversiones
```

---

### 📋 **PRUEBA 4: Segmentación Automática**

#### Paso 1: Crear varios usuarios y pedidos
```
Usuario 1: $100,000 en compras (1 pedido)
Usuario 2: $300,000 en compras (5 pedidos)
Usuario 3: $600,000 en compras (15 pedidos)
Usuario 4: $1,200,000 en compras (30 pedidos)
```

#### Paso 2: Ejecutar segmentación automática
```
URL: http://localhost:8092/api/segmentacion/actualizar-todos

O esperar hasta las 2 AM (se ejecuta automáticamente)
```

#### Paso 3: Ver segmentos creados
```
URL: http://localhost:8092/crm/segmentos

Verás segmentos automáticos:
- 🥉 Clientes BRONCE: Usuario 1
- 🥈 Clientes PLATA: Usuario 2
- 🥇 Clientes ORO: Usuario 3
- 💎 Clientes PLATINUM: Usuario 4
```

---

### 📋 **PRUEBA 5: Sistema de Puntos y Niveles**

#### Paso 1: Hacer pedidos progresivos
```
Usuario: test@unieats.com

Pedido 1: $50,000
→ Puntos: 50
→ Nivel: BRONCE 🥉

Pedido 2: $50,000 (Total: $100,000)
→ Puntos: 100
→ Nivel: BRONCE 🥉

Pedido 3: $150,000 (Total: $250,000)
→ Puntos: 250
→ Nivel: PLATA 🥈 ← ¡SUBIÓ DE NIVEL!

Pedido 4: $300,000 (Total: $550,000)
→ Puntos: 550
→ Nivel: ORO 🥇 ← ¡SUBIÓ DE NIVEL!

Pedido 5: $500,000 (Total: $1,050,000)
→ Puntos: 1,050
→ Nivel: PLATINUM 💎 ← ¡MÁXIMO NIVEL!
```

#### Paso 2: Verificar en el perfil
```
URL: http://localhost:8092/crm/clientes

Buscar: test@unieats.com

Verás:
- 💎 Nivel: PLATINUM
- ⭐ Puntos: 1,050
- 💰 Total compras: $1,050,000
- 🛒 Pedidos: 5
- 🎫 Ticket promedio: $210,000
```

---

## 🌐 URLs y Accesos Completos {#urls}

### 🎨 **Interfaces Web (Thymeleaf)**
```
📊 Dashboard CRM:
http://localhost:8092/crm/dashboard

👥 Gestión de Clientes:
http://localhost:8092/crm/clientes

📧 Gestión de Campañas:
http://localhost:8092/crm/campanas

🎯 Gestión de Segmentos:
http://localhost:8092/crm/segmentos
```

### 🔌 **APIs REST (JSON)**

#### Clientes:
```
GET    /api/clientes                    → Listar todos
GET    /api/clientes/{id}               → Ver detalle
GET    /api/clientes/email/{email}      → Buscar por email
GET    /api/clientes/nivel/{nivel}      → Filtrar por nivel
GET    /api/clientes/activos            → Solo activos
GET    /api/clientes/inactivos          → Solo inactivos
POST   /api/clientes/{id}/actualizar-estadisticas → Actualizar
```

#### Campañas:
```
GET    /api/campanas                    → Listar todas
POST   /api/campanas                    → Crear nueva
GET    /api/campanas/{id}               → Ver detalle
PUT    /api/campanas/{id}               → Actualizar
DELETE /api/campanas/{id}               → Eliminar
POST   /api/campanas/{id}/activar       → Activar
POST   /api/campanas/{id}/pausar        → Pausar
POST   /api/campanas/{id}/finalizar     → Finalizar
GET    /api/campanas/activas            → Solo activas
GET    /api/campanas/segmento/{id}      → Por segmento
```

#### Segmentos:
```
GET    /api/segmentos                   → Listar todos
POST   /api/segmentos                   → Crear nuevo
GET    /api/segmentos/{id}              → Ver detalle
PUT    /api/segmentos/{id}              → Actualizar
DELETE /api/segmentos/{id}              → Eliminar
GET    /api/segmentos/{id}/clientes     → Clientes del segmento
POST   /api/segmentacion/crear-segmento-nivel/{nivel} → Auto por nivel
POST   /api/segmentacion/crear-segmento-actividad → Auto por actividad
POST   /api/segmentacion/crear-segmento-frecuencia → Auto por frecuencia
POST   /api/segmentacion/actualizar-todos → Actualizar todos
```

#### Email Marketing:
```
POST   /api/email-marketing/enviar-campana/{campaniaId} → Enviar campaña
POST   /api/email-marketing/enviar-test → Enviar email de prueba
```

#### Estadísticas:
```
GET    /api/estadisticas/generales      → KPIs generales
GET    /api/estadisticas/campanas       → Rendimiento de campañas
GET    /api/estadisticas/clientes-por-nivel → Distribución
```

---

## 🎯 Ejemplo Completo de Flujo

### Escenario: "Campaña de Reactivación de Clientes Inactivos"

#### Paso 1: El sistema detecta clientes inactivos (automático)
```
→ Cada día a las 2 AM se ejecuta la segmentación
→ Se crea/actualiza el segmento "Clientes Inactivos"
→ Incluye clientes sin compras en 60+ días
```

#### Paso 2: Crear campaña de reactivación
```
1. Login como ADMIN o MARKETING
2. Ir a: http://localhost:8092/crm/campanas
3. Clic "Nueva Campaña"

Datos:
- Nombre: "Te extrañamos - 20% OFF"
- Tipo: EMAIL
- Segmento: "Clientes Inactivos"
- Template: "Carrito Abandonado" (modificado)
- Presupuesto: $200,000
- Fecha inicio: Hoy
- Fecha fin: +14 días
```

#### Paso 3: Personalizar el email
```
Asunto: "¡Te extrañamos {nombre}! 🎁 20% de descuento"

Contenido:
<h1>¡Hola {nombre}!</h1>
<p>Hace {diasInactivo} días que no te vemos.</p>
<p>Tenemos un descuento especial del 20% para ti.</p>
<p>Tus puntos actuales: {puntos} ⭐</p>
<a href="http://localhost:8092/">Ver ofertas</a>
```

#### Paso 4: Activar campaña
```
Clic en "Activar"
→ Estado cambia a ACTIVA
→ En máximo 5 minutos comienza el envío
```

#### Paso 5: El scheduler envía los emails
```
⏰ A los 5 minutos:
→ CRMSchedulerService detecta campaña ACTIVA
→ EmailMarketingService envía emails
→ Se crea NotificacionMarketing por cada envío
```

#### Paso 6: Tracking automático
```
Cuando un cliente:
- Abre el email → Tasa de apertura +1
- Hace clic → Tasa de clics +1
- Hace una compra → Conversiones +1, Ingresos += monto
```

#### Paso 7: Ver resultados
```
URL: http://localhost:8092/crm/campanas

Métricas en tiempo real:
✅ Enviados: 150
👁️ Aperturas: 105 (70%)
🖱️ Clics: 52 (35%)
🛒 Conversiones: 12 (8%)
💰 Ingresos: $1,200,000
💹 ROI: 500%
```

---

## 🔐 Roles y Permisos

### Roles requeridos:
- 👑 **ADMIN_PLATAFORMA**: Acceso completo
- 📊 **MARKETING**: Acceso completo a CRM

### Cómo asignar:
```sql
-- Ver roles existentes
SELECT * FROM roles;

-- Asignar rol MARKETING a un usuario
INSERT INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuarios u, roles r
WHERE u.email = 'tu-email@ejemplo.com'
AND r.nombre = 'ROLE_MARKETING';
```

---

## 💡 Tips y Mejores Prácticas

### ✅ DO (Hacer):
1. ✅ Ejecuta `init-crm-data.sql` para datos iniciales
2. ✅ Crea segmentos ANTES de crear campañas
3. ✅ Prueba con emails reales tuyos primero
4. ✅ Revisa métricas después de cada campaña
5. ✅ Actualiza segmentos regularmente

### ❌ DON'T (No hacer):
1. ❌ No envíes campañas sin probar el template
2. ❌ No uses segmentos vacíos
3. ❌ No actives múltiples campañas simultáneas (al inicio)
4. ❌ No ignores las métricas de ROI
5. ❌ No olvides pausar campañas finalizadas

---

## 🐛 Solución de Problemas

### Problema: "No veo el menú CRM"
**Solución:** Verifica que tengas rol ADMIN o MARKETING

### Problema: "Los emails no se envían"
**Solución:** 
1. Verifica configuración SMTP en `application.properties`
2. Revisa logs: busca "EmailMarketingService"
3. Verifica que la campaña esté en estado ACTIVA

### Problema: "Los segmentos están vacíos"
**Solución:**
1. Verifica que existan clientes en la BD
2. Ejecuta: `POST /api/segmentacion/actualizar-todos`
3. Revisa los criterios del segmento

### Problema: "Las métricas no se actualizan"
**Solución:**
1. Verifica que el tracking esté habilitado
2. Usa `?track=true` en los links de email
3. Revisa tabla `notificaciones_marketing`

---

## 📞 Comandos Útiles

### Iniciar aplicación:
```powershell
.\mvnw.cmd spring-boot:run
```

### Ejecutar SQL de inicialización:
```sql
-- Conectar a Supabase y ejecutar:
\i sql/init-crm-data.sql
```

### Ver logs de CRM:
```powershell
# En la consola busca:
"CRM"
"Email"
"Segmentacion"
"Campana"
```

---

## 🎉 ¡Listo para Empezar!

### Checklist de Inicio:
- [ ] ✅ Aplicación corriendo
- [ ] ✅ Usuario con rol MARKETING creado
- [ ] ✅ Login exitoso
- [ ] ✅ Dashboard CRM visible
- [ ] ✅ Al menos 1 cliente de prueba creado
- [ ] ✅ Segmentos generados
- [ ] ✅ Primera campaña de prueba creada

### Primer Test Recomendado:
```
1. Crear usuario de prueba
2. Hacer pedido de $100,000
3. Ver perfil CRM creado automáticamente
4. Crear segmento "Todos los clientes"
5. Crear campaña de email de prueba
6. Enviar a ti mismo
7. Verificar recepción
8. Revisar métricas
```

---

**¡El sistema CRM está completamente funcional y listo para usar! 🚀**

Para cualquier duda, revisa:
- `CRM_MARKETING_README.md` - Documentación técnica completa
- `CRM_RESUMEN_EJECUTIVO.md` - Resumen ejecutivo
- Este archivo - Guía práctica paso a paso
