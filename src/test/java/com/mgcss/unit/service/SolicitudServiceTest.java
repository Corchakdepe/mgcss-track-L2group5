package com.mgcss.unit.service;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.domain.repository.SolicitudRepository;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.SolicitudService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static com.mgcss.domain.model.EstadoSolicitud.ABIERTA;
import static com.mgcss.domain.model.EstadoSolicitud.CERRADA;
import static com.mgcss.domain.model.TipoCliente.STANDARD;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SolicitudServiceTest {
    private static SolicitudRepository repoSolicitud;
    private static TecnicoRepository repoTecnico;
    private static ClienteRepository repoCliente;
    private static SolicitudService sut;
    private static Solicitud solicitud;
    private static Tecnico tecnico;
    private static Cliente cliente;

    @BeforeAll
    static void beforeAll() {
        // 1. Arrange: Crear mocks y datos
        repoSolicitud = mock(SolicitudRepository.class);
        repoTecnico = mock(TecnicoRepository.class);
        repoCliente = mock(ClienteRepository.class);
        sut = new SolicitudService(repoSolicitud, repoTecnico, repoCliente);
        cliente = new Cliente(1L, "", "", STANDARD);
    }

    @Test
    void debeAsignarTecnicoCorrectamente() {
        // Simular dependencias externas
        solicitud = new Solicitud(1L, cliente, "", LocalDate.now(), ABIERTA, null);
        tecnico = new Tecnico(99L, "", "", true);
        when(repoSolicitud.findById(1L)).thenReturn(Optional.of(solicitud));
        when(repoTecnico.findById(99L)).thenReturn(Optional.of(tecnico));
        // 2. Act: Ejecutar servicio
        sut.asignarTecnico(1L, 99L);
        // 3. Assert: Verificar la orquestación
        verify(repoSolicitud).save(argThat(Solicitud::tieneTecnicoAsignado));
    }

    @Test
    void debeLanzarExcepcionAlAsignarTecnicoInactivo() {
        // Simular dependencias externas
        solicitud = new Solicitud(2L, cliente, "", LocalDate.now(), ABIERTA, null);
        tecnico = new Tecnico(98L, "", "", false);
        when(repoSolicitud.findById(2L)).thenReturn(Optional.of(solicitud));
        when(repoTecnico.findById(98L)).thenReturn(Optional.of(tecnico));
        // 2. Act: Ejecutar servicio esperando el fallo
        assertThrows(IllegalArgumentException.class, () -> sut.asignarTecnico(2L, 98L));
        // 3. Assert: Verificar que nunca se ha guardado ninguna solicitud que no tenga ningun tecnico asignado
        verify(repoSolicitud, never()).save(argThat(sol -> !sol.tieneTecnicoAsignado()));
    }

    @Test
    void debeLanzarExcepcionSiIdInexistente() {
        // No se simulan dependencias externas
        solicitud = new Solicitud(0L, cliente, "", LocalDate.now(), CERRADA, null);
        // 2. Act: Ejecutar servicio esperando el fallo
        assertThrows(IllegalArgumentException.class, () -> sut.asignarTecnico(0L, 100L));
        // 3. Assert: Verificar que no hubo efectos secundarios
        verify(repoSolicitud, never()).save(solicitud);
    }

    @Test
    void deberCrearSolicitudCorrectamente() {
        // Simular dependencias externas
        when(repoCliente.findById(cliente.getId())).thenReturn(Optional.of(cliente));
        when(repoSolicitud.save(any(Solicitud.class))).thenAnswer(i -> i.getArgument(0));
        // 2. Act: Ejecutar servicio
        Solicitud creada = sut.crearSolicitud(cliente.getId(), "");
        // 3. Assert: Verificar la orquestacion
        verify(repoSolicitud).save(creada);
    }

}