package com.btg.fondos.repository;

import com.btg.fondos.document.Visitante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitanteRepository extends MongoRepository<Visitante, String> {
}
