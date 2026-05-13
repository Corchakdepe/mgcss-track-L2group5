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
    private LocalDate fechaCierre;
    private Tecnico tecnicoAsignado;

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
        if (estadoSolicitud != EstadoSolicitud.ABIERTA) {
            throw new IllegalStateException("Solo se puede asignar técnicos a solicitudes abiertas.");
        }
        if (!tecnicoAsignado.estaActivo()) {
            throw new IllegalArgumentException("No se puede asignar un tecnico inactivo.");
        }

        this.tecnicoAsignado = tecnicoAsignado;
        estadoSolicitud = EstadoSolicitud.EN_PROCESO;
    }

    public void reabrir() {
        if (estadoSolicitud != EstadoSolicitud.CERRADA) {
            throw new IllegalStateException("Solo se pueden reabrir solicitudes que estén cerradas");
        }
        estadoSolicitud = EstadoSolicitud.EN_PROCESO;
        fechaCierre = null;
    }

    public boolean tieneTecnicoAsignado() {
        return tecnicoAsignado != null;
    }


}
