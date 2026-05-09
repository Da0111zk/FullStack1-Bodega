package com.example.ms_categorias.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_categorias.modelo.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Aquí puedes añadir métodos personalizados en el futuro, ej: findByNombre
}