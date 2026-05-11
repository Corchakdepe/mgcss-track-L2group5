package com.mgcss.domain.model;

import lombok.Getter;

@Getter
public class Cliente {

    // SOLO GETTERS (Cero Setters)
    private final Long id;
    private final String nombre;
    private final String email;
    private final TipoCliente tipoCliente;

    public Cliente(Long id, String nombre, String email, TipoCliente tipoCliente) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
    }

}
