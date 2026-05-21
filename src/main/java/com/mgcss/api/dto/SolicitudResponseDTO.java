package com.mgcss.api.dto;

import com.mgcss.domain.model.EstadoSolicitud;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SolicitudResponseDTO {

    private Long id;

    private Long clienteId;

    private String clienteNombre;

    private String descripcion;

    private LocalDate fechaCreacion;

    private EstadoSolicitud estadoSolicitud;

    private Long tecnicoId;

    private String tecnicoNombre;

    private LocalDate fechaCierre;

    private List<EstadoChangeDTO> historial;

}