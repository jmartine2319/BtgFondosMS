package com.btg.fondos.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "visitante")
public class Visitante {

    @Id
    private String id;
    private String idSucursal;
    private String idCliente;
    private LocalDateTime fechaVisita;
}
