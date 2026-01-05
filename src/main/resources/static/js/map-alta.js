/**
 * Script para gestionar el mapa de alta de sucursales usando Leaflet.
 * Se encarga de capturar las coordenadas del clic y pasarlas al formulario oculto.
 */
document.addEventListener('DOMContentLoaded', function () {

    // Coordenadas iniciales (UPIICSA por defecto)
    const DEFAULT_LAT = 19.3962;
    const DEFAULT_LNG = -99.0913;
    const ZOOM_LEVEL = 13;

    // 1. Inicializar el mapa
    // 'map' debe coincidir con el id del div en el HTML
    var map = L.map('map').setView([DEFAULT_LAT, DEFAULT_LNG], ZOOM_LEVEL);

    // 2. Cargar capa de OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
    }).addTo(map);

    var marker;

    // Referencias a los inputs ocultos y etiquetas de visualización
    const inputLat = document.getElementById('lat');
    const inputLng = document.getElementById('lng');
    const displayCoords = document.getElementById('coords-display');

    // 3. Manejador de evento 'click' en el mapa
    map.on('click', function(e) {
        var lat = e.latlng.lat;
        var lng = e.latlng.lng;

        // Si ya hay marcador, lo movemos; si no, creamos uno nuevo
        if (marker) {
            marker.setLatLng(e.latlng);
        } else {
            marker = L.marker(e.latlng).addTo(map);
        }

        // Asignar valores a los inputs del formulario
        if(inputLat && inputLng) {
            inputLat.value = lat;
            inputLng.value = lng;
        }

        // Actualizar texto visual para el usuario
        if(displayCoords) {
            displayCoords.innerText = lat.toFixed(6) + ", " + lng.toFixed(6);
        }
    });
});