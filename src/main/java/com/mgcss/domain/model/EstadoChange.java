package com.mgcss.domain.model;

import lombok.Getter;

// MAYBE hacer record class
@Getter
public class EstadoChange {
    private final EstadoSolicitud estadoAnterior;
    private final EstadoSolicitud estadoNuevo;
    // MAYBE: añadir campo que almacene el momento de cambio de estado

    public EstadoChange(EstadoSolicitud estadoAnterior, EstadoSolicitud estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

}
