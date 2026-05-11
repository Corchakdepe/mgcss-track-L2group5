package com.mgcss.domain.model;

public class Tecnico {
    private final Long id;
    private final String nombre;
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

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
