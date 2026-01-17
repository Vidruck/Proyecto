package mx.ipn.upiicsa.web.controlacceso.external.mvc.dto;

/**
 * DTO para el registro de nuevas sucursales y sus establecimientos.
 * Incluye coordenadas geográficas para la ubicación.
 *
 */
public class AltaSucursalDto {

    private String nombre;

    private Double latitud;
    private Double longitud;

    private String nombreEstablecimiento;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombreEstablecimiento() {
        return nombreEstablecimiento;
    }

    public void setNombreEstablecimiento(String nombreEstablecimiento) {
        this.nombreEstablecimiento = nombreEstablecimiento;
    }
}