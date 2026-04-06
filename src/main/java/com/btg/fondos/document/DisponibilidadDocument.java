package com.btg.fondos.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento mongo para almacenar la disponibilidad de los productos por sucursal
 */
@Data
@Document(collection = "disponibilidad")
public class DisponibilidadDocument {

    @Id
    private String id;
    private String idSucursal;
    private String idProducto;
}
