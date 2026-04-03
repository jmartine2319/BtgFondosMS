package com.btg.fondos.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutenticacionRequestDto {
    private String usuario;
    private String clave;
}
