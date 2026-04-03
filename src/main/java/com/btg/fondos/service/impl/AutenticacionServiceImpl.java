package com.btg.fondos.service.impl;

import com.btg.fondos.document.UsuarioDocument;
import com.btg.fondos.models.AutenticacionRequestDto;
import com.btg.fondos.models.AutenticacionResponseDto;
import com.btg.fondos.repository.UsuarioRepository;
import com.btg.fondos.security.JwtUtil;
import com.btg.fondos.service.AutenticacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacionServiceImpl implements AutenticacionService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AutenticacionResponseDto autenticarUsuario(AutenticacionRequestDto request) {
        UsuarioDocument usuario = usuarioRepository.findByUsername(request.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!request.getClave().equals(usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        AutenticacionResponseDto response = new AutenticacionResponseDto();
        response.setJwt(jwtUtil.generarToken(usuario.getUsername()));
        return response;
    }
}
