package mx.ipn.upiicsa.web.controlacceso.external.mvc.controller;

import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.EstablecimientoRepository;
import mx.ipn.upiicsa.web.controlacceso.external.jpa.repository.SucursalRepository;
import mx.ipn.upiicsa.web.controlacceso.external.mvc.dto.AltaSucursalDto;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Establecimiento;
import mx.ipn.upiicsa.web.controlacceso.internal.bs.entity.Sucursal;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/establecimientos")
public class EstablecimientoController {

    @Autowired
    private EstablecimientoRepository establecimientoRepo;
    @Autowired
    private SucursalRepository sucursalRepo;

    // Factoría JTS para crear puntos con SRID 4326 (WGS84)
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @GetMapping("/alta")
    public String mostrarAlta(Model model) {
        model.addAttribute("sucursalDto", new AltaSucursalDto());
        return "alta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute AltaSucursalDto dto, RedirectAttributes redirectAttrs) {
        // 1. Crear Establecimiento
        Establecimiento est = Establecimiento.builder()
                .nombre(dto.getNombreEstablecimiento())
                .build();
        est = establecimientoRepo.save(est);

        // 2. Crear Punto Geográfico (Longitud primero, Latitud después en JTS)
        Point punto = geometryFactory.createPoint(new Coordinate(dto.getLongitud(), dto.getLatitud()));

        // 3. Guardar Sucursal
        Sucursal suc = Sucursal.builder()
                .establecimiento(est)
                .nombre(dto.getNombre())
                .ubicacion(punto)
                .build();

        sucursalRepo.save(suc);

        redirectAttrs.addFlashAttribute("mensajeExito", "¡Sucursal registrada correctamente!");
        return "redirect:/establecimientos/alta";
    }
}