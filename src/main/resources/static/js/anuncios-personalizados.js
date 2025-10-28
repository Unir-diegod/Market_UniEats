/**
 * Sistema de Anuncios Personalizados
 * Uni-Eats Marketplace
 * 
 * Funcionalidades:
 * - Carga anuncios personalizados desde el backend
 * - Renderiza banners, productos y vendedores
 * - Registra impresiones automáticamente con IntersectionObserver
 * - Registra clics en anuncios
 * - Carousel automático de banners (5 segundos)
 */

class AnunciosPersonalizados {
    constructor() {
        this.anuncios = [];
        this.currentBannerIndex = 0;
        this.bannerInterval = null;
        this.observer = null;
        this.anunciosImpresionados = new Set();
    }

    /**
     * Inicializa el sistema de anuncios
     */
    async inicializar() {
        try {
            console.log('[Anuncios] Inicializando sistema...');
            await this.cargarAnuncios();
            this.renderizarAnuncios();
            this.iniciarCarousel();
            this.observarImpresiones();
            console.log('[Anuncios] Sistema inicializado correctamente');
        } catch (error) {
            console.error('[Anuncios] Error en inicialización:', error);
        }
    }

    /**
     * Carga anuncios personalizados desde el backend
     */
    async cargarAnuncios() {
        try {
            const response = await fetch('/api/anuncios/personalizados?limite=15');
            
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            
            this.anuncios = await response.json();
            console.log(`[Anuncios] Cargados ${this.anuncios.length} anuncios personalizados`);
            
        } catch (error) {
            console.error('[Anuncios] Error cargando anuncios:', error);
            this.anuncios = [];
        }
    }

    /**
     * Renderiza todos los anuncios en sus respectivas secciones
     */
    renderizarAnuncios() {
        const banners = this.anuncios.filter(a => a.tipo === 'BANNER' || a.tipo === 'PROMOCION');
        const productos = this.anuncios.filter(a => a.tipo === 'PRODUCTO');
        const vendedores = this.anuncios.filter(a => a.tipo === 'VENDEDOR');

        this.renderizarBanners(banners);
        this.renderizarProductos(productos);
        this.renderizarVendedores(vendedores);
    }

    /**
     * Renderiza banners en el carousel
     */
    renderizarBanners(banners) {
        const container = document.getElementById('banner-carousel');
        if (!container || banners.length === 0) {
            console.log('[Anuncios] No hay banners para mostrar');
            return;
        }

        let html = '';
        banners.slice(0, 5).forEach((banner, index) => {
            const activeClass = index === 0 ? 'active' : '';
            const badgeHtml = this.generarBadge(banner.motivoPersonalizacion);
            
            html += `
                <div class="banner-anuncio ${activeClass}" 
                     style="background-image: url('${banner.imagenUrl}')"
                     data-anuncio-id="${banner.id}"
                     data-posicion="${index + 1}"
                     onclick="anunciosPersonalizados.manejarClic(${banner.id}, '${banner.urlDestino}')">
                    <div class="banner-content">
                        ${badgeHtml}
                        <h2>${this.escapeHtml(banner.titulo)}</h2>
                        <p>${this.escapeHtml(banner.descripcion || '')}</p>
                    </div>
                </div>
            `;
        });

        // Agregar indicadores
        html += '<div class="carousel-indicators">';
        banners.slice(0, 5).forEach((_, index) => {
            const activeClass = index === 0 ? 'active' : '';
            html += `<div class="carousel-indicator ${activeClass}" data-index="${index}"></div>`;
        });
        html += '</div>';

        container.innerHTML = html;

        // Event listeners para indicadores
        document.querySelectorAll('.carousel-indicator').forEach(indicator => {
            indicator.addEventListener('click', () => {
                this.irABanner(parseInt(indicator.dataset.index));
            });
        });
    }

    /**
     * Renderiza productos recomendados
     */
    renderizarProductos(productos) {
        const container = document.getElementById('productos-grid');
        if (!container || productos.length === 0) return;

        let html = '';
        productos.slice(0, 6).forEach((producto, index) => {
            const badgeHtml = this.generarBadge(producto.motivoPersonalizacion);
            const precio = producto.productoPrecio ? 
                          this.formatearMoneda(producto.productoPrecio) : 'Consultar';
            
            html += `
                <div class="producto-recomendado fade-in-up fade-in-up-delay-${(index % 3) + 1}" 
                     data-anuncio-id="${producto.id}"
                     data-posicion="${index + 1}"
                     onclick="anunciosPersonalizados.manejarClic(${producto.id}, '${producto.urlDestino}')">
                    <img src="${producto.imagenUrl}" 
                         alt="${this.escapeHtml(producto.titulo)}" 
                         class="producto-imagen"
                         onerror="this.src='/img/placeholder-producto.jpg'">
                    <div class="producto-info">
                        ${badgeHtml}
                        <h4>${this.escapeHtml(producto.productoNombre || producto.titulo)}</h4>
                        <p>${this.escapeHtml(producto.descripcion || '')}</p>
                        <div class="producto-precio">${precio}</div>
                        ${producto.tiendaNombre ? 
                          `<div class="producto-vendedor">
                            <i class="fas fa-store"></i>
                            ${this.escapeHtml(producto.tiendaNombre)}
                          </div>` : ''}
                    </div>
                </div>
            `;
        });

        container.innerHTML = html;
    }

    /**
     * Renderiza vendedores destacados
     */
    renderizarVendedores(vendedores) {
        const container = document.getElementById('vendedores-grid');
        if (!container || vendedores.length === 0) return;

        let html = '';
        vendedores.slice(0, 4).forEach((vendedor, index) => {
            const badgeHtml = this.generarBadge(vendedor.motivoPersonalizacion);
            const favoritoClass = vendedor.motivoPersonalizacion?.includes('favorito') ? 'favorito' : '';
            
            html += `
                <div class="vendedor-card ${favoritoClass} fade-in-up" 
                     data-anuncio-id="${vendedor.id}"
                     data-posicion="${index + 1}"
                     onclick="anunciosPersonalizados.manejarClic(${vendedor.id}, '${vendedor.urlDestino}')">
                    <div class="vendedor-header">
                        <img src="${vendedor.tiendaLogo || '/img/default-logo.png'}" 
                             alt="${this.escapeHtml(vendedor.tiendaNombre)}" 
                             class="vendedor-logo"
                             onerror="this.src='/img/default-logo.png'">
                        <div class="vendedor-info">
                            ${badgeHtml}
                            <h4>${this.escapeHtml(vendedor.tiendaNombre || vendedor.titulo)}</h4>
                        </div>
                    </div>
                    <p>${this.escapeHtml(vendedor.descripcion || '')}</p>
                    <div class="vendedor-stats">
                        <div class="stat-item">
                            <div class="stat-value"><i class="fas fa-star"></i> 4.8</div>
                            <div class="stat-label">Rating</div>
                        </div>
                        <div class="stat-item">
                            <div class="stat-value">250+</div>
                            <div class="stat-label">Pedidos</div>
                        </div>
                    </div>
                </div>
            `;
        });

        container.innerHTML = html;
    }

    /**
     * Genera HTML del badge de personalización
     */
    generarBadge(motivo) {
        if (!motivo) return '';
        
        let clasesBadge = 'badge-personalizado';
        if (motivo.includes('favorito')) clasesBadge += ' badge-favorito';
        else if (motivo.includes('Compraste')) clasesBadge += ' badge-comprado';
        else if (motivo.includes('Nuevo')) clasesBadge += ' badge-nuevo';
        else if (motivo.includes('Promoción')) clasesBadge += ' badge-promocion';
        
        return `<span class="${clasesBadge}">${this.escapeHtml(motivo)}</span>`;
    }

    /**
     * Inicia el carousel automático de banners (rotación cada 5 segundos)
     */
    iniciarCarousel() {
        const banners = document.querySelectorAll('.banner-anuncio');
        if (banners.length <= 1) return;

        this.bannerInterval = setInterval(() => {
            this.siguienteBanner();
        }, 5000);
    }

    /**
     * Muestra el siguiente banner
     */
    siguienteBanner() {
        const banners = document.querySelectorAll('.banner-anuncio');
        if (banners.length === 0) return;

        banners[this.currentBannerIndex].classList.remove('active');
        
        this.currentBannerIndex = (this.currentBannerIndex + 1) % banners.length;
        
        banners[this.currentBannerIndex].classList.add('active');
        
        // Actualizar indicadores
        document.querySelectorAll('.carousel-indicator').forEach((ind, idx) => {
            ind.classList.toggle('active', idx === this.currentBannerIndex);
        });
    }

    /**
     * Ir a un banner específico
     */
    irABanner(index) {
        const banners = document.querySelectorAll('.banner-anuncio');
        if (index < 0 || index >= banners.length) return;

        banners[this.currentBannerIndex].classList.remove('active');
        this.currentBannerIndex = index;
        banners[this.currentBannerIndex].classList.add('active');

        document.querySelectorAll('.carousel-indicator').forEach((ind, idx) => {
            ind.classList.toggle('active', idx === this.currentBannerIndex);
        });

        // Reiniciar el intervalo
        if (this.bannerInterval) {
            clearInterval(this.bannerInterval);
            this.iniciarCarousel();
        }
    }

    /**
     * Observa cuando los anuncios son visibles para registrar impresiones
     */
    observarImpresiones() {
        const options = {
            root: null,
            rootMargin: '0px',
            threshold: 0.5 // 50% visible
        };

        this.observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting && entry.intersectionRatio >= 0.5) {
                    const anuncioId = entry.target.dataset.anuncioId;
                    const posicion = entry.target.dataset.posicion;
                    
                    if (anuncioId && !this.anunciosImpresionados.has(anuncioId)) {
                        this.registrarImpresion(anuncioId, posicion);
                        this.anunciosImpresionados.add(anuncioId);
                    }
                }
            });
        }, options);

        // Observar todos los elementos con data-anuncio-id
        document.querySelectorAll('[data-anuncio-id]').forEach(el => {
            this.observer.observe(el);
        });
    }

    /**
     * Registra una impresión en el backend
     */
    async registrarImpresion(anuncioId, posicion) {
        try {
            await fetch(`/api/anuncios/${anuncioId}/impresion?posicion=${posicion || 0}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            console.log(`[Anuncios] Impresión registrada: ${anuncioId}`);
        } catch (error) {
            console.error(`[Anuncios] Error registrando impresión:`, error);
        }
    }

    /**
     * Maneja el clic en un anuncio
     */
    async manejarClic(anuncioId, urlDestino) {
        try {
            await fetch(`/api/anuncios/${anuncioId}/clic`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
            console.log(`[Anuncios] Clic registrado: ${anuncioId}`);
            
            // Redirigir al destino
            if (urlDestino) {
                window.location.href = urlDestino;
            }
        } catch (error) {
            console.error(`[Anuncios] Error registrando clic:`, error);
        }
    }

    /**
     * Formatea un número como moneda colombiana
     */
    formatearMoneda(valor) {
        return new Intl.NumberFormat('es-CO', {
            style: 'currency',
            currency: 'COP',
            minimumFractionDigits: 0
        }).format(valor);
    }

    /**
     * Escapa HTML para prevenir XSS
     */
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    /**
     * Destruye el sistema (limpieza)
     */
    destruir() {
        if (this.bannerInterval) {
            clearInterval(this.bannerInterval);
        }
        if (this.observer) {
            this.observer.disconnect();
        }
    }
}

// Instancia global
let anunciosPersonalizados = null;

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    anunciosPersonalizados = new AnunciosPersonalizados();
    anunciosPersonalizados.inicializar();
});

// Limpiar antes de salir
window.addEventListener('beforeunload', () => {
    if (anunciosPersonalizados) {
        anunciosPersonalizados.destruir();
    }
});
