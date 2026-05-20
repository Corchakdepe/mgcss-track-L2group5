package com.mgcss.api.dto;

import com.mgcss.domain.model.EstadoSolicitud;
import lombok.Getter;

@Getter
public class EstadoChangeDTO {

    private final EstadoSolicitud estadoAnterior;

    private final EstadoSolicitud estadoNuevo;

    public EstadoChangeDTO(EstadoSolicitud estadoAnterior, EstadoSolicitud estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }
}