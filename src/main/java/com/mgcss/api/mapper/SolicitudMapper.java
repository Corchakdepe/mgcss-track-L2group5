package com.mgcss.api.mapper;

import com.mgcss.api.dto.EstadoChangeDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.model.Solicitud;

import java.util.ArrayList;
import java.util.List;

public class SolicitudMapper {

    private SolicitudMapper() {
    }

    public static SolicitudResponseDTO toResponseDTO(Solicitud solicitud) {
        if (solicitud == null) return null;

        Long clienteId = null;
        String clienteNombre = null;
        if (solicitud.getCliente() != null) {
            clienteId = solicitud.getCliente().getId();
            clienteNombre = solicitud.getCliente().getNombre();
        }

        Long tecnicoId = null;
        String tecnicoNombre = null;
        if (solicitud.getTecnicoAsignado() != null) {
            tecnicoId = solicitud.getTecnicoAsignado().getId();
            tecnicoNombre = solicitud.getTecnicoAsignado().getNombre();
        }

        List<EstadoChangeDTO> historialDto;
        if (solicitud.getHistorial() != null) {
            historialDto = solicitud.getHistorial().stream().map(change -> new EstadoChangeDTO(change.getEstadoAnterior(), change.getEstadoNuevo())).toList();
        } else {
            historialDto = new ArrayList<>();
        }

        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(solicitud.getId());
        dto.setClienteId(clienteId);
        dto.setClienteNombre(clienteNombre);
        dto.setDescripcion(solicitud.getDescripcion());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setEstadoSolicitud(solicitud.getEstadoSolicitud());
        dto.setTecnicoId(tecnicoId);
        dto.setTecnicoNombre(tecnicoNombre);
        dto.setFechaCierre(solicitud.getFechaCierre());
        dto.setHistorial(historialDto);

        return dto;
    }
}
