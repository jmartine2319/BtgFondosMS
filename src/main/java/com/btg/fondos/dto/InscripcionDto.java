package com.btg.fondos.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class InscripcionDto {
    private String id;
    private String idProducto;
    private String idCliente;
    private String estado;
    private LocalDate fechaApertura;
    private LocalDate fechaCancelacion;
    private Long monto;
}
