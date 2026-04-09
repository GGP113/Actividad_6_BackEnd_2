package com.control_de_lavanderia.control_de_lavanderia.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Cliente extends BaseEntity{

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "telefono", nullable = false, length = 40, unique = true)
    private String telefono;

    @Column(name = "puntos_lealtad", nullable = false)
    private Integer puntosLealtad;



}
