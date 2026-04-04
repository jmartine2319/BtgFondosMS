package com.btg.fondos.mapper;

import com.btg.fondos.document.InscripcionDocument;
import com.btg.fondos.document.ProductoDocument;
import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.dto.ProductoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    ProductoDto toDto(ProductoDocument producto);

    ProductoDocument toDocument(ProductoDto dto);
}
