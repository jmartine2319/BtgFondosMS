package com.btg.fondos.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "producto")
public class Producto {

    @Id
    private String id;
    private String nombre;
    private String tipoProducto;
    private Long monto;
}
