package com.mgcss.unit.domain.model;

import com.mgcss.domain.model.Tecnico;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TecnicoTest {

    @Test
    void obtieneLosAtributosCorrectamente() {
        Tecnico tecnico = new Tecnico(1L, "", "", true);
        assertEquals(1L, tecnico.getId());
        assertEquals("", tecnico.getNombre());
        assertEquals("", tecnico.getEspecialidad());
    }

}