package com.btg.fondos.mapper;

import com.btg.fondos.document.InscripcionDocument;
import com.btg.fondos.dto.InscripcionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {

    InscripcionDto toDto(InscripcionDocument inscripcion);

    InscripcionDocument toDocument(InscripcionDto dto);
}
