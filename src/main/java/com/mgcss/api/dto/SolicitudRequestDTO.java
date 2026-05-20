package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos de entrada para crear una solicitud")
public class SolicitudRequestDTO {

    @Schema(description = "Identificador del cliente", example = "1")
    private Long clienteId;

    @Schema(description = "Descripción de la incidencia", example = "Incidencia en la red interna")
    private String descripcion;

    public SolicitudRequestDTO(Long clienteId, String descripcion) {
        this.clienteId = clienteId;
        this.descripcion = descripcion;
    }
}
