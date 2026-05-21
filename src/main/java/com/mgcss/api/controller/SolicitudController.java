package com.mgcss.api.controller;

import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.api.dto.SolicitudResponseDTO;
import com.mgcss.api.mapper.SolicitudMapper;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.service.SolicitudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crear(@RequestBody SolicitudRequestDTO requestDTO) {
        Solicitud creada = solicitudService.crearSolicitud(requestDTO.getClienteId(), requestDTO.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudMapper.toResponseDTO(creada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> consultar(@PathVariable Long id) {
        Solicitud solicitud = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(SolicitudMapper.toResponseDTO(solicitud));
    }

    @PutMapping("/{id}/tecnico")
    public ResponseEntity<SolicitudResponseDTO> asignarTecnico(@PathVariable Long id, @RequestParam Long tecnicoId) {
        solicitudService.asignarTecnico(id, tecnicoId);
        Solicitud actualizada = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(SolicitudMapper.toResponseDTO(actualizada));
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<SolicitudResponseDTO> cerrar(@PathVariable Long id) {
        solicitudService.cerrarSolicitud(id);
        Solicitud actualizada = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(SolicitudMapper.toResponseDTO(actualizada));
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<SolicitudResponseDTO> reabrir(@PathVariable Long id) {
        solicitudService.reabrirSolicitud(id);
        Solicitud actualizada = solicitudService.buscarPorId(id);
        return ResponseEntity.ok(SolicitudMapper.toResponseDTO(actualizada));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listarSolicitudes() {
        List<SolicitudResponseDTO> listaRespuestas = solicitudService.listarTodas().stream()
                .map(SolicitudMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(listaRespuestas);
    }
}
