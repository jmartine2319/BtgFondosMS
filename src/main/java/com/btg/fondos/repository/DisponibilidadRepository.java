package com.btg.fondos.repository;

import com.btg.fondos.document.DisponibilidadDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadRepository extends MongoRepository<DisponibilidadDocument, String> {
}
