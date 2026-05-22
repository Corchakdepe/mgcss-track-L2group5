package com.mgcss.integration.api;

import com.mgcss.api.controller.SolicitudController;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.EstadoSolicitud;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.TipoCliente;
import com.mgcss.service.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitudController.class)
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudService solicitudService;

    private Solicitud solicitudSimulada;

    @BeforeEach
    void setUp() {
        Cliente clienteSimulado = new Cliente(1L, "Jose Gomez", "josegomez@mgcss.com", TipoCliente.STANDARD);
        solicitudSimulada = new Solicitud(1L,
                clienteSimulado,
                "Router roto",
                LocalDate.now(),
                EstadoSolicitud.ABIERTA,
                null,
                null);
    }

    @Test
    void crearSolicitud_DevuelveStatusCreatedYJson() throws Exception {
        SolicitudRequestDTO request = new SolicitudRequestDTO(solicitudSimulada.getId(), solicitudSimulada.getDescripcion());
        when(solicitudService.crearSolicitud(solicitudSimulada.getId(), solicitudSimulada.getDescripcion())).thenReturn(solicitudSimulada);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(solicitudSimulada.getId()))
                .andExpect(jsonPath("$.descripcion").value(solicitudSimulada.getDescripcion()))
                .andExpect(jsonPath("$.estadoSolicitud").value(solicitudSimulada.getEstadoSolicitud().toString()));
    }

    @Test
    void consultarPorIdExistente_DevuelveStatusOK() throws Exception {
        when(solicitudService.buscarPorId(solicitudSimulada.getId())).thenReturn(solicitudSimulada);
        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(solicitudSimulada.getId()))
                .andExpect(jsonPath("$.estadoSolicitud").value(solicitudSimulada.getEstadoSolicitud().toString()));
    }

    @Test
    void consultarPorIdInexistente_DevuelveStatusNotFound() throws Exception {
        when(solicitudService.buscarPorId(-1L)).thenThrow(new IllegalArgumentException("La solicitud no existe"));
        mockMvc.perform(get("/api/solicitudes/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void asignarTecnico_DevuelveStatusOK() throws Exception {
        doNothing().when(solicitudService).asignarTecnico(solicitudSimulada.getId(), 2L);
        mockMvc.perform(put("/api/solicitudes/1/tecnico").param("tecnicoId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void cerrarSolicitud_DevuelveStatusOK() throws Exception {
        doNothing().when(solicitudService).cerrarSolicitud(solicitudSimulada.getId());
        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isOk());
    }

    @Test
    void reabrirSolicitud_DevuelveStatusOK() throws Exception {
        doNothing().when(solicitudService).reabrirSolicitud(solicitudSimulada.getId());
        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk());
    }

    @Test
    void listarSolicitudes_DevuelveListaYStatusOk() throws Exception {
        when(solicitudService.listarTodas()).thenReturn(List.of(solicitudSimulada));
        mockMvc.perform(get("/api/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(solicitudSimulada.getId()))
                .andExpect(jsonPath("$.length()").value(1));
    }
}