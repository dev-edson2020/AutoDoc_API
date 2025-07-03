package com.autodoc.service;

import com.autodoc.dto.UsuarioDTO;
import com.autodoc.model.Usuario;
import com.autodoc.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    public Usuario createUser(UsuarioDTO userDto) {
        Usuario user = new Usuario();
        user.setId(UUID.randomUUID().toString());
        user.setPlan(userDto.getPlan());
        user.setSubscriptionExpires(userDto.getSubscriptionExpires());
        user.setFullName(userDto.getFullName());
        user.setCreatedDate(new Date());
        user.setUpdatedDate(new Date());
        user.setEmail(userDto.getEmail());
        user.setDisabled(userDto.getDisabled());
        user.setIsVerified(userDto.getIsVerified());
        user.setAppId(userDto.getAppId());
        user.setAppRole(userDto.getAppRole());
        user.setRole(userDto.getRole());

        return userRepository.save(user);
    }

    public Usuario getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public Usuario getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Usuario updateUser(String id, UsuarioDTO userDto) {
        Usuario user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setPlan(userDto.getPlan());
            user.setSubscriptionExpires(userDto.getSubscriptionExpires());
            user.setFullName(userDto.getFullName());
            user.setUpdatedDate(new Date());
            user.setEmail(userDto.getEmail());
            user.setDisabled(userDto.getDisabled());
            user.setIsVerified(userDto.getIsVerified());
            user.setAppId(userDto.getAppId()); // Faltava atualizar appId
            user.setAppRole(userDto.getAppRole());
            user.setRole(userDto.getRole());

            return userRepository.save(user);
        }
        return null;
    }


    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}