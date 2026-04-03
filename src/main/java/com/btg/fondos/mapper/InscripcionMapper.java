package com.btg.fondos.mapper;

import com.btg.fondos.document.Inscripcion;
import com.btg.fondos.dto.InscripcionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {

    InscripcionDto toDto(Inscripcion inscripcion);

    Inscripcion toDocument(InscripcionDto dto);
}
