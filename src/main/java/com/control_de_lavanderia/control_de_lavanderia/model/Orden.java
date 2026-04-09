package com.control_de_lavanderia.control_de_lavanderia.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Orden extends BaseEntity {

    @Column(name = "fecha_recibido", nullable = false, length = 80)
    private LocalDate fechaRecibido;

    @Column(name = "fecha_entrega_estimada", nullable = false, length = 40, unique = true)
    private LocalDate fechaEntregaEstimada;

    @Column(name = "total_prendas", nullable = false)
    private Integer total;

}
