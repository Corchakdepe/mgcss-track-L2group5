package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Datos para asignar un técnico a una solicitud")
public class AsignarTecnicoRequestDTO {

    @Schema(description = "Identificador del técnico", example = "1")
    private Long tecnicoId;

    public AsignarTecnicoRequestDTO(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }
}
