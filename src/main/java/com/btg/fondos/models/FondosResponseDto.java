package com.btg.fondos.models;

import com.btg.fondos.dto.InscripcionDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FondosResponseDto {
    private String mensaje;
    private Long saldo;
    private InscripcionDto transaccion;
}
