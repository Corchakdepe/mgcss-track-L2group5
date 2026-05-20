package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para asignar un técnico a una solicitud")
public class AsignarTecnicoRequestDTO {

    @Schema(description = "Identificador del técnico", example = "1")
    private Long tecnicoId;

    public AsignarTecnicoRequestDTO() {
    }

    public AsignarTecnicoRequestDTO(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }
}
