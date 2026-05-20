package com.mgcss.infrastructure.persistence.adapter;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.EstadoChange;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.infrastructure.persistence.entity.ClienteEntity;
import com.mgcss.infrastructure.persistence.entity.EstadoChangeEntity;
import com.mgcss.infrastructure.persistence.entity.SolicitudEntity;
import com.mgcss.infrastructure.persistence.entity.TecnicoEntity;
import com.mgcss.infrastructure.persistence.repository.JpaClienteRepository;
import com.mgcss.infrastructure.persistence.repository.JpaSolicitudRepository;
import com.mgcss.infrastructure.persistence.repository.JpaTecnicoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final JpaSolicitudRepository jpaSolicitudRepository;
    private final JpaClienteRepository jpaClienteRepository;
    private final JpaTecnicoRepository jpaTecnicoRepository;

    public SolicitudRepositoryAdapter(JpaSolicitudRepository jpaSolicitudRepository,
                                      JpaClienteRepository jpaClienteRepository,
                                      JpaTecnicoRepository jpaTecnicoRepository) {
        this.jpaSolicitudRepository = jpaSolicitudRepository;
        this.jpaClienteRepository = jpaClienteRepository;
        this.jpaTecnicoRepository = jpaTecnicoRepository;
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        SolicitudEntity entity = toEntity(solicitud);
        SolicitudEntity saved = jpaSolicitudRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return jpaSolicitudRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Solicitud> findAll() {
        return jpaSolicitudRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private Solicitud toDomain(SolicitudEntity entity) {
        Cliente cliente = new Cliente(
                entity.getCliente().getId(),
                entity.getCliente().getNombre(),
                entity.getCliente().getEmail(),
                entity.getCliente().getTipoCliente()
        );

        Tecnico tecnico = null;
        if (entity.getTecnicoAsignado() != null) {
            tecnico = new Tecnico(
                    entity.getTecnicoAsignado().getId(),
                    entity.getTecnicoAsignado().getNombre(),
                    entity.getTecnicoAsignado().getEspecialidad(),
                    entity.getTecnicoAsignado().isActivo()
            );
        }

        List<EstadoChange> historial = entity.getHistorial().stream()
                .map(ce -> new EstadoChange(ce.getEstadoAnterior(), ce.getEstadoNuevo()))
                .toList();

        return new Solicitud(
                entity.getId(),
                cliente,
                entity.getDescripcion(),
                entity.getFechaCreacion(),
                entity.getEstadoSolicitud(),
                entity.getFechaCierre(),
                new Solicitud.DatosAdicionales(tecnico, historial)
        );
    }

    private SolicitudEntity toEntity(Solicitud solicitud) {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(solicitud.getId());

        ClienteEntity clienteEntity = jpaClienteRepository
                .findById(solicitud.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + solicitud.getCliente().getId()));
        entity.setCliente(clienteEntity);
        entity.setDescripcion(solicitud.getDescripcion());
        entity.setFechaCreacion(solicitud.getFechaCreacion());
        entity.setEstadoSolicitud(solicitud.getEstadoSolicitud());
        entity.setFechaCierre(solicitud.getFechaCierre());

        if (solicitud.getTecnicoAsignado() != null) {
            TecnicoEntity tecnicoEntity = jpaTecnicoRepository
                    .findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Tecnico no encontrado con id: " + solicitud.getTecnicoAsignado().getId()));
            entity.setTecnicoAsignado(tecnicoEntity);
        }

        List<EstadoChangeEntity> historialEntity = solicitud.getHistorial().stream()
                .map(ce -> new EstadoChangeEntity(ce.getEstadoAnterior(), ce.getEstadoNuevo()))
                .toList();
        entity.setHistorial(historialEntity);

        return entity;
    }
}
