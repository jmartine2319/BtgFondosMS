package com.btg.fondos.repository;

import com.btg.fondos.document.ClienteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<ClienteDocument, String> {
}
