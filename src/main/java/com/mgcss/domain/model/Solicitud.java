package com.mgcss.domain.model;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private final List<EstadoChange> historial;
    @Getter
    private EstadoSolicitud estadoSolicitud;
    @Getter
    private LocalDate fechaCierre;
    @Getter
    private Tecnico tecnicoAsignado;

    public Solicitud(Long id, Cliente cliente, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estadoSolicitud, LocalDate fechaCierre) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estadoSolicitud = estadoSolicitud;
        this.fechaCierre = fechaCierre;
        tecnicoAsignado = null;
        historial = new ArrayList<>();
    }

    public Solicitud(Long id, Cliente cliente, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estadoSolicitud, LocalDate fechaCierre, Tecnico tecnicoAsignado, List<EstadoChange> historial) {
        this.id = id;
        this.cliente = cliente;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estadoSolicitud = estadoSolicitud;
        this.fechaCierre = fechaCierre;
        this.tecnicoAsignado = tecnicoAsignado;
        this.historial = new ArrayList<>(historial);
    }

    public void cerrar() {
        if (estadoSolicitud != EstadoSolicitud.EN_PROCESO) {
            throw new IllegalStateException("No se puede cerrar si no esta en proceso.");
        }
        var estadoAnterior = this.estadoSolicitud;


        estadoSolicitud = EstadoSolicitud.CERRADA;
        fechaCierre = LocalDate.now();

        cambiarEstado(estadoAnterior, estadoSolicitud);
    }

    public void asignar(Tecnico tecnicoAsignado) {
        if (estadoSolicitud != EstadoSolicitud.ABIERTA) {
            throw new IllegalStateException("Solo se puede asignar técnicos a solicitudes abiertas.");
        }
        if (!tecnicoAsignado.estaActivo()) {
            throw new IllegalArgumentException("No se puede asignar un tecnico inactivo.");
        }
        var estadoAnterior = this.estadoSolicitud;

        this.tecnicoAsignado = tecnicoAsignado;
        estadoSolicitud = EstadoSolicitud.EN_PROCESO;

        cambiarEstado(estadoAnterior, estadoSolicitud);
    }

    public void reabrir() {
        if (estadoSolicitud != EstadoSolicitud.CERRADA) {
            throw new IllegalStateException("Solo se pueden reabrir solicitudes que estén cerradas");
        }
        var estadoAnterior = this.estadoSolicitud;

        estadoSolicitud = EstadoSolicitud.EN_PROCESO;
        fechaCierre = null;

        cambiarEstado(estadoAnterior, estadoSolicitud);
    }

    public boolean tieneTecnicoAsignado() {
        return tecnicoAsignado != null;
    }

    private void cambiarEstado(EstadoSolicitud anterior, EstadoSolicitud nuevo) {
        historial.add(new EstadoChange(anterior, nuevo));
    }
}
