package mx.ipn.upiicsa.web.controlacceso.external.mvc.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AgendarCitaDto {
    private Integer idServicio;

    // @DateTimeFormat es vital para que Spring entienda el input type="date" del HTML
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    // @DateTimeFormat para entender el input type="time" o los strings de hora
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;
}