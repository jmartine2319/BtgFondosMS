package com.btg.fondos.repository;

import com.btg.fondos.document.VisitanteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitanteRepository extends MongoRepository<VisitanteDocument, String> {
}
