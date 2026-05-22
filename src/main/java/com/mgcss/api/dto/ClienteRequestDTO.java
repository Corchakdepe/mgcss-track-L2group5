package com.mgcss.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Datos requeridos para registrar un nuevo cliente en el sistema")
public class ClienteRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre completo del cliente", example = "Juan Perez")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    @Schema(description = "Dirección de correo electrónico de contacto", example = "juan.perez@mgcss.com")
    private String email;

}
