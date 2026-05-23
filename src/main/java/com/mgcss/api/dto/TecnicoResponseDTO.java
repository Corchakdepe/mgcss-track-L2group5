package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Información detallada del operario técnico devuelta por el sistema")
public class TecnicoResponseDTO extends CommonResponseDTO {

    @Schema(description = "Área de especialización técnica del operario", example = "Sistemas de Redes")
    private final String especialidad;

    @Schema(description = "Disponibilidad del tecnico para resolver incidencias", example = "true")
    private final boolean activo;

    public TecnicoResponseDTO(Long id, String nombre, String especialidad, boolean activo) {
        super(id, nombre);
        this.especialidad = especialidad;
        this.activo = activo;
    }

}
