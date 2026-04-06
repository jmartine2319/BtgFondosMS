package com.btg.fondos.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Documento mongo para consultar visitantes que tiene una sucursal
 */
@Data
@Document(collection = "visitante")
public class VisitanteDocument {

    @Id
    private String id;
    private String idSucursal;
    private String idCliente;
    private LocalDateTime fechaVisita;
}
