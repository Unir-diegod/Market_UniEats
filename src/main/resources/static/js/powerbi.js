/**
 * @file Power BI Integration para Dashboard de Vendedor
 * @description Maneja la integración de reportes de Power BI embebidos
 * @version 1.0
 */

class PowerBIManager {
    constructor() {
        this.config = null;
        this.embedContainer = null;
        this.isLoaded = false;
    }

    /**
     * Cargar configuración de Power BI desde el backend
     */
    async cargarConfiguracion() {
        try {
            const response = await fetch('/api/powerbi/config', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    [App.config.csrfHeader]: App.config.csrfToken
                }
            });

            if (!response.ok) {
                throw new Error('Error al cargar configuración de Power BI');
            }

            this.config = await response.json();
            return this.config;
        } catch (error) {
            console.error('Error cargando configuración Power BI:', error);
            return null;
        }
    }

    /**
     * Renderizar la vista de Analytics con Power BI
     */
    async renderVistaAnalytics() {
        const config = await this.cargarConfiguracion();

        if (!config || !config.tieneConfiguracion) {
            return this.renderVistaConfiguracion(config);
        }

        if (!config.activo) {
            return this.renderVistaDesactivada(config);
        }

        return this.renderVistaReporte(config);
    }

    /**
     * Vista cuando no hay configuración
     */
    renderVistaConfiguracion(config) {
        return `
            <div id="view-analytics" class="main-view p-4">
                <header class="mb-6">
                    <h1 class="text-2xl font-bold text-slate-800 flex items-center gap-2">
                        <i class="fas fa-chart-line text-indigo-600"></i>
                        Analytics Inteligente
                    </h1>
                    <p class="text-slate-600 mt-1">Integra Power BI para análisis avanzados</p>
                </header>

                <div class="bg-gradient-to-br from-indigo-50 to-purple-50 rounded-2xl p-8 text-center border-2 border-indigo-200">
                    <div class="w-24 h-24 mx-auto bg-indigo-100 rounded-full flex items-center justify-center mb-6">
                        <i class="fas fa-chart-bar text-4xl text-indigo-600"></i>
                    </div>
                    
                    <h2 class="text-2xl font-bold text-slate-800 mb-3">
                        Potencia tu negocio con Power BI
                    </h2>
                    
                    <p class="text-slate-600 mb-6 max-w-md mx-auto">
                        Conecta tus reportes de Power BI y obtén insights en tiempo real sobre tu negocio
                    </p>

                    <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8 text-left max-w-2xl mx-auto">
                        <div class="bg-white p-4 rounded-lg shadow-sm">
                            <i class="fas fa-chart-line text-green-500 text-2xl mb-2"></i>
                            <h3 class="font-semibold text-slate-800 mb-1">Análisis en tiempo real</h3>
                            <p class="text-sm text-slate-600">Visualiza métricas actualizadas</p>
                        </div>
                        <div class="bg-white p-4 rounded-lg shadow-sm">
                            <i class="fas fa-filter text-blue-500 text-2xl mb-2"></i>
                            <h3 class="font-semibold text-slate-800 mb-1">Filtros personalizados</h3>
                            <p class="text-sm text-slate-600">Explora tus datos a detalle</p>
                        </div>
                        <div class="bg-white p-4 rounded-lg shadow-sm">
                            <i class="fas fa-mobile-alt text-purple-500 text-2xl mb-2"></i>
                            <h3 class="font-semibold text-slate-800 mb-1">Acceso móvil</h3>
                            <p class="text-sm text-slate-600">Consulta desde cualquier lugar</p>
                        </div>
                    </div>

                    <button onclick="powerBIManager.mostrarModalConfiguracion()" 
                            class="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 px-8 rounded-lg shadow-lg transition-all transform hover:scale-105">
                        <i class="fas fa-plug mr-2"></i>
                        Configurar Power BI
                    </button>

                    <p class="text-sm text-slate-500 mt-6">
                        <i class="fas fa-info-circle mr-1"></i>
                        Necesitas tener un reporte publicado en Power BI
                    </p>
                </div>

                <div class="mt-8 bg-white rounded-xl p-6 shadow-sm">
                    <h3 class="font-bold text-slate-800 mb-4 flex items-center gap-2">
                        <i class="fas fa-question-circle text-indigo-600"></i>
                        ¿Cómo configurar Power BI?
                    </h3>
                    <ol class="space-y-3 text-slate-700">
                        <li class="flex gap-3">
                            <span class="flex-shrink-0 w-6 h-6 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center font-bold text-sm">1</span>
                            <span>Crea tu reporte en Power BI Desktop</span>
                        </li>
                        <li class="flex gap-3">
                            <span class="flex-shrink-0 w-6 h-6 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center font-bold text-sm">2</span>
                            <span>Publica el reporte en Power BI Service</span>
                        </li>
                        <li class="flex gap-3">
                            <span class="flex-shrink-0 w-6 h-6 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center font-bold text-sm">3</span>
                            <span>Obtén el enlace de compartir público</span>
                        </li>
                        <li class="flex gap-3">
                            <span class="flex-shrink-0 w-6 h-6 bg-indigo-100 text-indigo-600 rounded-full flex items-center justify-center font-bold text-sm">4</span>
                            <span>Pega el enlace en la configuración</span>
                        </li>
                    </ol>
                </div>
            </div>
        `;
    }

    /**
     * Vista cuando la configuración está desactivada
     */
    renderVistaDesactivada(config) {
        return `
            <div id="view-analytics" class="main-view p-4">
                <header class="mb-6">
                    <h1 class="text-2xl font-bold text-slate-800">Analytics Inteligente</h1>
                </header>

                <div class="bg-yellow-50 border-2 border-yellow-200 rounded-xl p-8 text-center">
                    <i class="fas fa-pause-circle text-6xl text-yellow-600 mb-4"></i>
                    <h2 class="text-xl font-bold text-slate-800 mb-2">Reporte desactivado</h2>
                    <p class="text-slate-600 mb-6">Tu reporte de Power BI está temporalmente desactivado</p>
                    
                    <div class="flex gap-4 justify-center">
                        <button onclick="powerBIManager.activarReporte()" 
                                class="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-6 rounded-lg">
                            <i class="fas fa-play mr-2"></i>Activar Reporte
                        </button>
                        <button onclick="powerBIManager.mostrarModalConfiguracion()" 
                                class="bg-slate-600 hover:bg-slate-700 text-white font-bold py-2 px-6 rounded-lg">
                            <i class="fas fa-cog mr-2"></i>Configuración
                        </button>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * Vista con el reporte de Power BI embebido
     */
    renderVistaReporte(config) {
        return `
            <div id="view-analytics" class="main-view flex flex-col h-screen">
                <header class="p-4 bg-white border-b flex justify-between items-center flex-shrink-0">
                    <div>
                        <h1 class="text-xl font-bold text-slate-800 flex items-center gap-2">
                            <i class="fas fa-chart-line text-indigo-600"></i>
                            Analytics - ${config.tiendaNombre}
                        </h1>
                        <p class="text-sm text-slate-600">Powered by Power BI</p>
                    </div>
                    <div class="flex gap-2">
                        <button onclick="powerBIManager.recargarReporte()" 
                                class="px-3 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg text-slate-700 transition-colors"
                                title="Recargar reporte">
                            <i class="fas fa-sync-alt"></i>
                        </button>
                        <button onclick="powerBIManager.abrirFullscreen()" 
                                class="px-3 py-2 bg-slate-100 hover:bg-slate-200 rounded-lg text-slate-700 transition-colors"
                                title="Pantalla completa">
                            <i class="fas fa-expand"></i>
                        </button>
                        <button onclick="powerBIManager.mostrarModalConfiguracion()" 
                                class="px-3 py-2 bg-indigo-100 hover:bg-indigo-200 rounded-lg text-indigo-700 transition-colors"
                                title="Configuración">
                            <i class="fas fa-cog"></i>
                        </button>
                    </div>
                </header>

                <div id="powerbi-embed-container" class="flex-1 bg-slate-100 relative overflow-hidden">
                    <div class="absolute inset-0 flex items-center justify-center" id="powerbi-loading">
                        <div class="text-center">
                            <i class="fas fa-spinner fa-spin text-4xl text-indigo-600 mb-4"></i>
                            <p class="text-slate-600">Cargando reporte...</p>
                        </div>
                    </div>
                    <iframe 
                        id="powerbi-iframe"
                        src="${config.embedUrl}&embedded=true&navContentPaneEnabled=false"
                        frameborder="0" 
                        allowfullscreen="true"
                        class="w-full h-full"
                        style="border: none;"
                        onload="powerBIManager.onReporteLoaded()">
                    </iframe>
                </div>

                <div class="p-2 bg-slate-50 border-t text-center text-xs text-slate-500 flex-shrink-0">
                    <i class="fas fa-shield-alt mr-1"></i>
                    Reporte embebido de forma segura | 
                    <a href="${config.embedUrl}" target="_blank" class="text-indigo-600 hover:underline">
                        Abrir en Power BI
                        <i class="fas fa-external-link-alt ml-1"></i>
                    </a>
                </div>
            </div>
        `;
    }

    /**
     * Callback cuando el reporte se carga
     */
    onReporteLoaded() {
        const loading = document.getElementById('powerbi-loading');
        if (loading) {
            setTimeout(() => {
                loading.style.display = 'none';
                this.isLoaded = true;
                console.log('✅ Reporte Power BI cargado exitosamente');
            }, 1500);
        }
    }

    /**
     * Recargar el reporte
     */
    recargarReporte() {
        const iframe = document.getElementById('powerbi-iframe');
        if (iframe) {
            const loading = document.getElementById('powerbi-loading');
            if (loading) loading.style.display = 'flex';
            
            iframe.src = iframe.src;
            console.log('🔄 Recargando reporte Power BI');
        }
    }

    /**
     * Abrir en pantalla completa
     */
    abrirFullscreen() {
        const container = document.getElementById('powerbi-embed-container');
        if (container) {
            if (container.requestFullscreen) {
                container.requestFullscreen();
            } else if (container.webkitRequestFullscreen) {
                container.webkitRequestFullscreen();
            } else if (container.msRequestFullscreen) {
                container.msRequestFullscreen();
            }
        }
    }

    /**
     * Mostrar modal de configuración
     */
    mostrarModalConfiguracion() {
        const modalHTML = `
            <div id="powerbi-config-modal" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
                <div class="bg-white rounded-2xl shadow-2xl max-w-2xl w-full max-h-[90vh] flex flex-col">
                    <div class="p-6 border-b flex justify-between items-center">
                        <h2 class="text-2xl font-bold text-slate-800">
                            <i class="fas fa-cog text-indigo-600 mr-2"></i>
                            Configuración de Power BI
                        </h2>
                        <button onclick="powerBIManager.cerrarModal()" class="text-slate-400 hover:text-slate-600 text-2xl">
                            &times;
                        </button>
                    </div>

                    <form id="powerbi-config-form" class="p-6 overflow-y-auto flex-1">
                        <div class="space-y-4">
                            <div>
                                <label class="block text-sm font-semibold text-slate-700 mb-2">
                                    URL de Embed del Reporte *
                                </label>
                                <input type="url" 
                                       id="embed-url" 
                                       class="w-full px-4 py-3 border-2 border-slate-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
                                       placeholder="https://app.powerbi.com/view?r=..."
                                       required>
                                <p class="text-xs text-slate-500 mt-1">
                                    <i class="fas fa-info-circle"></i>
                                    Obtén este enlace desde Power BI → Compartir → Embed
                                </p>
                            </div>

                            <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
                                <h3 class="font-semibold text-blue-900 mb-2 flex items-center gap-2">
                                    <i class="fas fa-lightbulb"></i>
                                    Consejos
                                </h3>
                                <ul class="text-sm text-blue-800 space-y-1">
                                    <li>• Asegúrate de que el reporte sea público o compartido</li>
                                    <li>• Usa el modo "Publicar en la web" para mayor compatibilidad</li>
                                    <li>• El reporte se actualizará automáticamente según tu configuración en Power BI</li>
                                </ul>
                            </div>
                        </div>
                    </form>

                    <div class="p-6 border-t bg-slate-50 flex justify-end gap-3">
                        <button type="button" 
                                onclick="powerBIManager.cerrarModal()" 
                                class="px-6 py-2 bg-white border border-slate-300 text-slate-700 font-semibold rounded-lg hover:bg-slate-100">
                            Cancelar
                        </button>
                        <button type="submit" 
                                form="powerbi-config-form"
                                onclick="powerBIManager.guardarConfiguracion(event)"
                                class="px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-bold rounded-lg">
                            <i class="fas fa-save mr-2"></i>
                            Guardar Configuración
                        </button>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHTML);

        // Cargar configuración existente si hay
        if (this.config && this.config.tieneConfiguracion) {
            document.getElementById('embed-url').value = this.config.embedUrl || '';
        }
    }

    /**
     * Guardar configuración
     */
    async guardarConfiguracion(event) {
        event.preventDefault();

        const embedUrl = document.getElementById('embed-url').value;

        if (!embedUrl) {
            alert('Por favor ingresa la URL del reporte');
            return;
        }

        try {
            const response = await fetch('/api/powerbi/config', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [App.config.csrfHeader]: App.config.csrfToken
                },
                body: JSON.stringify({
                    embedUrl: embedUrl,
                    embedType: 'PUBLIC',
                    activo: true
                })
            });

            if (!response.ok) {
                throw new Error('Error al guardar configuración');
            }

            const result = await response.json();
            console.log('✅ Configuración guardada:', result);

            this.cerrarModal();
            
            // Recargar la vista
            if (typeof App !== 'undefined' && App.views && App.views.showView) {
                App.views.showView('analytics');
            } else {
                location.reload();
            }

        } catch (error) {
            console.error('Error guardando configuración:', error);
            alert('Error al guardar la configuración. Por favor intenta de nuevo.');
        }
    }

    /**
     * Activar reporte
     */
    async activarReporte() {
        try {
            const response = await fetch('/api/powerbi/config/estado?activo=true', {
                method: 'PATCH',
                headers: {
                    [App.config.csrfHeader]: App.config.csrfToken
                }
            });

            if (!response.ok) {
                throw new Error('Error al activar reporte');
            }

            console.log('✅ Reporte activado');
            location.reload();

        } catch (error) {
            console.error('Error activando reporte:', error);
            alert('Error al activar el reporte');
        }
    }

    /**
     * Cerrar modal
     */
    cerrarModal() {
        const modal = document.getElementById('powerbi-config-modal');
        if (modal) {
            modal.remove();
        }
    }
}

// Instancia global
window.powerBIManager = new PowerBIManager();
