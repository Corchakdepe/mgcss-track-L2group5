package com.mgcss.unit.domain.model;

import com.mgcss.domain.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    @Test
    void cerrarSolicitudEnProcesoEstaPermitido() {
        Solicitud solicitud = new Solicitud(1L, new Cliente(1L, "", "@", TipoCliente.STANDARD), "", LocalDate.now(), EstadoSolicitud.EN_PROCESO, null);
        assertDoesNotThrow(solicitud::cerrar);
    }

    @Test
    void cerrarSolicitudNoEnProcesoEstaProhibido() {
        Solicitud solicitud = new Solicitud(2L, new Cliente(1L, "", "@", TipoCliente.STANDARD), "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        assertThrows(IllegalStateException.class, solicitud::cerrar);
    }

    @Test
    void asignarTecnicoEstaPermitido() {
        Solicitud solicitud = new Solicitud(1L, new Cliente(1L, "", "@", TipoCliente.PREMIUM), "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        Tecnico tecnicoActivo = new Tecnico(1L, "", "", true);
        assertDoesNotThrow(() -> solicitud.asignar(tecnicoActivo));
    }

    @Test
    void asignarTecnicoInactivoNoEstaPermitido() {
        Solicitud solicitud = new Solicitud(1L, new Cliente(1L, "", "@", TipoCliente.PREMIUM), "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        Tecnico tecnicoInactivo = new Tecnico(2L, "", "", false);
        assertThrows(IllegalArgumentException.class, () -> solicitud.asignar(tecnicoInactivo));
    }

    @Test
    void obtieneLosAtributosCorrectamente() {
        Cliente cliente = new Cliente(1L, "", "@", TipoCliente.PREMIUM);
        Solicitud solicitud = new Solicitud(1L, cliente, "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        assertEquals(1L, solicitud.getId());
        assertEquals(cliente, solicitud.getCliente());
        assertEquals("", solicitud.getDescripcion());
        assertEquals(LocalDate.now(), solicitud.getFechaCreacion());
        assertNull(solicitud.getFechaCierre());
    }

    @Test
    void reabrirSolicitudCerradaEstaPermitido() {
        // Se crea la solicitud y se cambia a EN_PROCESO, asignandole un tecnico activo
        Solicitud solicitud = new Solicitud(1L, new Cliente(1L, "", "@", TipoCliente.PREMIUM), "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        Tecnico tecnicoActivo = new Tecnico(1L, "", "", true);
        solicitud.asignar(tecnicoActivo);

        // Se cierra la solicitud
        solicitud.cerrar();
        assertEquals(EstadoSolicitud.CERRADA, solicitud.getEstadoSolicitud());

        // Se reabre la solicitud
        assertDoesNotThrow(solicitud::reabrir);

        // Se verifica el estado final
        assertEquals(EstadoSolicitud.EN_PROCESO, solicitud.getEstadoSolicitud());
        assertNull(solicitud.getFechaCierre());
    }

    @Test
    void reabrirSolicitudNoCerradaNoEstaPermitido() {
        // Se crea la solicitud y se cambia a EN_PROCESO, asignandole un tecnico activo
        Solicitud solicitud = new Solicitud(1L, new Cliente(1L, "", "@", TipoCliente.PREMIUM), "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);
        Tecnico tecnicoActivo = new Tecnico(1L, "", "", true);
        solicitud.asignar(tecnicoActivo);

        // No se cierra la solicitud

        // Se reabre la solicitud
        assertThrows(IllegalStateException.class ,solicitud::reabrir);
    }

    @Test
    void registraCambiosDeEstadoCorrectamente() {
        // Crear solicitud (Estado inicial: ABIERTA)
        Solicitud solicitud = new Solicitud(1L, new Cliente(1L, "", "@", TipoCliente.PREMIUM), "", LocalDate.now(), EstadoSolicitud.ABIERTA, null);

        // Asignar técnico (ABIERTA -> EN_PROCESO)
        Tecnico tecnicoActivo = new Tecnico(1L, "", "", true);
        solicitud.asignar(tecnicoActivo);

        // Cerrar (EN_PROCESO -> CERRADA)
        solicitud.cerrar();

        // Reabrir (CERRADA -> EN_PROCESO)
        solicitud.reabrir();

        // Verificar el estado final
        List<EstadoChange> historial = solicitud.getHistorial();

        // 1. Numero de cambios de estado esperados
        assertEquals(3, historial.size());

        // 2. Primera transicion
        assertEquals(EstadoSolicitud.ABIERTA, historial.get(0).getEstadoAnterior());
        assertEquals(EstadoSolicitud.EN_PROCESO, historial.get(0).getEstadoNuevo());
        // 3. Segunda transicion
        assertEquals(EstadoSolicitud.EN_PROCESO, historial.get(1).getEstadoAnterior());
        assertEquals(EstadoSolicitud.CERRADA, historial.get(1).getEstadoNuevo());
        // 4. Segunda transicion
        assertEquals(EstadoSolicitud.CERRADA, historial.get(2).getEstadoAnterior());
        assertEquals(EstadoSolicitud.EN_PROCESO, historial.get(2).getEstadoNuevo());
    }
}