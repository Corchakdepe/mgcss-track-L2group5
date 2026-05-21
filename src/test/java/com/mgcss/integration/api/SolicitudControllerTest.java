package com.mgcss.integration.api;

import com.mgcss.api.controller.SolicitudController;
import com.mgcss.api.dto.SolicitudRequestDTO;
import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.EstadoSolicitud;
import com.mgcss.domain.model.Solicitud;
import com.mgcss.domain.model.TipoCliente;
import com.mgcss.service.SolicitudService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private Cliente clienteMock;
    private Solicitud solicitudMock;

    @BeforeEach
    void setUp() {
        clienteMock = new Cliente(1L, "Juan Perez", "juan@mgcss.com", TipoCliente.STANDARD);
        solicitudMock = new Solicitud(1L, clienteMock, "Error en el servidor", LocalDate.now(), EstadoSolicitud.ABIERTA, null, null);
    }

    @Test
    void crearSolicitud_DevuelveStatusCreatedYJson() throws Exception {
        SolicitudRequestDTO request = new SolicitudRequestDTO(1L, "Error en el servidor");
        when(solicitudService.crearSolicitud(1L, "Error en el servidor")).thenReturn(solicitudMock);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Error en el servidor"))
                .andExpect(jsonPath("$.estadoSolicitud").value("ABIERTA"));
    }

    @Test
    void consultarPorIdExistente_DevuelveStatusOK() throws Exception {
        when(solicitudService.buscarPorId(1L)).thenReturn(solicitudMock);
        mockMvc.perform(get("/api/solicitudes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.estadoSolicitud").value("ABIERTA"));
    }

    @Test
    void consultarPorIdInexistente_DevuelveBadRequest() {
        when(solicitudService.buscarPorId(-1L)).thenThrow(new IllegalArgumentException("La solicitud no existe"));
        assertThrows(ServletException.class, () -> mockMvc.perform(get("/api/solicitudes/-1")));
    }

    @Test
    void asignarTecnico_DevuelveStatusOK() throws Exception {
        doNothing().when(solicitudService).asignarTecnico(1L, 2L);
        mockMvc.perform(put("/api/solicitudes/1/tecnico").param("tecnicoId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void cerrarSolicitud_DevuelveStatusOK() throws Exception {
        doNothing().when(solicitudService).cerrarSolicitud(1L);
        mockMvc.perform(put("/api/solicitudes/1/cerrar"))
                .andExpect(status().isOk());
    }

    @Test
    void reabrirSolicitud_DevuelveStatusOK() throws Exception {
        doNothing().when(solicitudService).reabrirSolicitud(1L);
        mockMvc.perform(patch("/api/solicitudes/1/reabrir"))
                .andExpect(status().isOk());
    }

}