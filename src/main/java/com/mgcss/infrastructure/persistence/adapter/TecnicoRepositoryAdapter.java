package com.mgcss.infrastructure.persistence.adapter;


import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.infrastructure.persistence.entity.TecnicoEntity;
import com.mgcss.infrastructure.persistence.repository.JpaTecnicoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TecnicoRepositoryAdapter implements TecnicoRepository {

    private final JpaTecnicoRepository jpaRepository;

    public TecnicoRepositoryAdapter(JpaTecnicoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Tecnico save(Tecnico tecnico) {
        // Mapeo a entidad usando setters
        TecnicoEntity entity = new TecnicoEntity();
        entity.setId(tecnico.getId());
        entity.setNombre(tecnico.getNombre());
        entity.setEspecialidad(tecnico.getEspecialidad());
        entity.setActivo(tecnico.estaActivo());

        TecnicoEntity guardado = jpaRepository.save(entity);
        return mapToDomain(guardado);
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        return jpaRepository.findById(id).map(this::mapToDomain);
    }

    private Tecnico mapToDomain(TecnicoEntity entity) {
        return new Tecnico(entity.getId(),
                entity.getNombre(),
                entity.getEspecialidad(),
                entity.isActivo());
    }
}
