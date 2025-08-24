package com.web.pharma.auth.controls;

import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;
import com.web.pharma.auth.records.response.AuthResponse;
import com.web.pharma.auth.services.AuthenticateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authenticate")
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthenticateService authenticateService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        log.info("Register request received for email: {}", request.username());

        authenticateService.registerUser(request);

        log.info("User registered successfully: {}", request.username());
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        log.info("Login attempt for username: {}", request.username());

        String token = authenticateService.authenticate(request);

        log.info("User authenticated successfully: {}", request.username());
        return ResponseEntity.ok(new AuthResponse(token));
    }

  /* @PreAuthorize("isAuthenticated()")
    @PostMapping("/reset-my-password")
    public ResponseEntity<?> resetOwnPassword(@RequestParam String oldPassword,
                                              @RequestParam String newPassword,
                                              Authentication auth) {
        String currentUsername = auth.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setPasswordExpired(false);

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }*/

}
