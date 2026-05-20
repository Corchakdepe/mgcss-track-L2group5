package com.mgcss.infrastructure.persistence.adapter;

import com.mgcss.domain.model.Cliente;
import com.mgcss.domain.repository.ClienteRepository;
import com.mgcss.infrastructure.persistence.entity.ClienteEntity;
import com.mgcss.infrastructure.persistence.repository.JpaClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaClienteRepository;

    public ClienteRepositoryAdapter(JpaClienteRepository jpaClienteRepository) {
        this.jpaClienteRepository = jpaClienteRepository;
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity();
        entity.setId(cliente.getId());
        entity.setNombre(cliente.getNombre());
        entity.setEmail(cliente.getEmail());
        entity.setTipoCliente(cliente.getTipoCliente());
        ClienteEntity saved = jpaClienteRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return jpaClienteRepository.findById(id).map(this::toDomain);
    }

    private Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getNombre(),
                entity.getEmail(),
                entity.getTipoCliente()
        );
    }
}
