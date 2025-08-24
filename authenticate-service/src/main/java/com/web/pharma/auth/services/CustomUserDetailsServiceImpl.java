package com.web.pharma.auth.services;

import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User '{}' not found in database", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        // If already marked expired in DB
        if (user.isPasswordExpired()) {
            log.warn("User '{}' has expired password (flag set in DB)", username);
            throw new CredentialsExpiredException("Password expired. Please reset your password.");
        }

        // Else, check by date
        if (user.getPasswordChangedAt() != null) {
            long days = ChronoUnit.DAYS.between(user.getPasswordChangedAt(), LocalDateTime.now());
            if (days > 90) {
                user.setPasswordExpired(true);
                userRepository.save(user);
                log.warn("User '{}' password expired after {} days", username, days);
                throw new CredentialsExpiredException("Password expired. Please reset your password.");
            }
        }

        // Map roles to Spring Security authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        log.info("User '{}' loaded successfully with roles: {}", username,
                authorities.stream().map(GrantedAuthority::getAuthority).toList());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!Boolean.TRUE.equals(user.getEnabled()))
                .authorities(authorities)
                .build();
    }
}

