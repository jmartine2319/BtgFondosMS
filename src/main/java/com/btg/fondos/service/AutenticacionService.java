package com.btg.fondos.service;

import com.btg.fondos.models.AutenticacionRequestDto;
import com.btg.fondos.models.AutenticacionResponseDto;

public interface AutenticacionService {
    AutenticacionResponseDto autenticarUsuario(AutenticacionRequestDto request);
}
