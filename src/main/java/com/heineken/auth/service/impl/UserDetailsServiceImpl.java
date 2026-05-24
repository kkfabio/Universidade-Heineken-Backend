package com.heineken.auth.service.impl;

import com.heineken.auth.model.entity.User;
import com.heineken.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userEntity.getRole());

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(List.of(authority))
                .build();
    }
}
