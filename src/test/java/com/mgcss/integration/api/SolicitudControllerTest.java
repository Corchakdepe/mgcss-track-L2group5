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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Mockito.when(solicitudService.crearSolicitud(1L, "Error en el servidor")).thenReturn(solicitudMock);

        mockMvc.perform(post("/api/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descripcion").value("Error en el servidor"))
                .andExpect(jsonPath("$.estadoSolicitud").value("ABIERTA"));
    }
}