package com.control_de_lavanderia.control_de_lavanderia.service;

import com.control_de_lavanderia.control_de_lavanderia.model.Cliente;
import com.control_de_lavanderia.control_de_lavanderia.model.Orden;
import com.control_de_lavanderia.control_de_lavanderia.model.Prenda;
import com.control_de_lavanderia.control_de_lavanderia.model.enums.TipoPrenda;
import com.control_de_lavanderia.control_de_lavanderia.repository.ClienteRepository;
import com.control_de_lavanderia.control_de_lavanderia.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LavanderiaService {

    // Precios fijos basados en el diseño propuesto
    private static final double PRECIO_CAMISA = 15000.0;
    private static final double PRECIO_PANTALON = 20000.0;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Procesa una nueva orden aplicando la lógica de negocio del sistema.
     * @param orden La orden con su cliente y sus respectivas prendas
     * @return La orden ya calculada
     */
    @Transactional
    public Orden procesarNuevaOrden(Orden orden) {
        // Cargar al cliente real desde la base de datos (por si viene solo con ID desde Postman)
        Cliente cliente = clienteRepository.findById(orden.getCliente().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));
        orden.setCliente(cliente);
        List<Prenda> prendas = orden.getPrendas();

        // 1. Validar que no se acepten más de 20 prendas en un solo pedido
        if (prendas == null || prendas.isEmpty()) {
            throw new IllegalArgumentException("La orden debe tener al menos una prenda.");
        }
        if (prendas.size() > 20) {
            throw new IllegalArgumentException("No se aceptan más de 20 prendas en un solo pedido.");
        }

        // Configurar la fecha de recibido (asumimos la actual si no viene en el parámetro)
        if (orden.getFechaRecibido() == null) {
            orden.setFechaRecibido(LocalDateTime.now());
        }

        // 2. Asignar fecha de entrega: +24 horas si < 5 prendas, +48 horas si >= 5 prendas
        if (prendas.size() < 5) {
            orden.setFechaEntregaEstimada(orden.getFechaRecibido().plusHours(24));
        } else {
            orden.setFechaEntregaEstimada(orden.getFechaRecibido().plusHours(48));
        }

        // 3. Calcula el total basado en tarifa fija por tipo de prenda
        double total = 0.0;
        double prendaMasBarata = Double.MAX_VALUE;

        for (Prenda prenda : prendas) {
            double precioActual = 0.0;
            if (prenda.getTipo() == TipoPrenda.CAMISA) {
                precioActual = PRECIO_CAMISA;
            } else if (prenda.getTipo() == TipoPrenda.PANTALON) {
                precioActual = PRECIO_PANTALON;
            }
            
            total += precioActual;
            
            // Encontrar el precio de la prenda más barata de la lista
            if (precioActual < prendaMasBarata) {
                prendaMasBarata = precioActual;
            }
        }

        // 4. Si el cliente tiene 50 puntos o más, aplicar descuento a la prenda más barata
        if (cliente.getPuntosLealtad() != null && cliente.getPuntosLealtad() >= 50) {
            total -= prendaMasBarata;
            // Restar los 50 puntos canjeados al cliente
            cliente.setPuntosLealtad(cliente.getPuntosLealtad() - 50);
        }

        orden.setTotal(total);

        // 5. Sumar puntos de lealtad al cliente por cada 10,000 pesos
        int puntosGanados = (int) (total / 10000);
        int puntosActuales = cliente.getPuntosLealtad() != null ? cliente.getPuntosLealtad() : 0;
        cliente.setPuntosLealtad(puntosActuales + puntosGanados);

        // Guardamos las entidades en base de datos
        clienteRepository.save(cliente);
        return ordenRepository.save(orden);
    }
}
