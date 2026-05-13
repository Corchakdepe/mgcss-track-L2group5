package com.mgcss.domain.model;

import lombok.Getter;

public class Tecnico {
    // SOLO GETTERS (Cero Setters)
    @Getter
    private final Long id;
    @Getter
    private final String nombre;
    @Getter
    private final String especialidad;
    private boolean activo;

    public Tecnico(Long id, String nombre, String especialidad, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = activo;
    }

    public boolean estaActivo() {
        return activo;
    }

}
