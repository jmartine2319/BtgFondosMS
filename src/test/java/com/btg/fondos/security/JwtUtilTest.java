package com.btg.fondos.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Clave mínima de 32 bytes requerida por HMAC-SHA256
    private static final String SECRET = "clave-secreta-btg-fondos-test-32x";
    private static final long EXPIRATION = 3_600_000L; // 1 hora

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);
    }

    @Test
    void generarToken_retornaTokenNoNulo() {
        String token = jwtUtil.generarToken("admin");
        assertThat(token).isNotBlank();
    }

    @Test
    void extraerUsername_retornaUsernameCorrectamente() {
        String token = jwtUtil.generarToken("admin");
        assertThat(jwtUtil.extraerUsername(token)).isEqualTo("admin");
    }

    @Test
    void esValido_tokenValidoYUsernameCoincide_retornaTrue() {
        String token = jwtUtil.generarToken("admin");
        assertThat(jwtUtil.esValido(token, "admin")).isTrue();
    }

    @Test
    void esValido_usernameDiferente_retornaFalse() {
        String token = jwtUtil.generarToken("admin");
        assertThat(jwtUtil.esValido(token, "otro_usuario")).isFalse();
    }

    @Test
    void esValido_tokenExpirado_retornaFalse() {
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L); // ya expirado
        String token = jwtUtil.generarToken("admin");

        assertThatThrownBy(() -> jwtUtil.esValido(token, "admin"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void generarToken_diferentesUsuarios_retornanTokensDistintos() {
        String token1 = jwtUtil.generarToken("user1");
        String token2 = jwtUtil.generarToken("user2");
        assertThat(token1).isNotEqualTo(token2);
    }
}
