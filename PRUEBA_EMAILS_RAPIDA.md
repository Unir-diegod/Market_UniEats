# 🚀 GUÍA RÁPIDA: PROBAR EL SISTEMA DE EMAILS

## ✅ Cambios Realizados

### 1. Contraseña Actualizada ✓
```
Ubicación: src/main/resources/application.properties
Nueva contraseña: qiws yptl afef tzmq
Estado: ACTIVA
```

### 2. Logging Mejorado ✓
```
EmailService ahora muestra:
  - 📧 Detalles del email
  - ✉️ Quién envía y a quién
  - 📋 Errores específicos
  - 🔧 Stack traces completos
```

### 3. Endpoints de Prueba Creados ✓
```
5 nuevos endpoints en /api/email-test/*
Todos devuelven JSON con estado y detalles
```

---

## 🧪 PRUEBAS PASO A PASO

### PASO 1: Reiniciar la Aplicación

```powershell
# Si está corriendo, presionar Ctrl+C

# Navegar a la carpeta del proyecto
cd "c:\Users\jero\Downloads\uni-eats-marketplace for diego\uni-eats-marketplace-main"

# Iniciar
.\mvnw.cmd spring-boot:run
```

**Esperar a que diga: "Started MarketplaceApplication"**

---

### PASO 2: Verificar Configuración

```
URL: http://localhost:8092/api/email-test/config

Deberías ver JSON con los datos correctos
```

---

### PASO 3: Enviar Email de Prueba

**Opción A: Simple (Recomendado para primer test)**

```
http://localhost:8092/api/email-test/enviar-texto?email=TU_EMAIL@gmail.com
```

Cambiar `TU_EMAIL@gmail.com` por tu correo personal

**Respuesta esperada:**
```json
{
  "estado": "éxito",
  "mensaje": "Email de prueba básico enviado correctamente"
}
```

---

**Opción B: Con Plantilla HTML**

```
http://localhost:8092/api/email-test/enviar-simple?email=TU_EMAIL@gmail.com&nombre=Diego
```

---

### PASO 4: Revisar Bandeja

1. Abrir Gmail
2. Revisar **Bandeja de Entrada**
3. Si no está, revisar **Spam** o **Promociones**

**Email esperado:**
- **De:** UniEats Marketplace (dvdavid2509vargs@gmail.com)
- **Asunto:** ¡Bienvenido a UniEats! 🍔
- **Contenido:** Hermoso email con logo y características

---

### PASO 5: Revisar Logs en Consola

Buscar líneas como:

**✅ Si funcionó:**
```
✅ Email enviado exitosamente a: tu@email.com
```

**❌ Si falló:**
```
❌ ERROR AL ENVIAR EMAIL
📍 Tipo de error: ...
📋 Mensaje de error: ...
```

---

## 📊 ÁRBOL DE DECISIÓN RÁPIDO

```
¿El endpoint devuelve "éxito"?
│
├─ SÍ ✅
│  └─ ¿Llegó el email a tu bandeja?
│     │
│     ├─ SÍ ✅ → FUNCIONA PERFECTAMENTE
│     │
│     └─ NO ❌
│        ├─ Revisar SPAM/Promociones
│        ├─ Revisar filtros de Gmail
│        └─ Problema: Email llega a SPAM (normal con cuentas nuevas)
│
└─ NO ❌
   └─ Ver error en JSON
      └─ Buscar solución en DIAGNOSTICO_EMAILS.md
```

---

## 🔍 DECODIFICAR ERRORES COMUNES

### Error 1: "Username and Password not accepted"
```
Causa: Contraseña incorrecta
Solución: Generar nueva contraseña en Google AppPasswords
```

### Error 2: "Connection refused" o "Network is unreachable"
```
Causa: Problema de conectividad a Gmail
Solución: Verificar conexión a internet, firewall
```

### Error 3: "Template not found"
```
Causa: Plantilla HTML no existe
Solución: Verificar que existe src/main/resources/templates/emails/bienvenida.html
```

### Error 4: "No such provider: smtp"
```
Causa: Falta dependencia de correo
Solución: Verificar pom.xml tiene spring-boot-starter-mail
```

---

## 🎯 PRÓXIMA PRUEBA: LOGIN AUTOMÁTICO

Una vez que confirmes que el email funciona:

1. **Registrar un nuevo usuario**
   ```
   http://localhost:8092/registro
   ```

2. **Hacer login con ese usuario**
   ```
   http://localhost:8092/login
   ```

3. **Automáticamente deberías recibir email de bienvenida**

4. **Revisar bandeja**

---

## 🆘 SI SIGUE SIN FUNCIONAR

1. Lee el archivo: **DIAGNOSTICO_EMAILS.md**
2. Sigue el árbol de diagnóstico completo
3. Recopila información de errores
4. Comparte los logs de error

---

## ✨ BONUS: Probar Todos los Endpoints

### Test 1: Configuración
```
GET http://localhost:8092/api/email-test/config
```

### Test 2: Health Check
```
GET http://localhost:8092/api/email-test/health
```

### Test 3: Email Simple
```
GET http://localhost:8092/api/email-test/enviar-simple?email=test@gmail.com&nombre=Diego
```

### Test 4: Email Texto
```
GET http://localhost:8092/api/email-test/enviar-texto?email=test@gmail.com
```

### Test 5: Email Personalizado
```
POST http://localhost:8092/api/email-test/enviar-personalizado

Body:
{
  "email": "test@gmail.com",
  "asunto": "Mi Email",
  "plantilla": "emails/bienvenida",
  "variables": {
    "nombreUsuario": "Mi Nombre"
  }
}
```

---

## 📝 RESUMEN DE CAMBIOS

```
✅ Contraseña actualizada
✅ Logging mejorado (500+ líneas de diagnóstico)
✅ 5 endpoints de prueba creados
✅ Proyecto compilado sin errores
✅ Listo para testing

ESTADO: 🟢 LISTO PARA PROBAR
```

---

**¡Sigue estos pasos y el email funcionará! Si hay problemas, usa DIAGNOSTICO_EMAILS.md para resolverlos. 🚀**
