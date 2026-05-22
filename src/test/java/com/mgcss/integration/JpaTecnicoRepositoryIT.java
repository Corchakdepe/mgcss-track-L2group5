package com.mgcss.integration;

import com.mgcss.domain.model.Tecnico;
import com.mgcss.infrastructure.persistence.adapter.TecnicoRepositoryAdapter;
import com.mgcss.infrastructure.persistence.repository.JpaTecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Tag("integration")
class JpaTecnicoRepositoryIT {

    @Autowired
    private JpaTecnicoRepository repository;

    private TecnicoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new TecnicoRepositoryAdapter(repository);
    }

    @Test
    void guardaYRecuperaTecnico() {
        // Guardamos el tecnico
        Tecnico tecnico = new Tecnico(null, "Tecnico 1", "Redes", true);
        Tecnico guardado = adapter.save(tecnico);

        // Recuperamos el tecnico mapeado
        Optional<Tecnico> encontrado = adapter.findById(guardado.getId());

        // Verificamos estado
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNombre()).isEqualTo(tecnico.getNombre());
    }

    @Test
    void guardaYActualizaTecnico() {
        Tecnico tecnico = new Tecnico(null, "Técnico Inicial", "Sistemas", true);
        Tecnico guardado = adapter.save(tecnico);

        // Simular actualización con nuevo constructor
        Tecnico tecnicoAActualizar = new Tecnico(guardado.getId(), "Técnico Actualizado", guardado.getEspecialidad(), guardado.estaActivo());
        Tecnico actualizado = adapter.save(tecnicoAActualizar);

        assertEquals("Técnico Actualizado", actualizado.getNombre());
    }

}
