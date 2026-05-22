package com.mgcss.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CommonResponseDTO {

    protected Long id;
    protected String nombre;

}
