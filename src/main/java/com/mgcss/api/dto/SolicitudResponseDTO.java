package com.mgcss.api.dto;

import com.mgcss.domain.model.EstadoSolicitud;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Información detallada de la solicitud devuelta por el sistema")
public class SolicitudResponseDTO {

    @Schema(description = "Identificador único de la solicitud generado por la base de datos", example = "1")
    private Long id;

    @Schema(description = "ID del cliente asociado a la solicitud", example = "1")
    private Long clienteId;

    @Schema(description = "Nombre completo del cliente", example = "Pedro Sanchez")
    private String clienteNombre;

    @Schema(description = "Texto explicativo con el detalle del problema técnico", example = "El servidor de base de datos no responde")
    private String descripcion;

    @Schema(description = "Fecha en la que se registró la incidencia")
    private LocalDate fechaCreacion;

    @Schema(description = "Estado actual del ciclo de vida de la solicitud")
    private EstadoSolicitud estadoSolicitud;

    @Schema(description = "ID del técnico asignado para resolver la incidencia", example = "2")
    private Long tecnicoId;

    @Schema(description = "Nombre completo del técnico asignado", example = "Jesus Diaz")
    private String tecnicoNombre;

    @Schema(description = "Fecha en la que se cerró la solicitud si procede")
    private LocalDate fechaCierre;

    @Schema(description = "Historial completo con todas las transiciones de estado sufridas por la solicitud")
    private List<EstadoChangeDTO> historial;

}