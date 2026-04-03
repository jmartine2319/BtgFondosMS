package com.btg.fondos.repository;

import com.btg.fondos.document.SucursalDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalRepository extends MongoRepository<SucursalDocument, String> {
}
