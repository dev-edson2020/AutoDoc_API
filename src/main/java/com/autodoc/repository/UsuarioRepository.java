package com.autodoc.repository;

import com.autodoc.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
}