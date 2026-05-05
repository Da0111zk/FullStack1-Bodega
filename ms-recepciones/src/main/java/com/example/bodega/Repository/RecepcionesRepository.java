package com.example.bodega.Repository;

import com.example.bodega.Model.Recepciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository

public interface RecepcionesRepository extends JpaRepository<Recepciones, Long> {
    Optional<Recepciones> findByNumeroGuia(String numeroGuia);
    List<Recepciones> findByProductoId(Long productoId);
    List<Recepciones> findByProveedorId(Long proveedorId);
}

