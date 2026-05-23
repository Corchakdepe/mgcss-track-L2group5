package com.mgcss.integration.api;

import com.mgcss.api.controller.TecnicoController;
import com.mgcss.api.dto.TecnicoRequestDTO;
import com.mgcss.domain.model.Tecnico;
import com.mgcss.service.TecnicoService;
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

@WebMvcTest(TecnicoController.class)
class TecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TecnicoService tecnicoService;

    private Tecnico tecnicoSimulado;

    @BeforeEach
    void setUp() {
        tecnicoSimulado = new Tecnico(1L, "Jesus Diaz", "Redes", true);
    }

    @Test
    void crearTecnico_DevuelveStatusCreatedYJson() throws Exception {
        TecnicoRequestDTO request = new TecnicoRequestDTO(tecnicoSimulado.getNombre(), tecnicoSimulado.getEspecialidad());

        when(tecnicoService.crearTecnico(tecnicoSimulado.getNombre(), tecnicoSimulado.getEspecialidad())).thenReturn(tecnicoSimulado);

        mockMvc.perform(post("/api/tecnicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(tecnicoSimulado.getId()))
                .andExpect(jsonPath("$.nombre").value(tecnicoSimulado.getNombre()))
                .andExpect(jsonPath("$.especialidad").value(tecnicoSimulado.getEspecialidad()));
    }

}