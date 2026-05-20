package com.mgcss.api.dto;

import com.mgcss.domain.model.EstadoSolicitud;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
public class SolicitudResponseDTO {

    private final Long id;

    private final Long clienteId;

    private final String clienteNombre;

    private final String descripcion;

    private final LocalDate fechaCreacion;

    private final EstadoSolicitud estadoSolicitud;

    private final Long tecnicoId;

    private final String tecnicoNombre;

    private final LocalDate fechaCierre;

    private final List<EstadoChangeDTO> historial;

    public SolicitudResponseDTO(Long id, Long clienteId, String clienteNombre, String descripcion, LocalDate fechaCreacion, EstadoSolicitud estadoSolicitud, Long tecnicoId, String tecnicoNombre, LocalDate fechaCierre, List<EstadoChangeDTO> historial) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estadoSolicitud = estadoSolicitud;
        this.tecnicoId = tecnicoId;
        this.tecnicoNombre = tecnicoNombre;
        this.fechaCierre = fechaCierre;
        // Evitamos fugas de mutabilidad encapsulando la lista externa
        this.historial = historial != null ? List.copyOf(historial) : Collections.emptyList();
    }

}