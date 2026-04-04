package com.btg.fondos.service;

import com.btg.fondos.document.UsuarioDocument;
import com.btg.fondos.models.AutenticacionRequestDto;
import com.btg.fondos.models.AutenticacionResponseDto;
import com.btg.fondos.repository.UsuarioRepository;
import com.btg.fondos.security.JwtUtil;
import com.btg.fondos.service.impl.AutenticacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacionServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AutenticacionServiceImpl autenticacionService;

    private UsuarioDocument usuario;
    private AutenticacionRequestDto request;

    @BeforeEach
    void setUp() {
        usuario = UsuarioDocument.builder()
                .id("u1")
                .username("admin")
                .password("secreto123")
                .rol("ADMIN")
                .build();

        request = new AutenticacionRequestDto();
        request.setUsuario("admin");
        request.setClave("secreto123");
    }

    @Test
    void autenticarUsuario_credencialesValidas_retornaJwt() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("secreto123", "secreto123")).thenReturn(true);
        when(jwtUtil.generarToken("admin")).thenReturn("token.jwt.generado");

        AutenticacionResponseDto response = autenticacionService.autenticarUsuario(request);

        assertThat(response.getJwt()).isEqualTo("token.jwt.generado");
    }

    @Test
    void autenticarUsuario_usuarioNoEncontrado_lanzaExcepcion() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> autenticacionService.autenticarUsuario(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuario no encontrado");
    }

    @Test
    void autenticarUsuario_claveIncorrecta_lanzaExcepcion() {
        request.setClave("claveErronea");
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("claveErronea", "secreto123")).thenReturn(false);

        assertThatThrownBy(() -> autenticacionService.autenticarUsuario(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Contraseña incorrecta");
    }
}
