package com.web.pharma.auth.services;

import com.web.pharma.auth.entities.Role;
import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;
import com.web.pharma.auth.repos.RoleRepository;
import com.web.pharma.auth.repos.UserRepository;
import com.web.pharma.auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticateServiceImpl implements AuthenticateService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Role employeeRole = roleRepository.findByName("ROLE_EMPLOYEE")
                .orElseThrow(() -> new RuntimeException("ROLE_EMPLOYEE not found"));

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEnabled(true);
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setRoles(Collections.singleton(employeeRole));

        userRepository.save(user);
    }

    @Override
    public String authenticate(AuthRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        // Account lock check
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Account is locked until " + user.getLockUntil());
        }

        // Password check
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= 3) {
                user.setLockUntil(LocalDateTime.now().plusMinutes(15));
                user.setFailedAttempts(0);
            }
            userRepository.save(user);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Password expiry check (90 days)
        if (user.getPasswordChangedAt() != null &&
                user.getPasswordChangedAt().plusDays(90).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password expired, please reset.");
        }

        // Reset failed attempts
        user.setFailedAttempts(0);
        user.setLockUntil(null);
        userRepository.save(user);

        return jwtUtil.generateToken(user.getUsername(),
                user.getRoles().stream().map(Role::getName).toList());
    }

}
