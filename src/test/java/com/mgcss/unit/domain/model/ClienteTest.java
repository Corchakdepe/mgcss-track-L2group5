package com.mgcss.unit.domain.model;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.TipoCliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClienteTest {

    @Test
    void obtieneLosAtributosCorrectamente() {
        Cliente cliente = new Cliente(1L, "", "@", TipoCliente.PREMIUM);
        assertEquals(1L, cliente.getId());
        assertEquals("", cliente.getNombre());
        assertEquals("@", cliente.getEmail());
        assertEquals(TipoCliente.PREMIUM, cliente.getTipoCliente());
    }
}