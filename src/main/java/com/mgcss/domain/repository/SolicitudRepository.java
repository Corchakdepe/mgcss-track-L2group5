package com.mgcss.domain.repository;

import com.mgcss.domain.model.Solicitud;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {
    Solicitud save(Solicitud solicitud);

    Optional<Solicitud> findById(Long id);

    List<Solicitud> findAll();
}
