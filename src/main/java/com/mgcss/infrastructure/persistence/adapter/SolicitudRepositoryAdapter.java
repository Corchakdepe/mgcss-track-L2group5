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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SolicitudRepositoryAdapter implements SolicitudRepository {

    private final JpaSolicitudRepository jpaRepository;
    private final JpaClienteRepository clienteRepository;
    private final JpaTecnicoRepository tecnicoRepository;

    public SolicitudRepositoryAdapter(JpaSolicitudRepository jpaRepository,
                                      JpaClienteRepository clienteRepository,
                                      JpaTecnicoRepository tecnicoRepository) {
        this.jpaRepository = jpaRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Override
    public Solicitud save(Solicitud solicitud) {
        SolicitudEntity entity = new SolicitudEntity();
        entity.setId(solicitud.getId());

        if (solicitud.getCliente() != null && solicitud.getCliente().getId() != null) {
            ClienteEntity clienteEntity = clienteRepository.findById(solicitud.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            entity.setCliente(clienteEntity);
        } else {
            throw new IllegalArgumentException("La solicitud debe tener un cliente válido con ID asignado");
        }

        entity.setDescripcion(solicitud.getDescripcion());
        entity.setFechaCreacion(solicitud.getFechaCreacion());
        entity.setEstadoSolicitud(solicitud.getEstadoSolicitud());

        if (solicitud.getTecnicoAsignado() != null && solicitud.getTecnicoAsignado().getId() != null) {
            TecnicoEntity tecnicoEntity = tecnicoRepository.findById(solicitud.getTecnicoAsignado().getId())
                    .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
            entity.setTecnicoAsignado(tecnicoEntity);
        } else {
            entity.setTecnicoAsignado(null);
        }

        entity.setFechaCierre(solicitud.getFechaCierre());

        entity.getHistorial().clear();
        solicitud.getHistorial().forEach(change ->
                entity.getHistorial().add(new EstadoChangeEntity(change.getEstadoAnterior(), change.getEstadoNuevo()))
        );

        SolicitudEntity guardada = jpaRepository.save(entity);
        return mapToDomain(guardada);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Solicitud> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .toList();
    }

    private Solicitud mapToDomain(SolicitudEntity entity) {
        Cliente clienteDominio = null;
        if (entity.getCliente() != null) {
            clienteDominio = new Cliente(
                    entity.getCliente().getId(),
                    entity.getCliente().getNombre(),
                    entity.getCliente().getEmail(),
                    entity.getCliente().getTipoCliente()
            );
        }

        Tecnico tecnicoDominio = null;
        if (entity.getTecnicoAsignado() != null) {
            tecnicoDominio = new Tecnico(
                    entity.getTecnicoAsignado().getId(),
                    entity.getTecnicoAsignado().getNombre(),
                    entity.getTecnicoAsignado().getEspecialidad(),
                    entity.getTecnicoAsignado().isActivo()
            );
        }

        Solicitud solicitud = new Solicitud(
                entity.getId(),
                clienteDominio,
                entity.getDescripcion(),
                entity.getFechaCreacion(),
                entity.getEstadoSolicitud(),
                tecnicoDominio,
                entity.getFechaCierre()
        );

        if (entity.getHistorial() != null) {
            entity.getHistorial().forEach(change ->
                    solicitud.getHistorial().add(new EstadoChange(change.getEstadoAnterior(), change.getEstadoNuevo()))
            );
        }

        return solicitud;
    }
}
