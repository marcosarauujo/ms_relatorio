package com.marcos.msrelatorio.infrastructure.repository;

import com.marcos.msrelatorio.infrastructure.entity.RelatorioEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelatorioRepository extends MongoRepository<RelatorioEntity, String> {

    List<RelatorioEntity> findByCriancaId(Long criancaId);
}
