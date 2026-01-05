/**
 * Script de seguridad para encriptar contraseñas en el cliente
 * Implementa SHA-512 + Base64 antes del envío.
 */
document.addEventListener('DOMContentLoaded', function () {

    // --- 1. LÓGICA DE VISUALIZACIÓN DE CONTRASEÑA (TOGGLE) ---
    // Se inicializa al cargar la página para que el botón funcione siempre.
    const toggleButtons = document.querySelectorAll('.btn-toggle-password');
    toggleButtons.forEach(button => {
        button.addEventListener('click', function () {
            const targetId = this.getAttribute('data-target');
            const input = document.getElementById(targetId);

            if (input && input.type === 'password') {
                input.type = 'text';
                this.textContent = '🙈'; // Icono de ocultar
            } else if (input) {
                input.type = 'password';
                this.textContent = '👁️'; // Icono de mostrar
            }
        });
    });

    // --- 2. ENCRIPTACIÓN ANTES DE ENVIAR (submit) ---
    const formulario = document.querySelector('form');

    if (formulario) {
        formulario.addEventListener('submit', async function (event) {
            // 1. Detenemos el envío estándar
            event.preventDefault();

            const form = event.target;
            // Buscamos inputs de tipo password que tengan name="password"
            // Nota: En signin.html o index.html el input puede tener otro ID, pero name="password" es común.
            // Si hay varios inputs de password (ej. confirmación), habría que adaptar esto.
            // Por ahora asumimos que el que se encripta es 'input[name="password"]'.
            const passwordInput = form.querySelector('input[name="password"]');

            // Si no hay password (o campo vacío), dejamos que el backend decida o validamos.
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