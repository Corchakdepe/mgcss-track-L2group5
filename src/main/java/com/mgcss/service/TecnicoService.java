package com.mgcss.service;

import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;

public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    public Tecnico crearTecnico(String nombre, String especialidad) {
        Tecnico tecnico = new Tecnico(null, nombre, especialidad, true);
        return tecnicoRepository.save(tecnico);
    }
}
