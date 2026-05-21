package com.mgcss.service;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.EstadoSolicitud;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;

import java.time.LocalDate;
import java.util.List;

public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, TecnicoRepository tecnicoRepository, ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId).orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId).orElseThrow(() -> new IllegalArgumentException("Tecnico no encontrado"));

        solicitud.asignar(tecnico);
        solicitudRepository.save(solicitud);
    }

    public Solicitud crearSolicitud(Long clienteId, String descripcion) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Solicitud solicitud = new Solicitud(null, cliente, descripcion, LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud buscarPorId(Long solicitudId) {
        return solicitudRepository.findById(solicitudId).orElseThrow(() -> new IllegalArgumentException("Solicitud con ID: " + solicitudId + " no encontrada"));
    }

    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAll();
    }

    public void cerrarSolicitud(Long id) {
        //TODO
    }
}
