package com.example.bodega.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recepciones")

public class Recepciones {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE  , generator="seq_recepciones")
    @SequenceGenerator(name="seq_recepciones", sequenceName="seq_recepciones", allocationSize=1)
    private Long id;


    @Column(name="PRODUCTO_ID", nullable = false)
    private Long productoId;

    @Column(name="PROVEEDOR_ID", nullable = false)
    private Long proveedorId;

    @Column(name="CANTIDAD", nullable = false)
    private int cantidad;

    @Column(name="FECHA_RECEPCION", nullable = false)
    private LocalDate fechaRecepcion;

    @Column(name="NUMERO_GUIA", unique = true, nullable = false, length=50)
    private String numeroGuia;

    @Column(name="OBSERVACIONES", length=300)
    private String observaciones;

}
