package com.example.despachos.controller;

import com.example.despachos.dto.DespachoDTO;
import com.example.despachos.model.Despacho;
import com.example.despachos.service.DespachoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/despachos")
public class DespachoController {

    @Autowired private DespachoService service;

    @PostMapping
    public ResponseEntity<Despacho> crear(@Valid @RequestBody DespachoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<Despacho>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despacho> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Despacho>> listarPendientes() {
        return ResponseEntity.ok(service.listarPendientes());
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Despacho> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(service.confirmar(id));
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<Despacho> anular(@PathVariable Long id) {
        return ResponseEntity.ok(service.anular(id));
    }
}