package com.autodoc.security;

import com.autodoc.model.Usuario;
import com.autodoc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Usuario> userOpt = userRepository.findByEmail(email);

        Usuario user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado com email: " + email)
        );

        return new User(
                user.getEmail(),
                user.getPassword(),  // Use a senha hasheada do usuário
                Collections.emptyList()  // Ou carregue as roles/autoridades do usuário
        );
    }
}