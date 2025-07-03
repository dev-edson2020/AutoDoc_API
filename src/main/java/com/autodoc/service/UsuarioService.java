package com.autodoc.service;

import com.autodoc.dto.UsuarioDTO;
import com.autodoc.model.Usuario;
import com.autodoc.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criarUsuario(UsuarioDTO userDto) {
        if (usuarioRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(userDto.getEmail());
        usuario.setPassword(passwordEncoder.encode(userDto.getPassword()));
        usuario.setFullName(userDto.getFullName());
        usuario.setRole(userDto.getRole());
        // Defina outros campos conforme necessário

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> getUserById(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUserByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> updateUser(String id, UsuarioDTO userDto) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (userDto.getFullName() != null) {
                        usuario.setFullName(userDto.getFullName());
                    }
                    if (userDto.getPassword() != null) {
                        usuario.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    }
                    if (userDto.getRole() != null) {
                        usuario.setRole(userDto.getRole());
                    }
                    // Atualize outros campos conforme necessário
                    return usuarioRepository.save(usuario);
                });
    }

    public boolean deleteUser(String id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}