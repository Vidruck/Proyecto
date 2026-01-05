document.addEventListener('DOMContentLoaded', function () {

    // --- GRÁFICA 1: GANANCIAS (BARRAS) ---
    const canvasGanancias = document.getElementById('chartGanancias');

    if (canvasGanancias) {
        // Leemos los datos desde los atributos 'data-' del HTML
        // Es vital usar JSON.parse porque vienen como texto string "[...]"
        const labels = JSON.parse(canvasGanancias.dataset.labels);
        const values = JSON.parse(canvasGanancias.dataset.values);

        new Chart(canvasGanancias, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Ingresos ($)',
                    data: values,
                    backgroundColor: '#1e66f5', // Adwaita Blue
                    borderRadius: 6
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    }

    // --- GRÁFICA 2: SERVICIOS TOP (DONA) ---
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
                    backgroundColor: [
                        '#1e66f5', // Blue
                        '#26a269', // Green
                        '#e5a50a', // Yellow
                        '#c061cb', // Purple
                        '#e01b24'  // Red
                    ],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { position: 'bottom' }
                }
            }
        });
    }
});