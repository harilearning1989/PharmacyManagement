package com.web.pharma.auth.services;

import com.web.pharma.auth.entities.Role;
import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;
import com.web.pharma.auth.repos.RoleRepository;
import com.web.pharma.auth.repos.UserRepository;
import com.web.pharma.auth.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void registerUser(RegisterRequest request) {
        log.info("Attempting to register user: {}", request.username());

        if (userRepository.findByUsername(request.username()).isPresent()) {
            log.warn("Registration failed - username '{}' already exists", request.username());
            throw new RuntimeException("Username already exists");
        }

        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> {
                    log.error("ROLE_EMPLOYEE not found in database");
                    return new RuntimeException("ROLE_EMPLOYEE not found");
                });

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setRoles(Collections.singleton(employeeRole));

        userRepository.save(user);

        log.info("User '{}' registered successfully", request.username());
    }

    @Override
    public String authenticate(AuthRequest request) {
        log.info("Authentication attempt for user: {}", request.username());

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> {
                    log.warn("Authentication failed - username '{}' not found", request.username());
                    return new BadCredentialsException("Invalid username or password");
                });

        // Account lock check
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            log.warn("User '{}' attempted login but account is locked until {}",
                    request.username(), user.getLockUntil());
            throw new RuntimeException("Account is locked until " + user.getLockUntil());
        }

        // Password check
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 3) {
                user.setLockUntil(LocalDateTime.now().plusMinutes(15));
                user.setFailedAttempts(0);
                log.warn("User '{}' account locked due to too many failed login attempts",
                        request.username());
            } else {
                log.warn("Invalid password attempt {} for user '{}'", attempts, request.username());
            }

            userRepository.save(user);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Password expiry check (90 days)
        if (user.getPasswordChangedAt() != null &&
                user.getPasswordChangedAt().plusDays(90).isBefore(LocalDateTime.now())) {
            log.warn("User '{}' password expired", request.username());
            throw new RuntimeException("Password expired, please reset.");
        }

        // Reset failed attempts
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        userRepository.save(user);

        String token = jwtTokenUtil.generateToken(
                user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList()
        );

        log.info("User '{}' authenticated successfully", request.username());
        return token;
    }

}
