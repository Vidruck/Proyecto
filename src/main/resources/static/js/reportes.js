document.addEventListener('DOMContentLoaded', function () {

    // --- CONFIGURACIÓN DE COLORES (Detección de Modo Oscuro) ---
    // Verificamos si el body tiene un estilo oscuro o si el sistema lo prefiere
    const isDarkMode = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

    // Colores basados en el tema
    const textColor = isDarkMode ? '#cdd6f4' : '#4c4f69'; // Texto claro u oscuro
    const gridColor = isDarkMode ? '#45475a' : '#ccd0da'; // Líneas sutiles

    // Aplicar defaults a Chart.js
    Chart.defaults.color = textColor;
    Chart.defaults.borderColor = gridColor;

    // --- GRÁFICA 1: GANANCIAS ---
    const canvasGanancias = document.getElementById('chartGanancias');
    if (canvasGanancias) {
        const labels = JSON.parse(canvasGanancias.dataset.labels);
        const values = JSON.parse(canvasGanancias.dataset.values);

        new Chart(canvasGanancias, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Ingresos ($)',
                    data: values,
                    backgroundColor: '#1e66f5',
                    borderRadius: 6,
                    borderWidth: 1,
                    borderColor: '#1e66f5'
                }]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        grid: { color: 'transparent' } // Quitamos líneas verticales para limpieza
                    },
                    y: {
                        beginAtZero: true,
                        grid: { color: gridColor }     // Color de líneas horizontales
                    }
                },
                plugins: {
                    legend: { labels: { color: textColor } }
                }
            }
        });
    }

    // --- GRÁFICA 2: SERVICIOS ---
    const canvasServicios = document.getElementById('chartServicios');
    if (canvasServicios) {
        const labels = JSON.parse(canvasServicios.dataset.labels);
        const values = JSON.parse(canvasServicios.dataset.values);

        new Chart(canvasServicios, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                    data: values,
                    backgroundColor: ['#1e66f5',
                        '#26a269', //Azul
                        '#e5a50a', //Verde
                        '#c061cb', //Morado/rosa (No sé)
                        '#e01b24' //Rojo
                    ],
                    borderWidth: 0 // Sin bordes blancos feos
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: { color: textColor }
                    }
                }
            }
        });
    }
});