package com.btg.fondos.repository;

import com.btg.fondos.document.UsuarioDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<UsuarioDocument, String> {
    Optional<UsuarioDocument> findByUsername(String username);
}
