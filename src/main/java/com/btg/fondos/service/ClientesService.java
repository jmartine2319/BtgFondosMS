package com.btg.fondos.service;

import com.btg.fondos.dto.ClienteDto;
import com.btg.fondos.models.FondosResponseDto;

public interface ClientesService {
    FondosResponseDto inscribirCliente(ClienteDto request);
}
