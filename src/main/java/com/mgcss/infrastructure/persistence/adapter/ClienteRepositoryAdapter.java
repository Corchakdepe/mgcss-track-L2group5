package com.mgcss.infrastructure.persistence.adapter;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.infrastructure.persistence.entity.ClienteEntity;
import com.mgcss.infrastructure.persistence.repository.JpaClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaRepository;

    public ClienteRepositoryAdapter(JpaClienteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        // Mapeo a entidad usando setters
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNombre(cliente.getNombre());
        entity.setEmail(cliente.getEmail());
        entity.setTipoCliente(cliente.getTipoCliente());

        ClienteEntity guardado = jpaRepository.save(entity);
        return mapToDomain(guardado);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    private Cliente mapToDomain(ClienteEntity entity) {
        // Mapeo a dominio usando su constructor completo
        return new Cliente(entity.getId(), entity.getNombre(), entity.getEmail(), entity.getTipoCliente());
    }
}
