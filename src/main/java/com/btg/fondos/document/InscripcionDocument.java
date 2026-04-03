package com.btg.fondos.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "inscripcion")
public class InscripcionDocument {

    @Id
    private String id;
    private String idProducto;
    private String idCliente;
    private String estado;
    private LocalDate fechaApertura;
    private LocalDate fechaCancelacion;
    private Long monto;
}
