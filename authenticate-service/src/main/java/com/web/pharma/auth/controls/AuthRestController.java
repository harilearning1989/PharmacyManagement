package com.web.pharma.auth.controls;

import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;
import com.web.pharma.auth.records.response.AuthResponse;
import com.web.pharma.auth.services.AuthenticateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("authenticate")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthRestController {

    private final AuthenticateService authenticateService;

    //@PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with a default role.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        log.info("Register request received for email: {}", request.username());

        authenticateService.registerUser(request);

        log.info("User registered successfully: {}", request.username());
        return ResponseEntity.ok("User registered successfully.");
    }

    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates the user and returns a JWT token if valid.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Authenticated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
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

    @GetMapping("/me")
    public ResponseEntity<User> getProfile(Principal principal) {
        return ResponseEntity.ok(authenticateService.getUserProfile(principal.getName()));
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> request, Principal principal){
        authenticateService.changePassword(principal.getName(), request.get("oldPassword"), request.get("newPassword"));
        return ResponseEntity.ok("Password changed successfully");
    }

}
