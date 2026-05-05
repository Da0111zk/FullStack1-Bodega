package com.example.despachos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DespachoDTO {

    @NotNull(message = "El ID de producto es obligatorio")
    @Positive(message = "El ID de producto debe ser positivo")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "El destinatario es obligatorio")
    @Size(max = 100, message = "El destinatario no puede exceder 100 caracteres")
    private String destinatario;

    @Size(max = 300, message = "Las observaciones no pueden exceder 300 caracteres")
    private String observaciones;
}