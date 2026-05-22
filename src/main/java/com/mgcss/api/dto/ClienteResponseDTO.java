package com.mgcss.api.dto;

import com.mgcss.domain.model.TipoCliente;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Información detallada del cliente devuelta por el sistema")
public class ClienteResponseDTO extends CommonResponseDTO {

    @Schema(description = "Dirección de correo electrónico del cliente", example = "juan.perez@mgcss.com")
    private final String email;

    @Schema(description = "Tipo o categoría de prioridad asignada al cliente dentro del dominio")
    private final TipoCliente tipoCliente;

    public ClienteResponseDTO() {
        super(0L, "");
        email = "";
        tipoCliente = null;
    }

    public ClienteResponseDTO(Long id, String nombre, String email, TipoCliente tipoCliente) {
        super(id, nombre);
        this.email = email;
        this.tipoCliente = tipoCliente;
    }
}
