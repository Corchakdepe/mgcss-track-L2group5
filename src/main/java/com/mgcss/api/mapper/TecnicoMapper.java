package com.mgcss.api.mapper;

import com.mgcss.api.dto.TecnicoResponseDTO;
import com.mgcss.domain.model.Tecnico;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TecnicoMapper {

    public static TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        if (tecnico == null) return null;

        return new TecnicoResponseDTO(
                tecnico.getId(),
                tecnico.getNombre(),
                tecnico.getEspecialidad(),
                tecnico.estaActivo()
        );
    }
}
