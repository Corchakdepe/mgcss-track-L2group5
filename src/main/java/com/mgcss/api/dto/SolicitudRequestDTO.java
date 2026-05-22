package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Modelo para la creación de una nueva solicitud")
public class SolicitudRequestDTO {

    @NotNull(message = "El identificador del cliente es obligatorio")
    @Schema(description = "ID del cliente que genera la solicitud", example = "1")
    private Long clienteId;

    @NotBlank(message = "La descripción de la incidencia es obligatoria")
    @Schema(description = "Descripción detallada del problema o incidencia", example = "El servidor de base de datos no responde")
    private String descripcion;

}