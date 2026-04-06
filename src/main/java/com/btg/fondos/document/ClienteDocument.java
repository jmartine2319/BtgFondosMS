package com.btg.fondos.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento mongo para almacenar los clientes de la aplicacion
 */
@Data
@Builder
@Document(collection = "cliente")
public class ClienteDocument {

    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String ciudad;
    private Long saldo;
    private String tipoNotificacion; // EMAIL o SMS
    private String email;
    private String telefono;
}
