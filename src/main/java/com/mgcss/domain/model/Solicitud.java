package com.mgcss.domain.model;

import lombok.Getter;

import java.time.LocalDate;

public class Solicitud {

    // SOLO GETTERS (Cero Setters)
    @Getter
    private final Long id;
    @Getter
    private final Cliente cliente;
    @Getter
    private final String descripcion;
    @Getter
    private final LocalDate fechaCreacion;
    @Getter
    private EstadoSolicitud estadoSolicitud;
    @Getter
    private final LocalDate fechaCierre;
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

    public void reabrir() { // Por implementar
    }

    public boolean tieneTecnicoAsignado() {
        return tecnicoAsignado != null;
    }


}
