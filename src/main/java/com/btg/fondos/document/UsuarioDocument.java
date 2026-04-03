package com.btg.fondos.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "usuario")
public class UsuarioDocument {

    @Id
    private String id;
    private String username;
    private String password;
    private String rol;
}
