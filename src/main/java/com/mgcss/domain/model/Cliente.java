package com.mgcss.domain.model;

public class Cliente {

    private final Long id;
    private String nombre;
    private String email;
    private TipoCliente tipoCliente;

    public Cliente(Long id, String nombre, String email, TipoCliente tipoCliente) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.tipoCliente = tipoCliente;
    }

    // SOLO GETTERS (Cero Setters)
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
}
