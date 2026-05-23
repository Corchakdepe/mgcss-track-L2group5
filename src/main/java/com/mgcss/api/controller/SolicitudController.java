package com.mgcss.api.controller;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.api.mapper.SolicitudMapper;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.service.SolicitudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitudes")
@Tag(name = "Solicitudes", description = "Controlador del ciclo de vida de las solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva solicitud", description = "Abre una nueva solicitud de mantenimiento asociada a un cliente. Debe contener una breve descripcion del problema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitud creada con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "El cliente proporcionado no existe en el sistema")
    })
    public ResponseEntity<SolicitudResponseDTO> crear(@Valid @RequestBody SolicitudRequestDTO requestDTO) {
        Solicitud creada = solicitudService.crearSolicitud(requestDTO.getClienteId(), requestDTO.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudMapper.toResponseDTO(creada));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una solicitud dado su ID", description = "Recupera los detalles completos de una solicitud específica a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud encontrada y devuelta con éxito"),
            @ApiResponse(responseCode = "404", description = "La solicitud con el ID proporcionado no se encuentra en el sistema")
    })
    public ResponseEntity<SolicitudResponseDTO> consultar(
            @Parameter(description = "Identificador único de la solicitud a consultar", example = "1") @PathVariable Long id) {
        Solicitud solicitud = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(SolicitudMapper.toResponseDTO(solicitud));
    }

    @PutMapping("/{id}/tecnico")
    @Operation(summary = "Asignar un técnico", description = "Asigna un tecnico activo a la solicitud, la cual pasará a estar en proceso de resolucion.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Técnico asignado correctamente. Estado mutado a EN_PROCESO"),
            @ApiResponse(responseCode = "400", description = "Regla de negocio violada (ej. técnico inactivo o solicitud cerrada)"),
            @ApiResponse(responseCode = "404", description = "La solicitud o el técnico indicados no existen")
    })
    public ResponseEntity<Void> asignarTecnico(
            @Parameter(description = "ID de la solicitud", example = "1") @PathVariable Long id,
            @Parameter(description = "ID del técnico que se hará cargo", example = "2") @RequestParam Long tecnicoId) {
        solicitudService.asignarTecnico(id, tecnicoId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/{id}/cerrar")
    @Operation(summary = "Cerrar una solicitud", description = "Cambia el estado de una solicitud a CERRADA. Requiere obligatoriamente que la solicitud esté EN_PROCESO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud cerrada con éxito"),
            @ApiResponse(responseCode = "400", description = "Intento de cierre ilegal desde un estado no permitido (ej. ABIERTA)"),
            @ApiResponse(responseCode = "404", description = "La solicitud con el ID proporcionado no existe en el sistema")
    })
    public ResponseEntity<Void> cerrar(
            @Parameter(description = "ID de la solicitud que se desea cerrar", example = "1") @PathVariable Long id) {
        solicitudService.cerrarSolicitud(id);
        return ResponseEntity.ok(null);
    }

    @PatchMapping("/{id}/reabrir")
    @Operation(summary = "Reabrir una solicitud cerrada", description = "Permite la reapertura manual de una incidencia previamente CERRADA, devolviéndola limpiamente al estado EN_PROCESO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitud reabierta con éxito"),
            @ApiResponse(responseCode = "400", description = "La solicitud no se encontraba en estado CERRADA"),
            @ApiResponse(responseCode = "404", description = "La solicitud especificada no existe")
    })
    public ResponseEntity<Void> reabrir(
            @Parameter(description = "ID de la solicitud a reabrir", example = "1") @PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    @Operation(summary = "Listar todas las solicitudes", description = "Retorna una lista completa con el histórico de solicitudes registradas en la aplicación.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    public ResponseEntity<List<SolicitudResponseDTO>> listarSolicitudes() {
        List<SolicitudResponseDTO> listaRespuestas = solicitudService.listarTodas().stream()
                .map(SolicitudMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(listaRespuestas);
    }
}
