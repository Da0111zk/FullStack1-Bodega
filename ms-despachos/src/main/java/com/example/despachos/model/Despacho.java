package com.example.despachos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DESPACHOS")
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_despachos")
    @SequenceGenerator(name = "seq_despachos", sequenceName = "SEQ_DESPACHOS", allocationSize = 1)
    private Long id;

    @Column(name = "PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "FECHA_DESPACHO", nullable = false)
    private LocalDate fechaDespacho;

    @Column(name = "DESTINATARIO", nullable = false, length = 100)
    private String destinatario;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(name = "OBSERVACIONES", length = 300)
    private String observaciones;
}