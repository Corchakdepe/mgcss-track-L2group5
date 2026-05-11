package com.mgcss.domain.model;

import java.time.LocalDate;

public class Solicitud {

    private final Long id;
    private Cliente cliente;
    private String descripcion;
    private LocalDate fechaCreacion;
    private EstadoSolicitud estadoSolicitud;
    private LocalDate fechaCierre;
    private Tecnico tecnicoAsignado;

    // MAYBE : a futuro si da tiempo que estadoSolicitud en el constructor sea ABIERTA por defecto, y cambie a EN_PROCESO una vez se le asigne un Tecnico?
    public Solicitud(Long id, Cliente cliente, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estadoSolicitud, LocalDate fechaCierre) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estadoSolicitud = estadoSolicitud;
        this.fechaCierre = fechaCierre;
        tecnicoAsignado = null;
    }

    // SOLO GETTERS (Cero Setters)
    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void cerrar() {
        if (estadoSolicitud != EstadoSolicitud.EN_PROCESO) {
            throw new IllegalStateException("No se puede cerrar si no esta en proceso.");
        }
        estadoSolicitud = EstadoSolicitud.CERRADA;
    }

    public void asignar(Tecnico tecnicoAsignado) {
        if (!tecnicoAsignado.estaActivo()) {
            throw new IllegalArgumentException("No se puede asignar un tecnico inactivo.");
        }
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public boolean tieneTecnicoAsignado() {
        return tecnicoAsignado != null;
    }
}
