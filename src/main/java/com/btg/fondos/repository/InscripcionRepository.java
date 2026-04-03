package com.btg.fondos.repository;

import com.btg.fondos.document.Inscripcion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends MongoRepository<Inscripcion, String> {
    List<Inscripcion> findByIdCliente(String idCliente);
    boolean existsByIdClienteAndIdProductoAndEstado(String idCliente, String idProducto, String estado);
    java.util.Optional<Inscripcion> findByIdClienteAndIdProductoAndEstado(String idCliente, String idProducto, String estado);
}
