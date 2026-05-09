package com.example.ms_producto.modelo;

import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombre;

    @NotBlank(message = "El SKU no puede estar vacío")
    @Column(nullable = false, unique = true, length = 20)
    private String sku;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotBlank(message = "El estado no puede estar vacío")
    @Column(nullable = false, length = 20)
    private String activo;

    
    @NotNull(message = "El id de la categoría es obligatorio")
    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;
}