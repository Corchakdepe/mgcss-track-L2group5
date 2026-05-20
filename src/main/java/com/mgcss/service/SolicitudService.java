package com.mgcss.service;

import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;

public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, TecnicoRepository tecnicoRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId).orElseThrow(() -> new IllegalArgumentException("Tecnico no encontrado"));

        solicitud.asignar(tecnico);
        solicitudRepository.save(solicitud);
    }

    public Solicitud crearSolicitud(Long id, String s) {
        // @TODO
    }
}
