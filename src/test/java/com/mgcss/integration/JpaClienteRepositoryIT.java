package com.mgcss.integration;

import com.mgcss.domain.model.Cliente;
import com.mgcss.infrastructure.persistence.adapter.ClienteRepositoryAdapter;
import com.mgcss.infrastructure.persistence.repository.JpaClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static com.mgcss.domain.model.TipoCliente.STANDARD;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Tag("integration")
class JpaClienteRepositoryIT {

    @Autowired
    private JpaClienteRepository repository;

    private ClienteRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ClienteRepositoryAdapter(repository);
    }

    @Test
    void guardaYRecuperaCliente() {
        // Guardamos el cliente
        Cliente cliente = new Cliente(null, "Cliente 1", "cliente1@mail.com", STANDARD);
        Cliente guardado = adapter.save(cliente);

        // Recuperamos el cliente mapeado
        Optional<Cliente> encontrado = adapter.findById(guardado.getId());

        // Verificamos estado
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo(cliente.getEmail());
    }

}
