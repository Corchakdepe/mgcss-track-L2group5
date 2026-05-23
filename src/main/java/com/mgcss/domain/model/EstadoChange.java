package com.mgcss.domain.model;

import lombok.Getter;

@Getter
public class EstadoChange {
    private final EstadoSolicitud estadoAnterior;
    private final EstadoSolicitud estadoNuevo;

    public EstadoChange(EstadoSolicitud estadoAnterior, EstadoSolicitud estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

}
