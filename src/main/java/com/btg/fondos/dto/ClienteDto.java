package com.btg.fondos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClienteDto {
    private String id;
    private String nombre;
    private String apellido;
    private String ciudad;
    private Long saldo;
    private String tipoNotificacion; // EMAIL o SMS
    private String email;
    private String telefono;
}
