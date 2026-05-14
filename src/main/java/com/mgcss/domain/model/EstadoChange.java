package com.mgcss.domain.model;

public class EstadoChange {
    private EstadoSolicitud estadoAnterior;
    private EstadoSolicitud estadoNuevo;

    public EstadoChange(EstadoSolicitud estadoSolicitud, EstadoSolicitud estadoSolicitud1) {
        this.estadoAnterior = estadoSolicitud;
        this.estadoNuevo = estadoSolicitud1;
    }

    public EstadoSolicitud getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoSolicitud getEstadoNuevo() {
        return estadoNuevo;
    }
}
