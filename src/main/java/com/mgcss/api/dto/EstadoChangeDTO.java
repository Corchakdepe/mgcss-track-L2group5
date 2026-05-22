package com.mgcss.api.dto;

import com.mgcss.domain.model.EstadoSolicitud;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Registro detallado de un cambio de estado en el ciclo de vida de la solicitud")
public class EstadoChangeDTO {

    @Schema(description = "Estado previo la solicitud antes de la modificación")
    private final EstadoSolicitud estadoAnterior;

    @Schema(description = "Nuevo estado asignado a la solicitud tras completarse la operación")
    private final EstadoSolicitud estadoNuevo;

    public EstadoChangeDTO(EstadoSolicitud estadoAnterior, EstadoSolicitud estadoNuevo) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }
}