package com.btg.fondos.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sucursal")
public class SucursalDocument {

    @Id
    private String id;
    private String nombre;
    private String ciudad;

}
