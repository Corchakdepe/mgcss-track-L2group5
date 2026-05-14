package com.mgcss.infrastructure.persistence.entity;

import com.mgcss.domain.model.EstadoSolicitud;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class EstadoChangeEntity {

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoNuevo;

    public EstadoChangeEntity() {
    }

    public EstadoChangeEntity(EstadoSolicitud estadoAnterior, EstadoSolicitud estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

    public EstadoSolicitud getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoSolicitud getEstadoNuevo() {
        return estadoNuevo;
    }
}