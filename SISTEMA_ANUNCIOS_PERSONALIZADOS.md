# 🎯 SISTEMA DE ANUNCIOS PERSONALIZADOS - CRM UniEats

## 📋 ESTRATEGIA COMPLETA

### 🎯 Objetivo
Crear un sistema de anuncios personalizados basado en:
- Historial de compras del cliente
- Categorías favoritas
- Vendedores preferidos
- Comportamiento de navegación
- Segmentación del CRM

---

## 🏗️ ARQUITECTURA PROPUESTA

### 1️⃣ **Anuncios en el Dashboard del Estudiante**
```
┌─────────────────────────────────────────┐
│  DASHBOARD ESTUDIANTE                   │
├─────────────────────────────────────────┤
│                                         │
│  🎯 ANUNCIOS PERSONALIZADOS PARA TI     │
│  ┌─────────────────────────────────┐   │
│  │ 🍔 ¡Tu vendedor favorito tiene  │   │
│  │    nuevos productos!            │   │
│  │    [Ver Menú] 👀               │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ 🎉 20% OFF en Pizza             │   │
│  │    Solo por hoy                 │   │
│  │    [Ordenar Ahora] 🛒          │   │
│  └─────────────────────────────────┘   │
│                                         │
│  📊 TUS PRODUCTOS RECOMENDADOS         │
│  ┌───────┐ ┌───────┐ ┌───────┐       │
│  │ Pizza │ │Burger │ │ Sushi │       │
│  └───────┘ └───────┘ └───────┘       │
└─────────────────────────────────────────┘
```

### 2️⃣ **Tipos de Anuncios**

#### A. **Anuncios de Banner (Header)**
- Posición: Top del dashboard
- Rotación: Cada 5 segundos
- Personalización: Basada en historial

#### B. **Anuncios de Productos Recomendados**
- Grid de productos sugeridos
- Basado en compras anteriores
- Categorías similares

#### C. **Notificaciones Push (In-App)**
- Alertas de nuevos productos
- Promociones exclusivas
- Vendedores favoritos

#### D. **Anuncios de Email**
- Ya implementado en CRM
- Campañas segmentadas
- Templates personalizados

---

## 🔧 IMPLEMENTACIÓN TÉCNICA

### Fase 1: Entidades y Modelos

#### 1. **Anuncio.java**
```java
@Entity
@Table(name = "anuncios")
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private String urlDestino;
    
    @Enumerated(EnumType.STRING)
    private TipoAnuncio tipo; // BANNER, PRODUCTO, PROMOCION
    
    @ManyToOne
    private Campana campana;
    
    @ManyToOne
    private Producto productoRelacionado;
    
    @ManyToOne
    private Vendedor vendedorRelacionado;
    
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    
    private Boolean activo = true;
    
    // Métricas
    private Integer impresiones = 0;
    private Integer clics = 0;
    private Double ctr = 0.0; // Click-Through Rate
}
```

#### 2. **AnuncioPersonalizado.java**
```java
@Entity
@Table(name = "anuncios_personalizados")
public class AnuncioPersonalizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Anuncio anuncio;
    
    private LocalDateTime fechaMostrado;
    private Boolean clickeado = false;
    private LocalDateTime fechaClick;
    
    // Score de relevancia (0-100)
    private Double scoreRelevancia;
    
    // Razón de personalización
    private String razonPersonalizacion;
}
```

#### 3. **Enum TipoAnuncio**
```java
public enum TipoAnuncio {
    BANNER,           // Banner top
    PRODUCTO,         // Anuncio de producto
    PROMOCION,        // Descuento/oferta
    VENDEDOR,         // Destacar vendedor
    CATEGORIA,        // Categoría completa
    PUSH_NOTIFICATION // Notificación push
}
```

### Fase 2: Servicios

#### 1. **AnuncioPersonalizacionService.java**
```java
@Service
public class AnuncioPersonalizacionService {
    
    /**
     * Obtener anuncios personalizados para un usuario
     */
    public List<AnuncioDTO> obtenerAnunciosPersonalizados(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId);
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        
        List<Anuncio> anuncios = new ArrayList<>();
        
        // 1. Anuncios basados en historial de compras
        anuncios.addAll(obtenerAnunciosPorHistorial(cliente));
        
        // 2. Anuncios basados en vendedores favoritos
        anuncios.addAll(obtenerAnunciosPorVendedoresFavoritos(cliente));
        
        // 3. Anuncios basados en categorías favoritas
        anuncios.addAll(obtenerAnunciosPorCategoriasFavoritas(cliente));
        
        // 4. Anuncios de campañas activas del segmento
        anuncios.addAll(obtenerAnunciosPorSegmento(cliente));
        
        // 5. Calcular score de relevancia y ordenar
        return calcularYOrdenarPorRelevancia(anuncios, cliente);
    }
    
    /**
     * Anuncios basados en productos comprados antes
     */
    private List<Anuncio> obtenerAnunciosPorHistorial(Cliente cliente) {
        // Obtener últimas categorías compradas
        List<String> categoriasCompradas = pedidoRepository
            .findTop10ByClienteOrderByFechaDesc(cliente)
            .stream()
            .flatMap(p -> p.getDetalles().stream())
            .map(d -> d.getProducto().getCategoria())
            .distinct()
            .collect(Collectors.toList());
        
        // Buscar anuncios de esas categorías
        return anuncioRepository.findActivosPorCategorias(categoriasCompradas);
    }
    
    /**
     * Anuncios de vendedores favoritos
     */
    private List<Anuncio> obtenerAnunciosPorVendedoresFavoritos(Cliente cliente) {
        // Obtener vendedores con más compras
        List<Vendedor> vendedoresFavoritos = pedidoRepository
            .findVendedoresMasFrecuentesByCliente(cliente.getId())
            .stream()
            .limit(5)
            .collect(Collectors.toList());
        
        // Anuncios de esos vendedores
        return anuncioRepository.findActivosPorVendedores(vendedoresFavoritos);
    }
    
    /**
     * Calcular score de relevancia
     */
    private Double calcularScoreRelevancia(Anuncio anuncio, Cliente cliente) {
        double score = 0.0;
        
        // +30 puntos si es de vendedor favorito
        if (esVendedorFavorito(anuncio.getVendedorRelacionado(), cliente)) {
            score += 30.0;
        }
        
        // +25 puntos si es de categoría comprada antes
        if (haCom pradoCategoria(anuncio.getProductoRelacionado().getCategoria(), cliente)) {
            score += 25.0;
        }
        
        // +20 puntos si está en el segmento de la campaña
        if (estaEnSegmentoCampana(cliente, anuncio.getCampana())) {
            score += 20.0;
        }
        
        // +15 puntos si es producto nuevo del vendedor
        if (esProductoNuevo(anuncio.getProductoRelacionado())) {
            score += 15.0;
        }
        
        // +10 puntos si es promoción/descuento
        if (anuncio.getTipo() == TipoAnuncio.PROMOCION) {
            score += 10.0;
        }
        
        return score;
    }
    
    /**
     * Registrar impresión de anuncio
     */
    @Transactional
    public void registrarImpresion(Long anuncioId, Long usuarioId) {
        Anuncio anuncio = anuncioRepository.findById(anuncioId).orElseThrow();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
        
        // Incrementar impresiones
        anuncio.setImpresiones(anuncio.getImpresiones() + 1);
        anuncioRepository.save(anuncio);
        
        // Crear registro personalizado
        AnuncioPersonalizado ap = new AnuncioPersonalizado();
        ap.setUsuario(usuario);
        ap.setAnuncio(anuncio);
        ap.setFechaMostrado(LocalDateTime.now());
        ap.setScoreRelevancia(calcularScoreRelevancia(anuncio, 
            clienteRepository.findByUsuario(usuario)));
        
        anuncioPersonalizadoRepository.save(ap);
    }
    
    /**
     * Registrar clic en anuncio
     */
    @Transactional
    public void registrarClic(Long anuncioPersonalizadoId) {
        AnuncioPersonalizado ap = anuncioPersonalizadoRepository
            .findById(anuncioPersonalizadoId).orElseThrow();
        
        ap.setClickeado(true);
        ap.setFechaClick(LocalDateTime.now());
        anuncioPersonalizadoRepository.save(ap);
        
        // Actualizar anuncio
        Anuncio anuncio = ap.getAnuncio();
        anuncio.setClics(anuncio.getClics() + 1);
        anuncio.setCtr((double) anuncio.getClics() / anuncio.getImpresiones() * 100);
        anuncioRepository.save(anuncio);
    }
}
```

#### 2. **AnuncioController.java**
```java
@RestController
@RequestMapping("/api/anuncios")
public class AnuncioController {
    
    @Autowired
    private AnuncioPersonalizacionService anuncioService;
    
    /**
     * Obtener anuncios personalizados para el usuario actual
     */
    @GetMapping("/personalizados")
    public ResponseEntity<List<AnuncioDTO>> obtenerAnunciosPersonalizados(
            Authentication auth) {
        Usuario usuario = (Usuario) auth.getPrincipal();
        List<AnuncioDTO> anuncios = anuncioService
            .obtenerAnunciosPersonalizados(usuario.getId());
        return ResponseEntity.ok(anuncios);
    }
    
    /**
     * Registrar impresión
     */
    @PostMapping("/{id}/impresion")
    public ResponseEntity<Void> registrarImpresion(
            @PathVariable Long id, Authentication auth) {
        Usuario usuario = (Usuario) auth.getPrincipal();
        anuncioService.registrarImpresion(id, usuario.getId());
        return ResponseEntity.ok().build();
    }
    
    /**
     * Registrar clic
     */
    @PostMapping("/personalizado/{id}/clic")
    public ResponseEntity<Void> registrarClic(@PathVariable Long id) {
        anuncioService.registrarClic(id);
        return ResponseEntity.ok().build();
    }
}
```

### Fase 3: Frontend (Dashboard Estudiante)

#### 1. **Componente de Anuncios**
```html
<!-- En estudiante_dashboard.html -->

<!-- SECCIÓN DE ANUNCIOS PERSONALIZADOS -->
<div class="anuncios-personalizados-container">
    <h3>🎯 Recomendado para ti</h3>
    
    <!-- Banner rotativo -->
    <div id="banner-carousel" class="carousel slide">
        <div class="carousel-inner" id="anuncios-banner">
            <!-- Se carga dinámicamente -->
        </div>
    </div>
    
    <!-- Grid de productos recomendados -->
    <div class="productos-recomendados">
        <h4>📦 Productos que te pueden gustar</h4>
        <div id="productos-grid" class="row">
            <!-- Se carga dinámicamente -->
        </div>
    </div>
    
    <!-- Anuncios de vendedores favoritos -->
    <div class="vendedores-favoritos">
        <h4>⭐ Novedades de tus vendedores favoritos</h4>
        <div id="vendedores-carousel">
            <!-- Se carga dinámicamente -->
        </div>
    </div>
</div>
```

#### 2. **JavaScript para Anuncios**
```javascript
// anuncios-personalizados.js

class AnunciosPersonalizados {
    constructor() {
        this.anuncios = [];
        this.impresionesRegistradas = new Set();
    }
    
    async cargarAnuncios() {
        try {
            const response = await fetch('/api/anuncios/personalizados');
            this.anuncios = await response.json();
            this.renderizarAnuncios();
        } catch (error) {
            console.error('Error cargando anuncios:', error);
        }
    }
    
    renderizarAnuncios() {
        this.renderizarBanners();
        this.renderizarProductosRecomendados();
        this.renderizarVendedoresFavoritos();
    }
    
    renderizarBanners() {
        const banners = this.anuncios.filter(a => a.tipo === 'BANNER');
        const container = document.getElementById('anuncios-banner');
        
        banners.forEach((banner, index) => {
            const isActive = index === 0 ? 'active' : '';
            const html = `
                <div class="carousel-item ${isActive}" 
                     data-anuncio-id="${banner.id}">
                    <div class="banner-anuncio" 
                         style="background-image: url('${banner.imagenUrl}')">
                        <div class="banner-content">
                            <h2>${banner.titulo}</h2>
                            <p>${banner.descripcion}</p>
                            <a href="${banner.urlDestino}" 
                               class="btn btn-primary"
                               onclick="registrarClicAnuncio(${banner.idPersonalizado})">
                                Ver más 👀
                            </a>
                        </div>
                        <span class="badge-razon">${banner.razonPersonalizacion}</span>
                    </div>
                </div>
            `;
            container.innerHTML += html;
            
            // Registrar impresión cuando se muestre
            this.registrarImpresionAlMostrar(banner.id);
        });
        
        // Activar carousel
        $('#banner-carousel').carousel({
            interval: 5000
        });
    }
    
    renderizarProductosRecomendados() {
        const productos = this.anuncios.filter(a => a.tipo === 'PRODUCTO');
        const container = document.getElementById('productos-grid');
        
        productos.slice(0, 6).forEach(producto => {
            const html = `
                <div class="col-md-4 mb-3">
                    <div class="card producto-recomendado" 
                         data-anuncio-id="${producto.id}">
                        <span class="badge-top">${producto.razonPersonalizacion}</span>
                        <img src="${producto.imagenUrl}" 
                             class="card-img-top" 
                             alt="${producto.titulo}">
                        <div class="card-body">
                            <h5>${producto.titulo}</h5>
                            <p>${producto.descripcion}</p>
                            <div class="d-flex justify-content-between">
                                <span class="precio">$${producto.precio}</span>
                                <button class="btn btn-sm btn-primary"
                                        onclick="agregarAlCarrito(${producto.productoId}); 
                                                 registrarClicAnuncio(${producto.idPersonalizado})">
                                    🛒 Agregar
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            container.innerHTML += html;
            
            this.registrarImpresionAlMostrar(producto.id);
        });
    }
    
    renderizarVendedoresFavoritos() {
        const vendedores = this.anuncios.filter(a => a.tipo === 'VENDEDOR');
        const container = document.getElementById('vendedores-carousel');
        
        vendedores.forEach(vendedor => {
            const html = `
                <div class="vendedor-card" data-anuncio-id="${vendedor.id}">
                    <div class="vendedor-badge">⭐ Tu vendedor favorito</div>
                    <img src="${vendedor.imagenUrl}" alt="${vendedor.titulo}">
                    <h4>${vendedor.titulo}</h4>
                    <p>${vendedor.descripcion}</p>
                    <a href="${vendedor.urlDestino}" 
                       class="btn btn-success"
                       onclick="registrarClicAnuncio(${vendedor.idPersonalizado})">
                        Ver menú 🍔
                    </a>
                </div>
            `;
            container.innerHTML += html;
            
            this.registrarImpresionAlMostrar(vendedor.id);
        });
    }
    
    registrarImpresionAlMostrar(anuncioId) {
        if (this.impresionesRegistradas.has(anuncioId)) return;
        
        // Usar Intersection Observer para registrar cuando sea visible
        const element = document.querySelector(`[data-anuncio-id="${anuncioId}"]`);
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting && !this.impresionesRegistradas.has(anuncioId)) {
                    this.registrarImpresion(anuncioId);
                    this.impresionesRegistradas.add(anuncioId);
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.5 });
        
        if (element) observer.observe(element);
    }
    
    async registrarImpresion(anuncioId) {
        try {
            await fetch(`/api/anuncios/${anuncioId}/impresion`, {
                method: 'POST'
            });
        } catch (error) {
            console.error('Error registrando impresión:', error);
        }
    }
}

async function registrarClicAnuncio(idPersonalizado) {
    try {
        await fetch(`/api/anuncios/personalizado/${idPersonalizado}/clic`, {
            method: 'POST'
        });
    } catch (error) {
        console.error('Error registrando clic:', error);
    }
}

// Inicializar al cargar página
document.addEventListener('DOMContentLoaded', () => {
    const anuncios = new AnunciosPersonalizados();
    anuncios.cargarAnuncios();
});
```

#### 3. **CSS para Anuncios**
```css
/* anuncios-personalizados.css */

.anuncios-personalizados-container {
    margin: 20px 0;
}

.banner-anuncio {
    position: relative;
    height: 300px;
    background-size: cover;
    background-position: center;
    border-radius: 15px;
    overflow: hidden;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}

.banner-content {
    position: absolute;
    bottom: 20px;
    left: 20px;
    background: rgba(255,255,255,0.95);
    padding: 20px;
    border-radius: 10px;
    max-width: 500px;
}

.badge-razon {
    position: absolute;
    top: 15px;
    right: 15px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 8px 15px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
}

.producto-recomendado {
    position: relative;
    transition: transform 0.3s, box-shadow 0.3s;
    cursor: pointer;
}

.producto-recomendado:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.15);
}

.badge-top {
    position: absolute;
    top: 10px;
    left: 10px;
    background: #FFD700;
    color: #333;
    padding: 5px 10px;
    border-radius: 15px;
    font-size: 11px;
    font-weight: bold;
    z-index: 10;
}

.vendedor-card {
    background: white;
    border-radius: 15px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    margin: 10px;
    transition: transform 0.3s;
}

.vendedor-card:hover {
    transform: scale(1.05);
}

.vendedor-badge {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    color: white;
    padding: 5px 15px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 600;
    display: inline-block;
    margin-bottom: 15px;
}
```

---

## 📊 MÉTRICAS Y ANALYTICS

### Dashboard de Anuncios para Marketing
```java
@GetMapping("/estadisticas/anuncios")
public EstadisticasAnunciosDTO obtenerEstadisticas() {
    return EstadisticasAnunciosDTO.builder()
        .totalImpresiones(...)
        .totalClics(...)
        .ctrPromedio(...)
        .anuncioMasEfectivo(...)
        .tasaConversion(...)
        .ventasGeneradas(...)
        .roiCampanas(...)
        .build();
}
```

---

## 🚀 PLAN DE IMPLEMENTACIÓN

### Semana 1: Backend
- ✅ Crear entidades Anuncio y AnuncioPersonalizado
- ✅ Crear repositorios
- ✅ Implementar AnuncioPersonalizacionService
- ✅ Crear endpoints REST

### Semana 2: Frontend
- ✅ Diseñar componentes de anuncios
- ✅ Implementar JavaScript para carga dinámica
- ✅ Estilos CSS
- ✅ Integración con dashboard

### Semana 3: Integración CRM
- ✅ Conectar con campañas existentes
- ✅ Usar segmentación del CRM
- ✅ Sincronizar métricas

### Semana 4: Testing y Ajustes
- ✅ Pruebas de personalización
- ✅ Optimización de algoritmo de relevancia
- ✅ Ajustes de UI/UX

---

## 💡 CARACTERÍSTICAS CLAVE

### 1. **Personalización Inteligente**
- 🎯 Basada en historial real de compras
- ⭐ Vendedores y productos favoritos
- 📊 Score de relevancia algorítmico
- 🔄 Actualización en tiempo real

### 2. **Métricas Completas**
- 👁️ Impresiones registradas
- 🖱️ Clics rastreados
- 📈 CTR calculado automáticamente
- 💰 Conversiones medidas

### 3. **Integración Total con CRM**
- 📧 Campañas de email → Anuncios en dashboard
- 👥 Segmentación compartida
- 📊 Métricas unificadas
- 🎯 Objetivos coordinados

### 4. **Experiencia de Usuario**
- 🎨 Diseño atractivo y moderno
- 📱 Responsive para móviles
- ⚡ Carga rápida y eficiente
- 🔔 No invasivo

---

## 🎯 RESULTADO ESPERADO

```
Usuario entra al dashboard
        ↓
Sistema analiza su perfil CRM
        ↓
Calcula anuncios más relevantes
        ↓
Muestra banners + productos + vendedores personalizados
        ↓
Usuario ve anuncios de SUS INTERESES
        ↓
Mayor probabilidad de clic
        ↓
Mayor conversión
        ↓
Más ventas para vendedores
        ↓
Mejor ROI de campañas
```

---

**¿Quieres que empiece a implementar esto? ¿Por cuál fase empezamos? 🚀**
