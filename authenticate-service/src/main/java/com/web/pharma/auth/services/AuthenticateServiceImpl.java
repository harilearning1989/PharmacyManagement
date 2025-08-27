package com.web.pharma.auth.services;

import com.web.pharma.auth.entities.ActivityLog;
import com.web.pharma.auth.entities.Role;
import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;
import com.web.pharma.auth.repos.ActivityLogRepository;
import com.web.pharma.auth.repos.RoleRepository;
import com.web.pharma.auth.repos.UserRepository;
import com.web.pharma.auth.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateServiceImpl implements AuthenticateService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ActivityLogRepository logRepository;

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

    public void logActivity(Long userId, String action){
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setAction(action);
        logRepository.save(log);
    }

    // Get profile
    public User getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Change password
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logActivity(user.getId(), "PASSWORD_CHANGE");
    }

    // Admin: Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Admin: Update user role
    public void updateUserRole(Long userId, String newRoleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByName(newRoleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Replace all existing roles with the new role
        user.setRoles(new HashSet<>(Collections.singletonList(role)));

        userRepository.save(user);
        logActivity(user.getId(), "ROLE_UPDATE");
    }


    // Admin: Delete user
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }

    // Admin: Get activity logs
    public List<ActivityLog> getAllActivityLogs() {
        return logRepository.findAll();
    }

    public List<ActivityLog> getUserActivityLogs(Long userId) {
        return logRepository.findByUserId(userId);
    }

    public void loggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String username = userDetails.getUsername();
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                System.out.println("Logged in user: " + username);
                System.out.println("Roles: " + authorities);
            } else {
                // In case of OAuth2 / JWT, principal may just be a String (username)
                String username = principal.toString();
                System.out.println("Logged in user: " + username);
            }
        }
    }

}
