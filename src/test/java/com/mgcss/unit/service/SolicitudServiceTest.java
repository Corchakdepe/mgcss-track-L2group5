package com.mgcss.unit.service;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.mgcss.domain.model.EstadoSolicitud.ABIERTA;
import static com.mgcss.domain.model.EstadoSolicitud.CERRADA;
import static com.mgcss.domain.model.EstadoSolicitud.EN_PROCESO;
import static com.mgcss.domain.model.TipoCliente.STANDARD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudServiceTest {
    private SolicitudRepository repoSolicitud;
    private TecnicoRepository repoTecnico;
    private ClienteRepository repoCliente;
    private SolicitudService sut;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        repoSolicitud = mock(SolicitudRepository.class);
        repoTecnico = mock(TecnicoRepository.class);
        repoCliente = mock(ClienteRepository.class);
        sut = new SolicitudService(repoSolicitud, repoTecnico, repoCliente);
        cliente = new Cliente(1L, "", "", STANDARD);
    }

    @Test
    void debeAsignarTecnicoCorrectamente() {
        Solicitud solicitud = new Solicitud(1L, cliente, "", LocalDate.now(), ABIERTA, null);
        Tecnico tecnico = new Tecnico(99L, "", "", true);
        when(repoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(repoTecnico.findById(99L)).thenReturn(Optional.of(tecnico));

        sut.asignarTecnico(1L, 99L);

        verify(repoSolicitud).save(argThat(Solicitud::tieneTecnicoAsignado));
    }

    @Test
    void debeLanzarExcepcionAlAsignarTecnicoInactivo() {
        Solicitud solicitud = new Solicitud(2L, cliente, "", LocalDate.now(), ABIERTA, null);
        Tecnico tecnico = new Tecnico(98L, "", "", false);
        when(repoSolicitud.findById(2L)).thenReturn(Optional.of(solicitud));
        when(repoTecnico.findById(98L)).thenReturn(Optional.of(tecnico));

        assertThrows(IllegalArgumentException.class, () -> sut.asignarTecnico(2L, 98L));

        verify(repoSolicitud, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiIdInexistente() {
        assertThrows(IllegalArgumentException.class, () -> sut.asignarTecnico(0L, 100L));

        verify(repoSolicitud, never()).save(any());
    }

    @Test
    void debeCrearSolicitudCorrectamente() {
        when(repoCliente.findById(1L)).thenReturn(Optional.of(cliente));
        when(repoSolicitud.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Solicitud resultado = sut.crearSolicitud(1L, "Nueva incidencia");

        assertNotNull(resultado);
        assertEquals("Nueva incidencia", resultado.getDescripcion());
        assertEquals(ABIERTA, resultado.getEstadoSolicitud());
        verify(repoCliente).findById(1L);
        verify(repoSolicitud).save(any());
    }

    @Test
    void debeLanzarExcepcionAlCrearSolicitudConClienteInexistente() {
        when(repoCliente.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> sut.crearSolicitud(99L, "Incidencia"));

        verify(repoSolicitud, never()).save(any());
    }

    @Test
    void debeConsultarSolicitudCorrectamente() {
        Solicitud solicitud = new Solicitud(1L, cliente, "Consulta", LocalDate.now(), ABIERTA, null);
        when(repoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));

        Solicitud resultado = sut.consultarSolicitud(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Consulta", resultado.getDescripcion());
        verify(repoSolicitud).findById(1L);
    }

    @Test
    void debeLanzarExcepcionAlConsultarSolicitudInexistente() {
        when(repoSolicitud.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> sut.consultarSolicitud(99L));
    }

    @Test
    void debeCambiarEstadoCorrectamente() {
        Solicitud solicitud = new Solicitud(1L, cliente, "", LocalDate.now(), ABIERTA, null);
        Tecnico tecnico = new Tecnico(1L, "", "", true);
        solicitud.asignar(tecnico);
        when(repoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(repoSolicitud.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Solicitud resultado = sut.cambiarEstado(1L);

        assertEquals(CERRADA, resultado.getEstadoSolicitud());
        assertNotNull(resultado.getFechaCierre());
        verify(repoSolicitud).save(any());
    }

    @Test
    void debeReabrirSolicitudCorrectamente() {
        Solicitud solicitud = new Solicitud(1L, cliente, "", LocalDate.now(), ABIERTA, null);
        Tecnico tecnico = new Tecnico(1L, "", "", true);
        solicitud.asignar(tecnico);
        solicitud.cerrar();
        when(repoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(repoSolicitud.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Solicitud resultado = sut.reabrirSolicitud(1L);

        assertEquals(EN_PROCESO, resultado.getEstadoSolicitud());
        assertNull(resultado.getFechaCierre());
        verify(repoSolicitud).save(any());
    }

    @Test
    void debeListarSolicitudes() {
        Solicitud solicitud = new Solicitud(1L, cliente, "", LocalDate.now(), ABIERTA, null);
        when(repoSolicitud.findAll()).thenReturn(List.of(solicitud));

        List<Solicitud> resultado = sut.listarSolicitudes();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        verify(repoSolicitud).findAll();
    }
}
