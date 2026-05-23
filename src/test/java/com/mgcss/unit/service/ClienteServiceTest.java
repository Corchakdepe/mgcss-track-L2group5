package com.mgcss.unit.service;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.service.ClienteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private static ClienteRepository repoCliente;
    private static ClienteService sut;

    @BeforeAll
    static void setUp() {
        // 1. Arrange: Crear mocks y datos
        repoCliente = mock(ClienteRepository.class);
        sut = new ClienteService(repoCliente);
    }

    @Test
    void debeCrearClienteCorrectamente() {
        // Simular dependencias externas
        when(repoCliente.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));
        // 2. Act: Ejecutar servicio
        Cliente cliente = sut.crearCliente("Cliente1", "cliente1@mail.com");
        // 3. Assert: Verificar la orquestacion
        verify(repoCliente).save(cliente);
    }

}