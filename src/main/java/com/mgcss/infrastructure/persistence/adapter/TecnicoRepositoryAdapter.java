package com.mgcss.infrastructure.persistence.adapter;

import com.mgcss.domain.model.Tecnico;
import com.mgcss.domain.repository.TecnicoRepository;
import com.mgcss.infrastructure.persistence.entity.TecnicoEntity;
import com.mgcss.infrastructure.persistence.repository.JpaTecnicoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TecnicoRepositoryAdapter implements TecnicoRepository {

    private final JpaTecnicoRepository jpaTecnicoRepository;

    public TecnicoRepositoryAdapter(JpaTecnicoRepository jpaTecnicoRepository) {
        this.jpaTecnicoRepository = jpaTecnicoRepository;
    }

    @Override
    public Tecnico save(Tecnico tecnico) {
        TecnicoEntity entity = new TecnicoEntity();
        entity.setId(tecnico.getId());
        entity.setNombre(tecnico.getNombre());
        entity.setEspecialidad(tecnico.getEspecialidad());
        entity.setActivo(tecnico.estaActivo());
        TecnicoEntity saved = jpaTecnicoRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        return jpaTecnicoRepository.findById(id).map(this::toDomain);
    }

    private Tecnico toDomain(TecnicoEntity entity) {
        return new Tecnico(
                entity.getId(),
                entity.getNombre(),
                entity.getEspecialidad(),
                entity.isActivo()
        );
    }
}
