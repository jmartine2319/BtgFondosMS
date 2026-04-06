package com.btg.fondos.controller;

import com.btg.fondos.models.AutenticacionRequestDto;
import com.btg.fondos.models.AutenticacionResponseDto;
import com.btg.fondos.security.JwtUtil;
import com.btg.fondos.service.AutenticacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para autenticacion de los servicios
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AutenticacionController {

    private final AutenticacionService autenticacionService;

    /**
     * Servicio para obtener token jwt y acceder a los servicios
     * @param request datos de credenciales
     * @return jwt para obtener peticiones
     */
    @PostMapping("/login")
    public ResponseEntity<AutenticacionResponseDto> login(@RequestBody AutenticacionRequestDto request) {
        AutenticacionResponseDto response = autenticacionService.autenticarUsuario(request);
        return ResponseEntity.ok(response);
    }
}
