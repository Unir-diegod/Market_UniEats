-- Script SQL para inicializar datos de CRM en UniEats

-- Insertar rol MARKETING si no existe
INSERT INTO roles (nombre, descripcion)
SELECT 'ROLE_MARKETING', 'Rol para equipo de marketing y CRM'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ROLE_MARKETING');

-- Insertar templates de email de ejemplo
INSERT INTO email_templates (nombre, asunto, contenido_html, categoria, descripcion, activo, fecha_creacion)
VALUES 
(
    'Bienvenida VIP',
    '¡Felicidades {nombre}! Eres cliente {nivel}',
    '<!DOCTYPE html><html><body style="font-family: Arial;"><h1>Hola {nombre}!</h1><p>Tienes {puntos} puntos.</p></body></html>',
    'BIENVENIDA',
    'Template de bienvenida para clientes VIP',
    true,
    CURRENT_TIMESTAMP
),
(
    'Carrito Abandonado',
    '{nombre}, ¡Completa tu compra ahora!',
    '<!DOCTYPE html><html><body style="font-family: Arial;"><h1>Hola {nombre}!</h1><p>Tu carrito tiene ${valorCarrito}</p></body></html>',
    'ABANDONO_CARRITO',
    'Recordatorio de carrito abandonado',
    true,
    CURRENT_TIMESTAMP
),
(
    'Promoción Mensual',
    '¡Ofertas especiales para ti, {nombre}!',
    '<!DOCTYPE html><html><body style="font-family: Arial;"><h1>Hola {nombre}!</h1><p>Descuentos exclusivos para nivel {nivel}</p></body></html>',
    'PROMOCION',
    'Template para promociones mensuales',
    true,
    CURRENT_TIMESTAMP
);

-- Crear segmentos iniciales automáticos
INSERT INTO segmentos (nombre, descripcion, criterios, cantidad_clientes, fecha_creacion, activo)
VALUES 
(
    'Todos los Clientes',
    'Segmento que incluye a todos los clientes registrados',
    '{"todos": true}',
    0,
    CURRENT_TIMESTAMP,
    true
),
(
    'Clientes Nuevos',
    'Clientes registrados en los últimos 30 días',
    '{"dias_registro": 30}',
    0,
    CURRENT_TIMESTAMP,
    true
);

-- Crear una campaña de bienvenida de ejemplo (en estado BORRADOR)
INSERT INTO campanas (nombre, descripcion, tipo, estado, fecha_creacion, presupuesto, objetivo,
                      envios_totales, envios_exitosos, envios_fallidos, tasa_apertura, tasa_clics,
                      conversiones, ingresos_generados, roi)
VALUES 
(
    'Campaña de Bienvenida',
    'Campaña automática para dar la bienvenida a nuevos usuarios',
    'EMAIL',
    'BORRADOR',
    CURRENT_TIMESTAMP,
    50000.00,
    'Dar la bienvenida y promocionar el programa de fidelidad',
    0, 0, 0, 0.0, 0.0, 0, 0.0, 0.0
);

-- Nota: Los clientes se crearán automáticamente cuando los usuarios realicen su primer pedido
-- gracias al servicio CRMIntegracionService

COMMIT;
