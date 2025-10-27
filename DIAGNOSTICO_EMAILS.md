# 📧 DIAGNÓSTICO Y SOLUCIÓN DE PROBLEMAS CON EMAILS

## 🔴 Problema: "No llegó el correo"

He actualizado la contraseña y creado herramientas de diagnóstico. Sigue estas instrucciones:

---

## ✅ PASO 1: Actualizar la Contraseña

### ✓ Ya está hecho:
```
Email: dvdavid2509vargs@gmail.com
Contraseña anterior: gmar nkdd zbrz ivna
Contraseña nueva: qiws yptl afef tzmq
Ubicación: src/main/resources/application.properties
```

---

## ✅ PASO 2: Recompilar y Reiniciar

```powershell
# Detener la aplicación (Ctrl+C si está corriendo)

# Limpiar y recompilar
cd "c:\Users\jero\Downloads\uni-eats-marketplace for diego\uni-eats-marketplace-main"
.\mvnw.cmd clean install -DskipTests

# Reiniciar
.\mvnw.cmd spring-boot:run
```

---

## ✅ PASO 3: Probar Envío de Correos

He creado 5 endpoints de prueba para diagnosticar el problema:

### 🧪 TEST 1: Verificar Configuración SMTP
```
GET http://localhost:8092/api/email-test/config
```

**Respuesta esperada:**
```json
{
  "estado": "conectado",
  "host": "smtp.gmail.com",
  "puerto": 587,
  "username": "dvdavid2509vargs@gmail.com",
  "autenticacion": true,
  "tls_enabled": true
}
```

---

### 🧪 TEST 2: Health Check del Servicio
```
GET http://localhost:8092/api/email-test/health
```

**Respuesta esperada:**
```json
{
  "servicio": "Email Service",
  "estado": "activo",
  "proveedor": "Gmail SMTP",
  "host": "smtp.gmail.com",
  "puerto": 587
}
```

---

### 🧪 TEST 3: Enviar Email de Prueba Simple

```
GET http://localhost:8092/api/email-test/enviar-simple?email=TU_EMAIL@gmail.com&nombre=Diego
```

**Cambiar:**
- `TU_EMAIL@gmail.com` por tu correo personal para pruebas

**Respuesta esperada:**
```json
{
  "estado": "éxito",
  "mensaje": "Email de bienvenida enviado a TU_EMAIL@gmail.com",
  "email": "TU_EMAIL@gmail.com",
  "nombre": "Diego"
}
```

**Si ves "éxito": ✅ El email se envió, revisa tu bandeja**

---

### 🧪 TEST 4: Enviar Email de Texto Básico

```
GET http://localhost:8092/api/email-test/enviar-texto?email=TU_EMAIL@gmail.com
```

**Este es el test más simple. Si este no funciona, hay un problema de conectividad SMTP**

**Respuesta esperada:**
```json
{
  "estado": "éxito",
  "mensaje": "Email de prueba básico enviado correctamente",
  "email": "TU_EMAIL@gmail.com"
}
```

---

### 🧪 TEST 5: Enviar Email Personalizado

**POST** a `http://localhost:8092/api/email-test/enviar-personalizado`

**Body (JSON):**
```json
{
  "email": "TU_EMAIL@gmail.com",
  "asunto": "🧪 Test Personalizado",
  "plantilla": "emails/bienvenida",
  "variables": {
    "nombreUsuario": "Diego Prueba"
  }
}
```

---

## 📊 ÁRBOL DE DIAGNÓSTICO

```
¿Llega el email?

├─ SÍ ✅
│  └─ El sistema funciona
│     └─ Ir a PASO 4
│
└─ NO ❌
   │
   ├─ ¿El endpoint devuelve "éxito"?
   │  │
   │  ├─ SÍ ✅
   │  │  └─ Error de Gmail:
   │  │     ├─ Revisar carpeta SPAM
   │  │     ├─ Verificar que la contraseña sea correcta
   │  │     └─ Ir a SOLUCIÓN 1
   │  │
   │  └─ NO ❌ (devuelve error)
   │     └─ Error de configuración SMTP:
   │        ├─ Verificar endpoint /config
   │        ├─ Ver logs en consola
   │        └─ Ir a SOLUCIÓN 2
   │
   └─ Revisar LOGS EN CONSOLA
      └─ Buscar mensaje de error específico
         └─ Ir a SOLUCIONES
```

---

## 🔧 SOLUCIONES SEGÚN EL ERROR

### SOLUCIÓN 1: Email llega a SPAM

**Causa:** Gmail clasifica el email como spam

**Soluciones:**
1. Marcar como "No es spam" en Gmail
2. Agregar a contactos
3. Cambiar la plantilla HTML para ser menos "promocional"
4. Aumentar reputación del dominio (proceso largo)

**Para testing:** Usar un email personal tuyo

---

### SOLUCIÓN 2: Error de Autenticación

**Error típico en logs:**
```
com.sun.mail.smtp.SMTPAuthenticationException: 535-5.7.8 Username and Password not accepted
```

**Causa:** Contraseña incorrecta

**Solución:**
```
1. Ir a: https://myaccount.google.com/apppasswords
2. Generar una NUEVA contraseña de aplicación
3. Copiar exactamente los 16 caracteres (sin espacios)
4. Actualizar en application.properties
5. Reiniciar aplicación
```

**Recuerda:** NO USES la contraseña normal de Gmail, usa una contraseña de APLICACIÓN

---

### SOLUCIÓN 3: Error de Conexión al Host

**Error típico en logs:**
```
java.net.ConnectException: Connection refused
java.net.SocketException: Network is unreachable
```

**Causa:** Problema de conectividad de red

**Soluciones:**
1. Verificar conexión a internet
2. Verificar que el puerto 587 no esté bloqueado
3. Cambiar puerto a 465 (SSL) si 587 no funciona:
```properties
spring.mail.port=465
spring.mail.properties.mail.smtp.socketFactory.port=465
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
```

---

### SOLUCIÓN 4: La Plantilla No Encuentra el Template

**Error típico en logs:**
```
org.thymeleaf.exceptions.TemplateInputException: An error occurred while trying to resolve the template
```

**Causa:** La plantilla HTML no existe en la ruta correcta

**Verificar:**
```
Debe existir: src/main/resources/templates/emails/bienvenida.html
```

**Si no existe:**
1. Crear la carpeta: `src/main/resources/templates/emails/`
2. Crear archivo `bienvenida.html`
3. Copiar contenido HTML válido

---

### SOLUCIÓN 5: JavaMailSender No Configurado

**Error típico en logs:**
```
org.springframework.beans.factory.UnsatisfiedDependencyException
```

**Causa:** Spring no encontró la configuración de email

**Verificar en application.properties:**
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=dvdavid2509vargs@gmail.com
spring.mail.password=qiws yptl afef tzmq
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

## 📋 LOGS QUE DEBES VER EN CONSOLA

### ✅ Logs CORRECTOS:
```
🧪 TEST: Enviando email simple de prueba a: test@gmail.com
🔧 Iniciando envío de email a: test@gmail.com
📧 Asunto: ¡Bienvenido a UniEats! 🍔
✉️ De: UniEats Marketplace <dvdavid2509vargs@gmail.com>
➜ Para: test@gmail.com
✅ Email enviado exitosamente a: test@gmail.com
```

### ❌ Logs de ERROR (que necesitas ver):
```
❌ ERROR AL ENVIAR EMAIL a test@gmail.com
📍 Tipo de error: com.sun.mail.smtp.SMTPAuthenticationException
📋 Mensaje de error: 535-5.7.8 Username and Password not accepted
```

---

## 🔍 CÓMO LEER LOS LOGS

### En la Consola de ejecución:

1. **Buscar errores:**
   - Ctrl+F (o Cmd+F en Mac)
   - Escribir: `ERROR`
   - Escribir: `Exception`
   - Escribir: `Failed`

2. **Ver detalles completos:**
   - Los logs mostrarán la causa exacta
   - Busca líneas que digan `Caused by:`

3. **Copiar y compartir:**
   - Selecciona todo el log relevante
   - Cópialo para diagnóstico

---

## 🎯 PLAN DE ACCIÓN RECOMENDADO

### Paso 1: Probar la Configuración
```
1. Ejecuta: GET /api/email-test/config
2. Verifica que todos los valores sean correctos
3. Si hay error, revisa application.properties
```

### Paso 2: Probar Conectividad
```
1. Ejecuta: GET /api/email-test/health
2. Debe responder "activo"
3. Si falla, hay problema de conectividad SMTP
```

### Paso 3: Enviar Email de Prueba
```
1. Ejecuta: GET /api/email-test/enviar-texto?email=TU_EMAIL@gmail.com
2. Busca en logs mensajes de éxito o error
3. Revisa tu bandeja (incluyendo SPAM/Promociones)
```

### Paso 4: Si Todo Funciona
```
1. Realiza login en la aplicación
2. Deberías recibir email de bienvenida automáticamente
3. Si no llega, revisa SPAM
```

---

## 📧 VERIFICAR EN GMAIL

### Si los tests dicen "éxito" pero no recibes email:

1. **Revisar carpetas:**
   - Bandeja de entrada ✓
   - Spam/Correo no deseado ✓
   - Otras carpetas ✓

2. **Revisar filtros:**
   - Settings → Filters and Blocked Addresses
   - Ver si hay regla bloqueando dvdavid2509vargs@gmail.com

3. **Revisar notificaciones:**
   - Algunos emails van a "Promociones" por defecto
   - Marcar como importante para futuras

4. **Generar nueva contraseña:**
   - Si los tests fallan con autenticación
   - Ir a: https://myaccount.google.com/apppasswords
   - Generar nueva contraseña de 16 caracteres

---

## 🔐 CHECKLIST DE SEGURIDAD

- [ ] Contraseña de aplicación usada (NO contraseña principal)
- [ ] Dos factores habilitado en Google Account
- [ ] Acceso de aplicaciones menos seguras: NO (usar app password)
- [ ] Contraseña nunca incluida en repositorio git
- [ ] Logs mostrando autenticación exitosa

---

## 🆘 INFORMACIÓN PARA SOPORTE

Si necesitas ayuda, recopila esta información:

1. **Error en logs:**
   ```
   [Copiar línea completa de error]
   ```

2. **Respuesta del endpoint /config:**
   ```json
   [Copiar JSON completo]
   ```

3. **Resultado de /api/email-test/enviar-texto:**
   ```json
   [Copiar JSON completo]
   ```

4. **¿De qué dirección enviaste el test?**
   ```
   [Tu email]
   ```

5. **¿Llegó a spam o no llegó en absoluto?**
   ```
   [Respuesta]
   ```

---

## 📞 URLS DE PRUEBA RÁPIDA

```
Configuración:
http://localhost:8092/api/email-test/config

Health Check:
http://localhost:8092/api/email-test/health

Enviar Simple:
http://localhost:8092/api/email-test/enviar-simple?email=test@gmail.com&nombre=Diego

Enviar Texto:
http://localhost:8092/api/email-test/enviar-texto?email=test@gmail.com

Personalizado (POST):
http://localhost:8092/api/email-test/enviar-personalizado
```

---

## ✅ CAMBIOS REALIZADOS

```
✓ Contraseña actualizada en application.properties
✓ Logs mejorados en EmailService con más detalles
✓ Creado EmailTestController con 5 endpoints de prueba
✓ Cada endpoint devuelve JSON con estado y detalles
✓ Logging completo para diagnosticar problemas
```

---

**¡Ahora tienes todas las herramientas para diagnosticar y resolver el problema de emails! 🚀**

Comienza con el TEST 1 y sigue el árbol de diagnóstico según los resultados.
