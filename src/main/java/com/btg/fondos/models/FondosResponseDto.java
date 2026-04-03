package com.btg.fondos.models;

import com.btg.fondos.document.Inscripcion;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class FondosResponseDto {
    private String mensaje;
    private Long saldo;
    private Inscripcion transaccion;
}
