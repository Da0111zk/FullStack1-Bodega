package com.example.bodega.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RecepcionesDTO {

    @NotNull(message="El ID de producto es obligatorio")
    @Positive(message="El ID de producto debe ser un número positivo")
    private Long productoId;

    @NotNull(message="El ID de proveedor es obligatorio")
    @Positive(message="El ID de proveedor debe ser un número positivo")
    private Long proveedorId;

    @NotNull(message="La cantidad es obligatoria")
    @Min(value=1, message="La cantidad debe ser un número positivo")
    private Integer cantidad;


    @NotBlank(message="El número de guía es obligatorio")
    @Size(max=50, message="El número de guía no puede exceder los 50 caracteres")
    private String numeroGuia;

    @Size(max=300, message="Las observaciones no pueden exceder los 300 caracteres")
    private String observaciones;
}
