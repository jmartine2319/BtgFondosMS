package com.btg.fondos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Servicio para generar jwt para autenticación a los servicios
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Clave hmac generada
     * @return secret
     */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Metodo para generar token
     * @param username usuario autenticado
     * @return token generado
     */
    public String generarToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    /**
     * Metodo para obtener el nombre
     * @param token jwt recibido
     * @return nombre obtenido del jwt
     */
    public String extraerUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Metodo para validar si el jwt es valido
     * @param token jwt recibido
     * @param username nombre del usuario autenticado
     * @return si es valido o no el jwt
     */
    public boolean esValido(String token, String username) {
        return extraerUsername(token).equals(username) && !estaExpirado(token);
    }

    /**
     * Metodo apra validar si el token esta vigente
     * @param token jwt recibido
     * @return si el token es valido o no
     */
    private boolean estaExpirado(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    /**
     * Metodo para obtener atributos del jwt
     * @param token jwt recibido
     * @return atributos del jwt
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
