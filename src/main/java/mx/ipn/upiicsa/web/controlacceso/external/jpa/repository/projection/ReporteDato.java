package mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.projection;

/**
 * Proyección (interfaz) para mapear resultados de consultas nativas de
 * reportes.
 * Utilizada para obtener pares etiqueta-valor para gráficas.
 *
 */
public interface ReporteDato {
    /**
     * Obtiene la etiqueta del dato (ej. Nombre de Sucursal, Nombre de Servicio).
     * 
     * @return La etiqueta.
     */
    String getEtiqueta();

    /**
     * Obtiene el valor numérico del dato (ej. Ganancia Total, Cantidad de Citas).
     * 
     * @return El valor.
     */
    Double getValor();

}
