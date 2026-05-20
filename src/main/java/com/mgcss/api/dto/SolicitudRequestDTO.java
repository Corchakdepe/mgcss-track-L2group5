package com.mgcss.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudRequestDTO {

    private Long clienteId;

    private String descripcion;

    public SolicitudRequestDTO(Long clienteId, String descripcion) {
        this.clienteId = clienteId;
        this.descripcion = descripcion;
    }
}