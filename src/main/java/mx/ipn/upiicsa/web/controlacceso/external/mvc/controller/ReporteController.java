package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ReporteRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.projection.ReporteDato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador MVC para la visualización de reportes estadísticos.
 * Convierte los datos del repositorio a JSON para ser consumidos por Chart.js.
 * Mapea a la ruta "/reportes".
 *
 */
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteRepository reporteRepo;

    // Implementacion de jackson para convertir objetos a JSON

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Genera y muestra los reportes estadísticos.
     * Obtiene datos de ganancias por sucursal y top de servicios.
     *
     * @param model Modelo para enviar cadenas JSON a la vista.
     * @return Vista "reportes/index".
     * @throws Exception Si ocurre un error al serializar a JSON.
     */
    @GetMapping
    public String verReportes(Model model) throws Exception { // por si truena el JSON
        List<ReporteDato> datosGanancia = reporteRepo.obtenerGananciasPorSucursal();

        // Separacion de etiquetas y valores para que las manipule Chart.js
        List<String> lblGanancias = datosGanancia.stream().map(ReporteDato::getEtiqueta).toList();
        List<Double> valGanancias = datosGanancia.stream().map(ReporteDato::getValor).toList();

        model.addAttribute("jsonLblGanancias", objectMapper.writeValueAsString(lblGanancias));
        model.addAttribute("jsonValGanancias", objectMapper.writeValueAsString(valGanancias));

        // Grafica 2: popularidad
        List<ReporteDato> datosTop = reporteRepo.obtenerTopServicios();

        List<String> lblTop = datosTop.stream().map(ReporteDato::getEtiqueta).toList();
        List<Double> valTop = datosTop.stream().map(ReporteDato::getValor).toList();

        model.addAttribute("jsonLblTop", objectMapper.writeValueAsString(lblTop));
        model.addAttribute("jsonValTop", objectMapper.writeValueAsString(valTop));

        return "reportes/index";
    }
}
