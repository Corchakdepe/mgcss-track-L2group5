package com.mgcss.api.controller;

import com.mgcss.api.dto.ClienteRequestDTO;
import com.mgcss.api.dto.ClienteResponseDTO;
import com.mgcss.api.mapper.ClienteMapper;
import com.mgcss.domain.model.Cliente;
import com.mgcss.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Controlador para el registro de clientes y la gestión de su estado en el sistema")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente", description = "Crea un cliente de manera persistentente en el sistema utilizando su nombre y dirección de correo electrónico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada incorrectos o formato de email inválido")
    })
    public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO request) { // Arquitectura: @Valid integrado
        Cliente nuevo = clienteService.crearCliente(request.getNombre(), request.getEmail());
        return new ResponseEntity<>(ClienteMapper.toResponseDTO(nuevo), HttpStatus.CREATED);
    }

}