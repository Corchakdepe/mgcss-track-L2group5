package com.mgcss.api.controller;

import com.mgcss.api.dto.TecnicoRequestDTO;
import com.mgcss.api.dto.TecnicoResponseDTO;
import com.mgcss.api.mapper.TecnicoMapper;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.service.TecnicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tecnicos")
@Tag(name = "Técnicos", description = "Controlador para la gestión de operarios técnicos y su carga de trabajo")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo técnico", description = "Crea un operario técnico en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Técnico registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos")
    })
    public ResponseEntity<TecnicoResponseDTO> crear(@Valid @RequestBody TecnicoRequestDTO request) {
        Tecnico nuevo = tecnicoService.crearTecnico(request.getNombre(), request.getEspecialidad());
        return ResponseEntity.status(HttpStatus.CREATED).body(TecnicoMapper.toResponseDTO(nuevo));
    }

}
