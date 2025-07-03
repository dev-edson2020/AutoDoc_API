package com.autodoc.repository;

import com.autodoc.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findAllByOrderByIdDesc();
    List<Documento> findByCreatedBy(String createdBy);
}
