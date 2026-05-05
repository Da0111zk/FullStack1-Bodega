package com.example.despachos.repository;

import com.example.despachos.model.Despacho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DespachoRepository extends JpaRepository<Despacho, Long> {
    List<Despacho> findByEstado(String estado);
    List<Despacho> findByProductoId(Long productoId);
}