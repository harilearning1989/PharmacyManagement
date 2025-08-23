package com.web.pharma.auth.services;

import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.repos.UserRepository;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // if already marked expired in DB
        if (user.isPasswordExpired()) {
            throw new CredentialsExpiredException("Password expired. Please reset your password.");
        }

        // else, check by date
        if (user.getPasswordChangedAt() != null) {
            long days = ChronoUnit.DAYS.between(user.getPasswordChangedAt(), LocalDateTime.now());
            if (days > 90) {
                user.setPasswordExpired(true);
                userRepository.save(user);
                throw new CredentialsExpiredException("Password expired. Please reset your password.");
            }
        }

        // Map roles to Spring Security authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!Boolean.TRUE.equals(user.getEnabled()))
                .authorities(authorities)
                .build();
    }
}

