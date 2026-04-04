package com.btg.fondos.service;

import com.btg.fondos.dto.InscripcionDto;
import com.btg.fondos.dto.ProductoDto;
import com.btg.fondos.models.FondosRequestDto;
import com.btg.fondos.models.FondosResponseDto;

import java.util.List;

public interface FondosService {
    FondosResponseDto suscribirFondo(FondosRequestDto request);
    FondosResponseDto cancelarSuscripcion(FondosRequestDto request);
    List<InscripcionDto> consultarTransacciones(String idCliente);
    List<ProductoDto> consultarProductos();
}
