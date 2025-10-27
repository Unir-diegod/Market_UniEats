# Sistema CRM de Marketing - UniEats

## 📋 Descripción General

Se ha agregado un sistema completo de **Customer Relationship Management (CRM)** al proyecto UniEats Marketplace para potenciar las capacidades de marketing, segmentación de clientes y automatización de campañas.

## 🎯 Funcionalidades Principales

### 1. **Gestión de Clientes**
- Perfiles automáticos de cliente vinculados a usuarios
- Tracking de comportamiento de compra
- Sistema de niveles: BRONCE, PLATA, ORO, PLATINUM
- Programa de puntos de fidelidad
- Preferencias de marketing (email, SMS)
- Historial completo de interacciones

### 2. **Segmentación Inteligente**
- Segmentación automática por:
  - Nivel de cliente
  - Actividad (activos/inactivos)
  - Frecuencia de compra
  - Valor total de compras (VIP)
  - Categoría favorita
- Actualización automática de segmentos
- Criterios personalizables

### 3. **Campañas de Marketing**
- Creación de campañas dirigidas
- Múltiples tipos: EMAIL, SMS, PUSH, PROMOCION
- Estados: BORRADOR, PROGRAMADA, ACTIVA, PAUSADA, FINALIZADA
- Métricas en tiempo real:
  - Tasa de apertura
  - Tasa de clics
  - Conversiones
  - ROI (Return on Investment)

### 4. **Email Marketing**
- Sistema de templates personalizables
- Variables dinámicas: {nombre}, {puntos}, {nivel}, etc.
- Envío programado
- Tracking de aperturas y clics
- Reintentos automáticos en caso de fallos

### 5. **Análisis y Estadísticas**
- Dashboard completo con KPIs
- Análisis de clientes por nivel
- Rendimiento de campañas
- Historial de interacciones
- Estadísticas de conversión

## 📊 Estructura del Sistema

### Entidades Principales

#### Cliente
```java
- Información de perfil
- Estadísticas de compra
- Preferencias de marketing
- Sistema de puntos y niveles
```

#### Segmento
```java
- Nombre y descripción
- Criterios de segmentación
- Lista de clientes
- Campañas asociadas
```

#### Campaña
```java
- Información básica
- Segmento objetivo
- Template de email
- Métricas de rendimiento
```

#### InteraccionCliente
```java
- Tipo de interacción
- Canal utilizado
- Fecha y descripción
- Valor monetario
```

#### EmailTemplate
```java
- Plantillas HTML
- Variables personalizables
- Categorización
```

#### NotificacionMarketing
```java
- Registro de envíos
- Estados de entrega
- Tracking de apertura/clics
```

## 🔧 Servicios Implementados

### ClienteService
- Creación y actualización de perfiles
- Cálculo de niveles automático
- Gestión de puntos de fidelidad
- Actualización de estadísticas

### CampanaService
- CRUD de campañas
- Gestión de estados
- Actualización de métricas
- Cálculo de ROI

### SegmentacionService
- Creación de segmentos manuales
- Segmentación automática
- Actualización periódica
- Filtrado de clientes

### EmailMarketingService
- Envío masivo de emails
- Personalización de contenido
- Tracking de interacciones
- Gestión de templates

### EstadisticasCRMService
- Recopilación de KPIs
- Análisis de comportamiento
- Registro de interacciones
- Generación de reportes

### CRMIntegracionService
- Sincronización automática con pedidos
- Registro de visitas a productos
- Detección de carritos abandonados
- Actualización de categorías favoritas

### CRMSchedulerService
- Envío automático de emails pendientes (cada 5 min)
- Reintentos de envíos fallidos (cada hora)
- Actualización de segmentos (diaria a las 2 AM)

## 🌐 Endpoints API REST

### Clientes
```
GET  /crm/clientes              - Listar todos
GET  /crm/clientes/api/activos  - Clientes activos
GET  /crm/clientes/api/inactivos - Clientes inactivos
GET  /crm/clientes/api/top      - Top clientes
POST /crm/clientes/api/{id}/preferencias - Actualizar preferencias
```

### Campañas
```
GET    /crm/campanas             - Listar todas
GET    /crm/campanas/api         - API: Listar todas
POST   /crm/campanas/crear       - Crear campaña
POST   /crm/campanas/api/{id}/activar - Activar
POST   /crm/campanas/api/{id}/pausar  - Pausar
POST   /crm/campanas/api/{id}/finalizar - Finalizar
DELETE /crm/campanas/api/{id}    - Eliminar
```

### Segmentos
```
GET  /crm/segmentos                     - Listar todos
POST /crm/segmentos/api/segmentar-por-nivel - Por nivel
POST /crm/segmentos/api/segmentar-activos   - Activos
POST /crm/segmentos/api/segmentar-inactivos - Inactivos
POST /crm/segmentos/api/segmentar-frecuentes - Frecuentes
POST /crm/segmentos/api/segmentar-vip        - VIP
```

### Dashboard
```
GET /crm/dashboard                         - Vista principal
GET /crm/dashboard/api/estadisticas        - Estadísticas
GET /crm/dashboard/api/interacciones-por-tipo - Análisis
```

## 🎨 Vistas Disponibles

### Dashboard Principal
`/crm/dashboard`
- KPIs principales
- Gráficos de distribución
- Resumen de campañas

### Gestión de Clientes
`/crm/clientes`
- Lista completa de clientes
- Filtros por actividad y nivel
- Acceso a perfiles detallados

### Gestión de Campañas
`/crm/campanas`
- Lista de campañas activas
- Métricas en tiempo real
- Controles de estado

### Segmentación
`/crm/segmentos`
- Segmentos existentes
- Herramientas de segmentación automática
- Análisis de distribución

## 🔄 Integración Automática

El sistema se integra automáticamente con:

1. **Registro de Usuarios**: Crea perfil de cliente automáticamente
2. **Pedidos**: Actualiza estadísticas y registra interacciones
3. **Navegación**: Tracking de visitas a productos
4. **Carritos Abandonados**: Detección y registro
5. **Categorías**: Actualización de preferencias

## 📧 Sistema de Niveles

| Nivel | Valor Mínimo de Compras | Beneficios |
|-------|-------------------------|------------|
| BRONCE | $0 | Cliente básico |
| PLATA | $200,000 | Cliente frecuente |
| ORO | $500,000 | Cliente premium |
| PLATINUM | $1,000,000 | Cliente VIP |

## 💎 Puntos de Fidelidad

- **1 punto** por cada $1,000 gastados
- Acumulación automática en cada compra
- Base para futuras campañas de recompensas

## 🔐 Control de Acceso

Todas las funciones de CRM requieren rol **ADMIN** o **MARKETING**:
```java
@PreAuthorize("hasAnyRole('ADMIN', 'MARKETING')")
```

## 📅 Tareas Programadas

### Envío de Emails
- **Frecuencia**: Cada 5 minutos
- **Función**: Procesar cola de emails pendientes

### Reintentos
- **Frecuencia**: Cada hora
- **Función**: Reintentar envíos fallidos (máx 3 intentos)

### Actualización de Segmentos
- **Frecuencia**: Diaria a las 2:00 AM
- **Función**: Recalcular todos los segmentos automáticos

## 🚀 Cómo Usar

### 1. Acceder al Dashboard
```
http://localhost:8080/crm/dashboard
```

### 2. Crear un Segmento
- Ve a "Segmentos"
- Usa botones de segmentación automática
- O crea uno manual con criterios personalizados

### 3. Crear una Campaña
- Ve a "Campañas" → "Nueva Campaña"
- Selecciona un segmento objetivo
- Elige un template de email
- Define fechas y objetivos
- Activa la campaña

### 4. Monitorear Resultados
- Dashboard principal muestra KPIs generales
- Detalle de campaña muestra métricas específicas
- Perfil de cliente muestra historial completo

## 📈 Métricas Clave

- **Tasa de Apertura**: % de emails abiertos
- **Tasa de Clics**: % de clics en emails
- **Conversión**: Clientes que realizaron compra
- **ROI**: (Ingresos - Inversión) / Inversión * 100

## 🛠️ Configuración Adicional

### Variables de Entorno para Email
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 📦 Archivos Creados

### Entidades (6)
- `Cliente.java`
- `Segmento.java`
- `Campana.java`
- `InteraccionCliente.java`
- `EmailTemplate.java`
- `NotificacionMarketing.java`

### Repositorios (6)
- `ClienteRepository.java`
- `SegmentoRepository.java`
- `CampanaRepository.java`
- `InteraccionClienteRepository.java`
- `EmailTemplateRepository.java`
- `NotificacionMarketingRepository.java`

### Servicios (6)
- `ClienteService.java`
- `CampanaService.java`
- `SegmentacionService.java`
- `EmailMarketingService.java`
- `EstadisticasCRMService.java`
- `CRMIntegracionService.java`
- `CRMSchedulerService.java`

### Controladores (4)
- `ClienteController.java`
- `CampanaController.java`
- `SegmentacionController.java`
- `DashboardCRMController.java`

### DTOs (5)
- `ClienteDTO.java`
- `CampanaDTO.java`
- `CampanaRequest.java`
- `SegmentoDTO.java`
- `EstadisticasCRMDTO.java`

### Vistas (4)
- `crm/dashboard.html`
- `crm/campanas/lista.html`
- `crm/clientes/lista.html`
- `crm/segmentos/lista.html`

## ✅ Próximos Pasos Recomendados

1. **Configurar servidor SMTP** para emails reales
2. **Crear templates HTML** personalizados para diferentes tipos de campañas
3. **Agregar gráficos** con Chart.js en el dashboard
4. **Implementar notificaciones SMS** usando Twilio
5. **Crear reportes exportables** en PDF/Excel
6. **Agregar A/B testing** para campañas
7. **Implementar webhooks** para tracking avanzado

## 📚 Documentación Adicional

Para más detalles sobre cada componente, consultar los JavaDocs en el código fuente.

---

**Desarrollado para UniEats Marketplace**  
Sistema CRM completo con marketing automation 🚀
