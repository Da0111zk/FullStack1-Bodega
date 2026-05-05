package com.example.despachos.service;

import com.example.despachos.dto.DespachoDTO;
import com.example.despachos.model.Despacho;
import com.example.despachos.repository.DespachoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DespachoService {

    @Autowired private DespachoRepository repository;
    @Autowired private WebClient webClientProductos;
    @Autowired private WebClient webClientStock;

    public Despacho crear(DespachoDTO dto) {
        log.info("Validando producto {} en ms-productos", dto.getProductoId());
        validarExterno(webClientProductos,
            "/api/productos/" + dto.getProductoId(),
            "Producto " + dto.getProductoId() + " no existe en ms-productos");

        log.info("Verificando stock para productoId={}", dto.getProductoId());
        Integer stockDisponible = obtenerStock(dto.getProductoId());
        if (stockDisponible < dto.getCantidad())
            throw new RuntimeException("Stock insuficiente. Disponible: " + stockDisponible + ", solicitado: " + dto.getCantidad());

        Despacho d = new Despacho();
        d.setProductoId(dto.getProductoId());
        d.setCantidad(dto.getCantidad());
        d.setFechaDespacho(LocalDate.now());
        d.setDestinatario(dto.getDestinatario());
        d.setEstado("PENDIENTE");
        d.setObservaciones(dto.getObservaciones());

        log.info("Despacho PENDIENTE creado para productoId={}", dto.getProductoId());
        return repository.save(d);
    }

    public Despacho confirmar(Long id) {
        Despacho d = buscarPorId(id);
        if (!d.getEstado().equals("PENDIENTE"))
            throw new RuntimeException("Solo se pueden confirmar despachos en estado PENDIENTE");

        try {
            webClientStock.patch()
                .uri("/api/stock/restar/" + d.getProductoId() + "/" + d.getCantidad())
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofSeconds(3))
                .block();
            log.info("Stock descontado para productoId={}", d.getProductoId());
        } catch (Exception e) {
            log.warn("No se pudo descontar stock en ms-stock: {}", e.getMessage());
        }

        d.setEstado("CONFIRMADO");
        log.info("Despacho ID {} CONFIRMADO", id);
        return repository.save(d);
    }

    public Despacho anular(Long id) {
        Despacho d = buscarPorId(id);
        if (d.getEstado().equals("CONFIRMADO"))
            throw new RuntimeException("No se puede anular un despacho ya CONFIRMADO");
        d.setEstado("ANULADO");
        log.info("Despacho ID {} ANULADO", id);
        return repository.save(d);
    }

    public List<Despacho> listarTodos() { return repository.findAll(); }

    public Despacho buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despacho no encontrado con ID: " + id));
    }

    public List<Despacho> listarPendientes() { return repository.findByEstado("PENDIENTE"); }

    private Integer obtenerStock(Long productoId) {
        try {
            Map<String, Object> stock = webClientStock.get()
                .uri("/api/stock/producto/" + productoId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .timeout(Duration.ofSeconds(3))
                .block();
            if (stock != null && stock.get("cantidad") != null)
                return ((Number) stock.get("cantidad")).intValue();
        } catch (Exception e) {
            log.warn("No se pudo obtener stock: {}", e.getMessage());
        }
        return 0;
    }

    private void validarExterno(WebClient client, String uri, String errorMsg) {
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