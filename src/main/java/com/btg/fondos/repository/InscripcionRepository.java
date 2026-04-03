package com.btg.fondos.repository;

import com.btg.fondos.document.InscripcionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends MongoRepository<InscripcionDocument, String> {
    List<InscripcionDocument> findByIdCliente(String idCliente);
    boolean existsByIdClienteAndIdProductoAndEstado(String idCliente, String idProducto, String estado);
    java.util.Optional<InscripcionDocument> findByIdClienteAndIdProductoAndEstado(String idCliente, String idProducto, String estado);
}
