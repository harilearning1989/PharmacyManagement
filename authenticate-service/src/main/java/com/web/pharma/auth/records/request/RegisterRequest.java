package com.web.pharma.auth.records.request;

import com.web.pharma.auth.enums.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotEmpty Set<RoleEnum> roles
) {
}
