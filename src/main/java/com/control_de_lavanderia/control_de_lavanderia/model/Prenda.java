package com.control_de_lavanderia.control_de_lavanderia.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.control_de_lavanderia.control_de_lavanderia.model.enums.*;


@Entity
@Table(name = "prendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Prenda extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoPrenda tipo;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "instrucciones_especiales", length = 255)
    private String instruccionesEspeciales;

    @ManyToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

}
