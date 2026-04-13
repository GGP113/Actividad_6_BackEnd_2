package com.control_de_lavanderia.control_de_lavanderia.controller;

import com.control_de_lavanderia.control_de_lavanderia.model.Orden;
import com.control_de_lavanderia.control_de_lavanderia.model.Prenda;
import com.control_de_lavanderia.control_de_lavanderia.service.LavanderiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private LavanderiaService lavanderiaService;

    @PostMapping
    public ResponseEntity<Orden> crearOrden(@RequestBody Orden orden) {
        // Enlazar las prendas con la orden para mantener la relación ManyToOne
        if (orden.getPrendas() != null) {
            for (Prenda p : orden.getPrendas()) {
                p.setOrden(orden);
            }
        }
        
        Orden nuevaOrden = lavanderiaService.procesarNuevaOrden(orden);
        return ResponseEntity.ok(nuevaOrden);
    }
}
