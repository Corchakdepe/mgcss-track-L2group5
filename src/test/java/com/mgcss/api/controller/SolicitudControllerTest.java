package com.mgcss.api.controller;

import com.mgcss.api.dto.AsignarTecnicoRequestDTO;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.service.SolicitudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.mgcss.domain.model.EstadoSolicitud.ABIERTA;
import static com.mgcss.domain.model.EstadoSolicitud.CERRADA;
import static com.mgcss.domain.model.EstadoSolicitud.EN_PROCESO;
import static com.mgcss.domain.model.TipoCliente.STANDARD;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @SuppressWarnings("unused")
    private SolicitudService solicitudService;

    private ObjectMapper objectMapper;

    private final Cliente cliente = new Cliente(1L, "Cliente Test", "cliente@test.com", STANDARD);
    private final Tecnico tecnico = new Tecnico(1L, "Tecnico Test", "Redes", true);

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearSolicitud_devuelve200_conDatosCorrectos() throws Exception {
        SolicitudRequestDTO request = new SolicitudRequestDTO(1L, "Nueva incidencia");
        Solicitud solicitud = new Solicitud(1L, cliente, "Nueva incidencia", LocalDate.now(), ABIERTA, null);

        when(solicitudService.crearSolicitud(1L, "Nueva incidencia")).thenReturn(solicitud);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Nueva incidencia"))
                .andExpect(jsonPath("$.estado").value("ABIERTA"))
                .andExpect(jsonPath("$.clienteNombre").value("Cliente Test"));
    }

    @Test
    void consultarSolicitud_devuelve200_conDatosCorrectos() throws Exception {
        Solicitud solicitud = new Solicitud(1L, cliente, "Incidencia", LocalDate.now(), ABIERTA, null);

        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitud);

        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descripcion").value("Incidencia"))
                .andExpect(jsonPath("$.estado").value("ABIERTA"));
    }

    @Test
    void listarSolicitudes_devuelve200_conLista() throws Exception {
        Solicitud s1 = new Solicitud(1L, cliente, "Incidencia 1", LocalDate.now(), ABIERTA, null);
        Solicitud s2 = new Solicitud(2L, cliente, "Incidencia 2", LocalDate.now(), EN_PROCESO, null);

        when(solicitudService.listarSolicitudes()).thenReturn(List.of(s1, s2));

        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void asignarTecnico_devuelve200_conTecnicoAsignado() throws Exception {
        AsignarTecnicoRequestDTO request = new AsignarTecnicoRequestDTO(1L);
        Solicitud solicitud = new Solicitud(1L, cliente, "Incidencia", LocalDate.now(), EN_PROCESO, null, new Solicitud.DatosAdicionales(tecnico, List.of()));

        when(solicitudService.consultarSolicitud(1L)).thenReturn(solicitud);

        mockMvc.perform(put("/api/solicitudes/1/asignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tecnicoNombre").value("Tecnico Test"));
    }

    @Test
    void cambiarEstado_devuelve200_conEstadoCerrada() throws Exception {
        Solicitud solicitud = new Solicitud(1L, cliente, "Incidencia", LocalDate.now(), CERRADA, LocalDate.now());

        when(solicitudService.cambiarEstado(1L)).thenReturn(solicitud);

        mockMvc.perform(put("/api/solicitudes/1/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CERRADA"))
                .andExpect(jsonPath("$.fechaCierre").isNotEmpty());
    }

    @Test
    void reabrirSolicitud_devuelve200_conEstadoEnProceso() throws Exception {
        Solicitud solicitud = new Solicitud(1L, cliente, "Incidencia", LocalDate.now(), EN_PROCESO, null);

        when(solicitudService.reabrirSolicitud(1L)).thenReturn(solicitud);

        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"))
                .andExpect(jsonPath("$.fechaCierre").isEmpty());
    }

    @Test
    void consultarSolicitudInexistente_devuelve404() throws Exception {
        when(solicitudService.consultarSolicitud(99L))
                .thenThrow(new IllegalArgumentException("Solicitud no encontrada con id: 99"));

        mockMvc.perform(get("/api/solicitudes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Solicitud no encontrada con id: 99"));
    }

    @Test
    void cambiarEstadoSolicitudNoEnProceso_devuelve400() throws Exception {
        when(solicitudService.cambiarEstado(1L))
                .thenThrow(new IllegalStateException("No se puede cerrar si no esta en proceso."));

        mockMvc.perform(put("/api/solicitudes/1/estado"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("No se puede cerrar si no esta en proceso."));
    }
}
