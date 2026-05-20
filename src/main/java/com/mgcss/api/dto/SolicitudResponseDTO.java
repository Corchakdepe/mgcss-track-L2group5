package com.mgcss.api.dto;

import com.mgcss.domain.model.EstadoSolicitud;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Datos de respuesta de una solicitud")
public class SolicitudResponseDTO {

    @Schema(description = "Identificador único de la solicitud", example = "1")
    private Long id;

    @Schema(description = "Descripción de la incidencia", example = "Incidencia en la red interna")
    private String descripcion;

    @Schema(description = "Estado actual de la solicitud", example = "ABIERTA")
    private EstadoSolicitud estado;

    @Schema(description = "Fecha de creación", example = "2026-05-20")
    private LocalDate fechaCreacion;

    @Schema(description = "Fecha de cierre (si está cerrada)", example = "2026-05-21")
    private LocalDate fechaCierre;

    @Schema(description = "Nombre del cliente", example = "Juan Pérez")
    private String clienteNombre;

    @Schema(description = "Nombre del técnico asignado (si tiene)", example = "María López")
    private String tecnicoNombre;

    public SolicitudResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDate fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getTecnicoNombre() {
        return tecnicoNombre;
    }

    public void setTecnicoNombre(String tecnicoNombre) {
        this.tecnicoNombre = tecnicoNombre;
    }
}
