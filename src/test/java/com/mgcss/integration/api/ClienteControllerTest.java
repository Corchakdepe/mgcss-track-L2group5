package com.mgcss.integration.api;

import com.mgcss.api.controller.ClienteController;
import com.mgcss.api.dto.ClienteRequestDTO;
import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.TipoCliente;
import com.mgcss.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    private Cliente clienteSimulado;

    @BeforeEach
    void setUp() {
        clienteSimulado = new Cliente(1L, "Carlos Gomez", "carlos@mgcss.com", TipoCliente.STANDARD);
    }

    @Test
    void crearCliente_DevuelveStatusCreatedYJson() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO(clienteSimulado.getNombre(), clienteSimulado.getEmail());

        when(clienteService.crearCliente(clienteSimulado.getNombre(), clienteSimulado.getEmail())).thenReturn(clienteSimulado);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(clienteSimulado.getId()))
                .andExpect(jsonPath("$.nombre").value(clienteSimulado.getNombre()))
                .andExpect(jsonPath("$.email").value(clienteSimulado.getEmail()));
    }

}