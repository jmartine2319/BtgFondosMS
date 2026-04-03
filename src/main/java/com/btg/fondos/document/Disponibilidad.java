package com.btg.fondos.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "disponibilidad")
public class Disponibilidad {

    @Id
    private String id;
    private String idSucursal;
    private String idProducto;
}
