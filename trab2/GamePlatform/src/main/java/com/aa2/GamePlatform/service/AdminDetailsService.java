package com.aa2.GamePlatform.service;

import com.aa2.GamePlatform.models.Admin;
import com.aa2.GamePlatform.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Verifica se a classe Admin está corretamente importada
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador não encontrado com o email: " + email));

        // Certifica-se de que os métodos getEmail() e getSenha() existem na classe Admin e são públicos
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");

        return new User(admin.getEmail(), admin.getSenha(), Collections.singletonList(authority));
    }
}