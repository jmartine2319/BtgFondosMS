package com.btg.fondos.repository;

import com.btg.fondos.document.ProductoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<ProductoDocument, String> {
}
