package com.mgcss.api.controller;

import com.mgcss.api.dto.AsignarTecnicoRequestDTO;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
@Tag(name = "Solicitudes", description = "API para la gestión de solicitudes de incidencias")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud de incidencia")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud creada correctamente",
                    content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<SolicitudResponseDTO> crear(@RequestBody SolicitudRequestDTO request) {
        Solicitud solicitud = solicitudService.crearSolicitud(request.getClienteId(), request.getDescripcion());
        return ResponseEntity.ok(toResponseDTO(solicitud));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar solicitud", description = "Obtiene los detalles de una solicitud por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada",
                    content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<SolicitudResponseDTO> consultar(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.consultarSolicitud(id);
        return ResponseEntity.ok(toResponseDTO(solicitud));
    }

    @GetMapping
    @Operation(summary = "Listar solicitudes", description = "Obtiene todas las solicitudes registradas")
    @ApiResponse(responseCode = "200", description = "Lista de solicitudes",
            content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class)))
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        List<Solicitud> solicitudes = solicitudService.listarSolicitudes();
        List<SolicitudResponseDTO> response = solicitudes.stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/asignar")
    @Operation(summary = "Asignar técnico", description = "Asigna un técnico a una solicitud abierta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Técnico asignado correctamente",
                    content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud no está abierta o técnico inactivo"),
            @ApiResponse(responseCode = "404", description = "Solicitud o técnico no encontrado")
    })
    public ResponseEntity<SolicitudResponseDTO> asignarTecnico(
            @PathVariable Long id,
            @RequestBody AsignarTecnicoRequestDTO request) {
        solicitudService.asignarTecnico(id, request.getTecnicoId());
        Solicitud solicitud = solicitudService.consultarSolicitud(id);
        return ResponseEntity.ok(toResponseDTO(solicitud));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado", description = "Cierra una solicitud en proceso")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "La solicitud no está en proceso"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<SolicitudResponseDTO> cambiarEstado(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.cambiarEstado(id);
        return ResponseEntity.ok(toResponseDTO(solicitud));
    }

    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Reabrir solicitud", description = "Reabre una solicitud que estaba cerrada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud reabierta correctamente",
                    content = @Content(schema = @Schema(implementation = SolicitudResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "La solicitud no está cerrada"),
            @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    public ResponseEntity<SolicitudResponseDTO> reabrir(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.reabrirSolicitud(id);
        return ResponseEntity.ok(toResponseDTO(solicitud));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    private SolicitudResponseDTO toResponseDTO(Solicitud solicitud) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setId(solicitud.getId());
        dto.setDescripcion(solicitud.getDescripcion());
        dto.setEstado(solicitud.getEstadoSolicitud());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setFechaCierre(solicitud.getFechaCierre());
        dto.setClienteNombre(solicitud.getCliente().getNombre());
        if (solicitud.tieneTecnicoAsignado()) {
            dto.setTecnicoNombre(solicitud.getTecnicoAsignado().getNombre());
        }
        return dto;
    }
}
