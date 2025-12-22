/**
 * Script de seguridad para encriptar contraseñas en el cliente
 * Implementa SHA-512 + Base64 antes del envío.
 */
document.addEventListener('DOMContentLoaded', function() {
    // Buscamos el formulario en la página actual
    const formulario = document.querySelector('form');

    if (formulario) {
        formulario.addEventListener('submit', async function(event) {
            // 1. Detenemos el envío estándar
            event.preventDefault();

            const form = event.target;
            const passwordInput = form.querySelector('input[name="password"]');

            // Si no hay password (o campo vacío), dejamos que el backend decida
            if (!passwordInput || !passwordInput.value) {
                form.submit();
                return;
            }

            try {
                // Guardamos el valor original
                const passwordTextoPlano = passwordInput.value;

                // 2. Proceso de Encriptación
                const encoder = new TextEncoder();
                const data = encoder.encode(passwordTextoPlano);

                // Hashing (SHA-512)
                const hashBuffer = await crypto.subtle.digest('SHA-512', data);

                // Conversión a Base64
                const hashArray = Array.from(new Uint8Array(hashBuffer));
                const hashBase64 = btoa(hashArray.map(byte => String.fromCharCode(byte)).join(''));

                // 3. Reemplazamos el valor y enviamos
                passwordInput.value = hashBase64;

                // Desactivamos este listener para evitar bucles infinitos al hacer submit manual
                // o simplemente usamos el método nativo del elemento HTMLFormElement
                HTMLFormElement.prototype.submit.call(form);

            } catch (error) {
                console.error("Error crítico de seguridad:", error);
                alert("Error de seguridad al procesar los datos. Intente nuevamente.");
            }
        });
    }
});