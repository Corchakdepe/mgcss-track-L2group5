package com.mgcss.integration;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.infrastructure.persistence.adapter.SolicitudRepositoryAdapter;
import com.mgcss.infrastructure.persistence.entity.ClienteEntity;
import com.mgcss.infrastructure.persistence.entity.TecnicoEntity;
import com.mgcss.infrastructure.persistence.repository.JpaClienteRepository;
import com.mgcss.infrastructure.persistence.repository.JpaSolicitudRepository;
import com.mgcss.infrastructure.persistence.repository.JpaTecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static com.mgcss.domain.model.EstadoSolicitud.ABIERTA;
import static com.mgcss.domain.model.TipoCliente.STANDARD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Tag("integration")
class JpaSolicitudRepositoryIT {

    @Autowired
    private JpaSolicitudRepository repositorySolicitud;
    @Autowired
    private JpaClienteRepository repositoryCliente;
    @Autowired
    private JpaTecnicoRepository repositoryTecnico;

    private SolicitudRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SolicitudRepositoryAdapter(repositorySolicitud, repositoryCliente, repositoryTecnico);
    }

    @Test
    void guardaYRecuperaSolicitud() {
        ClienteEntity clienteEntity = new ClienteEntity();

        // Guardamos la entidad del cliente
        clienteEntity.setNombre("Cliente 1");
        clienteEntity.setEmail("Cliente1@mail");
        clienteEntity.setTipoCliente(STANDARD);
        repositoryCliente.save(repositoryCliente.save(clienteEntity));

        // Guardamos la solicitud a traves del adaptador
        Cliente cliente = new Cliente(clienteEntity.getId(), clienteEntity.getNombre(), clienteEntity.getEmail(), clienteEntity.getTipoCliente());
        Solicitud solicitud = new Solicitud(null, cliente, "Incidencia de red", LocalDate.now(), ABIERTA, null);
        Solicitud guardada = adapter.save(solicitud);

        // Recuperamos la solicitud guardada
        Optional<Solicitud> encontrada = adapter.findById(guardada.getId());

        // Verificamos estado
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getDescripcion()).isEqualTo(solicitud.getDescripcion());
    }

    @Test
    void persisteYRecuperaHistorico() {
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

        // Guardamos la solicitud a traves del adapter
        Solicitud guardada = adapter.save(solicitud);

        // Recuperamos la solicitud mapeada
        Optional<Solicitud> encontrada = adapter.findById(guardada.getId());

        // Verificamos si el mapeo se ha llevado a cabo correcatemente
        var historial = solicitud.getHistorial();
        int numeroDeCambiosEstado = historial.size();
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getHistorial()).hasSameSizeAs(historial);

        var historialEncontrado = encontrada.get().getHistorial();
        for (int i = 0; i < numeroDeCambiosEstado; i++) {
            assertThat(historialEncontrado.get(i).getEstadoAnterior()).isEqualTo(historial.get(i).getEstadoAnterior());
            assertThat(historialEncontrado.get(i).getEstadoNuevo()).isEqualTo(historial.get(i).getEstadoNuevo());
        }
    }

    @Test
    void fallaAlGuardarSinCliente() {
        Solicitud solicitud = new Solicitud(null, null, "Sin cliente", LocalDate.now(), ABIERTA, null, null);
        assertThrows(IllegalArgumentException.class, () -> adapter.save(solicitud));
    }

    @Test
    void fallaAlGuardarConTecnicoInexistente() {
        Cliente cliente = new Cliente(999L, "Falso", null, STANDARD);
        Solicitud solicitud = new Solicitud(null, cliente, "Test", LocalDate.now(), ABIERTA, null, null);
        assertThrows(RuntimeException.class, () -> adapter.save(solicitud));
    }
}
