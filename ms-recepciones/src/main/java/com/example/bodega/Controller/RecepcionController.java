package com.example.bodega.Controller;

import com.example.bodega.DTO.RecepcionesDTO;
import com.example.bodega.Model.Recepciones;
import com.example.bodega.Service.RecepcionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones")
public class RecepcionController {

    @Autowired
    private RecepcionService service;

    @PostMapping
    public ResponseEntity<Recepciones> registrar(@Valid @RequestBody RecepcionesDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<Recepciones>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepciones> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Recepciones>> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(service.buscarPorProductoId(productoId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recepciones> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody RecepcionesDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }
}