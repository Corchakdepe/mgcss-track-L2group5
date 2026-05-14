package com.mgcss.integration;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.infrastructure.persistence.entity.ClienteEntity;
import com.mgcss.infrastructure.persistence.entity.EstadoChangeEntity;
import com.mgcss.infrastructure.persistence.entity.SolicitudEntity;
import com.mgcss.infrastructure.persistence.entity.TecnicoEntity;
import com.mgcss.infrastructure.persistence.repository.JpaClienteRepository;
import com.mgcss.infrastructure.persistence.repository.JpaSolicitudRepository;
import com.mgcss.infrastructure.persistence.repository.JpaTecnicoRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static com.mgcss.domain.model.EstadoSolicitud.ABIERTA;
import static com.mgcss.domain.model.TipoCliente.STANDARD;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Tag("integration")
class JpaSolicitudRepositoryIT {

    @Autowired
    private JpaSolicitudRepository repositorySolicitud;
    @Autowired
    private JpaClienteRepository repositoryCliente;
    @Autowired
    private JpaTecnicoRepository repositoryTecnico;

    @Test
    void guardaYRecuperaSolicitud() {
        SolicitudEntity solicitudEntity = new SolicitudEntity();
        ClienteEntity clienteEntity = new ClienteEntity();

        // Guardamos la entidad del cliente
        clienteEntity.setNombre("Cliente 1");
        clienteEntity.setEmail("Cliente1@mail");
        clienteEntity.setTipoCliente(STANDARD);
        repositoryCliente.save(repositoryCliente.save(clienteEntity));

        // Guardamos la entidad de solicitud
        solicitudEntity.setCliente(clienteEntity);
        solicitudEntity.setDescripcion("Incidencia de red");
        solicitudEntity.setFechaCreacion(LocalDate.now());
        solicitudEntity.setEstadoSolicitud(ABIERTA);

        SolicitudEntity saved = repositorySolicitud.save(solicitudEntity);
        Optional<SolicitudEntity> found = repositorySolicitud.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDescripcion()).isEqualTo("Incidencia de red");
    }

    @Test
    void persisteYRecuperaHistorico() {
        SolicitudEntity solicitudEntity = new SolicitudEntity();
        ClienteEntity clienteEntity = new ClienteEntity();
        TecnicoEntity tecnicoEntity = new TecnicoEntity();

        // Guardamos la entidad del cliente
        clienteEntity.setNombre("Cliente 1");
        clienteEntity.setEmail("Cliente1@mail");
        clienteEntity.setTipoCliente(STANDARD);
        ClienteEntity clienteSaved = repositoryCliente.save(clienteEntity);

        // Guardamos la entidad del tecnico
        tecnicoEntity.setNombre("Tecnico 1");
        tecnicoEntity.setEspecialidad("Especialidad 1");
        tecnicoEntity.setActivo(true);
        TecnicoEntity tecnicoSaved = repositoryTecnico.save(tecnicoEntity);

        // Creamos y modificamos el estado del DOMINIO solicitud
        Cliente cliente = new Cliente(clienteSaved.getId(), clienteSaved.getNombre(), clienteSaved.getEmail(), clienteSaved.getTipoCliente());
        Tecnico tecnico = new Tecnico(tecnicoSaved.getId(), tecnicoSaved.getNombre(), tecnicoSaved.getEspecialidad(), tecnicoSaved.isActivo());
        Solicitud solicitud = new Solicitud(null, cliente, "Test Historico", LocalDate.now(), ABIERTA, null);
        solicitud.asignar(tecnico); // Cambio 1
        solicitud.cerrar(); // Cambio 2
        solicitud.reabrir(); // Cambio 3

        // Guardamos la entidad de la solicitud
        solicitudEntity.setCliente(clienteSaved);
        solicitudEntity.setDescripcion(solicitud.getDescripcion());
        solicitudEntity.setFechaCreacion(solicitud.getFechaCreacion());
        solicitudEntity.setEstadoSolicitud(solicitud.getEstadoSolicitud());
        solicitudEntity.setFechaCierre(solicitud.getFechaCierre());
        solicitudEntity.setTecnicoAsignado(tecnicoSaved);
        var historialEntity =
                solicitud
                        .getHistorial()
                        .stream()
                        .map(estadoChange ->
                                new EstadoChangeEntity(
                                        estadoChange.getEstadoAnterior(),
                                        estadoChange.getEstadoNuevo()))
                        .toList();
        solicitudEntity.setHistorial(historialEntity);
        SolicitudEntity solicitudSaved = repositorySolicitud.save(solicitudEntity);

        // Recuperamos la solicitud mapeada
        Optional<SolicitudEntity> found = repositorySolicitud.findById(solicitudSaved.getId());

        // Verificamos si el mapeo se ha llevado a cabo correcatemente
        int numeroDeCambiosEstado = solicitud.getHistorial().size();
        assertThat(found).isPresent();
        assertThat(found.get().getHistorial().size()).isEqualTo(numeroDeCambiosEstado);
        for (int i = 0; i < numeroDeCambiosEstado; i++) {
            assertThat(found.get().getHistorial().get(i).getEstadoAnterior()).isEqualTo(solicitud.getHistorial().get(i).getEstadoAnterior());
            assertThat(found.get().getHistorial().get(i).getEstadoNuevo()).isEqualTo(solicitud.getHistorial().get(i).getEstadoNuevo());
        }
    }

}
