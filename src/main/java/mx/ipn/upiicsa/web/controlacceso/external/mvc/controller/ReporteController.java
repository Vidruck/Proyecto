package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import  com.fasterxml.jackson.databind.ObjectMapper;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.ReporteRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.projection.ReporteDato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteRepository reporteRepo;

    //Implementacion de jackson para convertir objetos a JSON

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String verReportes(Model model) throws Exception { //por si truena el JSON
        List<ReporteDato> datosGanancia = reporteRepo.obtenerGananciasPorSucursal();

        //Separacion de etiquetas y valores para que las manipule Chart.js
        List<String> lblGanancias = datosGanancia.stream().map(ReporteDato::getEtiqueta).toList();
        List<Double> valGanancias = datosGanancia.stream().map(ReporteDato::getValor).toList();

        model.addAttribute("jsonLblGanancias", objectMapper.writeValueAsString(lblGanancias));
        model.addAttribute("jsonValGanancias", objectMapper.writeValueAsString(valGanancias));


        //Grafica 2:  popularidad
        List<ReporteDato> datosTop = reporteRepo.obtenerTopServicios();

        List<String> lblTop = datosTop.stream().map(ReporteDato::getEtiqueta).toList();
        List<Double> valTop = datosTop.stream().map(ReporteDato::getValor).toList();

        model.addAttribute("jsonLblTop", objectMapper.writeValueAsString(lblTop));
        model.addAttribute("jsonValTop", objectMapper.writeValueAsString(valTop));

        return "reportes/index";
    }
}
