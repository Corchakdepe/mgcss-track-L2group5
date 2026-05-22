package com.mgcss.service;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.model.TipoCliente;
import com.mgcss.domain.repository.ClienteRepository;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente crearCliente(String nombre, String email) {
        Cliente cliente = new Cliente(null, nombre, email, TipoCliente.STANDARD);
        return clienteRepository.save(cliente);
    }
}
