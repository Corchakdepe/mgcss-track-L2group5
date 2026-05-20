package com.mgcss.service;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.mgcss.domain.model.EstadoSolicitud.ABIERTA;

@Service
public class SolicitudService {
    private static final String SOLICITUD_NO_ENCONTRADA = "Solicitud no encontrada con id: ";

    private final SolicitudRepository solicitudRepository;
    private final TecnicoRepository tecnicoRepository;
    private final ClienteRepository clienteRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, TecnicoRepository tecnicoRepository, ClienteRepository clienteRepository) {
        this.solicitudRepository = solicitudRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public Solicitud crearSolicitud(Long clienteId, String descripcion) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + clienteId));
        Solicitud solicitud = new Solicitud(null, cliente, descripcion, LocalDate.now(), ABIERTA, null);
        return solicitudRepository.save(solicitud);
    }

    public Solicitud consultarSolicitud(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(SOLICITUD_NO_ENCONTRADA + id));
    }

    public void asignarTecnico(Long solicitudId, Long tecnicoId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada"));
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new IllegalArgumentException("Tecnico no encontrado"));

        solicitud.asignar(tecnico);
        solicitudRepository.save(solicitud);
    }

    public Solicitud cambiarEstado(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException(SOLICITUD_NO_ENCONTRADA + solicitudId));
        solicitud.cerrar();
        return solicitudRepository.save(solicitud);
    }

    public Solicitud reabrirSolicitud(Long solicitudId) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException(SOLICITUD_NO_ENCONTRADA + solicitudId));
        solicitud.reabrir();
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
    }
}
