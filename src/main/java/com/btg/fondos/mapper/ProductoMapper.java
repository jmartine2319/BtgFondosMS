package com.btg.fondos.mapper;

import com.btg.fondos.document.ProductoDocument;
import com.btg.fondos.dto.ProductoDto;
import org.mapstruct.Mapper;

/**
 * Mapper para convertir documentos producto a dto
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper {
    ProductoDto toDto(ProductoDocument producto);

    ProductoDocument toDocument(ProductoDto dto);
}
