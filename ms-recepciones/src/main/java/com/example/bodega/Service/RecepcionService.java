package com.example.bodega.Service;

import com.example.bodega.DTO.*;
import com.example.bodega.Model.Recepciones;
import com.example.bodega.Repository.RecepcionesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class RecepcionService {

    @Autowired private RecepcionesRepository repository;
    @Autowired private WebClient webClientProductos;
    @Autowired private WebClient webClientProveedores;
    @Autowired private WebClient webClientStock;

    public Recepciones registrar(RecepcionesDTO dto) {
        if (repository.findByNumeroGuia(dto.getNumeroGuia()).isPresent())
            throw new RuntimeException("Ya existe una recepción con número de guía: " + dto.getNumeroGuia());

        log.info("Validando producto {} en ms-productos", dto.getProductoId());
        validarServicioExterno(webClientProductos,
            "/api/productos/" + dto.getProductoId(),
            "Producto " + dto.getProductoId() + " no existe en ms-productos");

        log.info("Validando proveedor {} en ms-proveedores", dto.getProveedorId());
        validarServicioExterno(webClientProveedores,
            "/api/proveedores/" + dto.getProveedorId(),
            "Proveedor " + dto.getProveedorId() + " no existe en ms-proveedores");

        Recepciones r = new Recepciones();
        r.setProductoId(dto.getProductoId());
        r.setProveedorId(dto.getProveedorId());
        r.setCantidad(dto.getCantidad());
        r.setFechaRecepcion(LocalDate.now());
        r.setNumeroGuia(dto.getNumeroGuia());
        r.setObservaciones(dto.getObservaciones());

        Recepciones guardada = repository.save(r);
        log.info("Recepción guardada con ID={}. Actualizando stock...", guardada.getId());

        try {
            webClientStock.patch()
                .uri("/api/stock/sumar/" + dto.getProductoId() + "/" + dto.getCantidad())
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(3))
                .block();
            log.info("Stock actualizado para productoId={}", dto.getProductoId());
        } catch (Exception e) {
            log.warn("No se pudo actualizar stock en ms-stock: {}", e.getMessage());
        }

        return guardada;
    }

    public List<Recepciones> listarTodos() {
        return repository.findAll();
    }

    public Recepciones buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada con ID: " + id));
    }

    public List<Recepciones > buscarPorProductoId(Long productoId) {
        return repository.findByProductoId(productoId);
    }

    public Recepciones actualizar(Long id, RecepcionesDTO dto) {
        Recepciones r = buscarPorId(id);
        r.setObservaciones(dto.getObservaciones());
        log.info("Recepción ID {} actualizada", id);
        return repository.save(r);
    }

    private void validarServicioExterno(WebClient client, String uri, String errorMsg) {
        try {
            client.get().uri(uri)
                  .retrieve()
                  .bodyToMono(Object.class)
                  .timeout(Duration.ofSeconds(3))
                  .block();
        } catch (Exception e) {
            log.error("Error en llamada externa a {}: {}", uri, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }
}