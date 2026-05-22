package com.mgcss.unit.service;

import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.service.TecnicoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TecnicoServiceTest {

    private static TecnicoRepository repoTecnico;
    private static TecnicoService sut;

    @BeforeAll
    static void setUp() {
        // 1. Arrange: Crear mocks y datos
        repoTecnico = mock(TecnicoRepository.class);
        sut = new TecnicoService(repoTecnico);
    }

    @Test
    void debeCrearClienteCorrectamente() {
        // Simular dependencias externas
        when(repoTecnico.save(any(Tecnico.class))).thenAnswer(i -> i.getArgument(0));
        // 2. Act: Ejecutar servicio
        Tecnico tecnico = sut.crearTecnico("Jesus Diaz", "jesusdiaz@mail.com");
        // 3. Assert: Verificar la orquestacion
        verify(repoTecnico).save(tecnico);
    }
}