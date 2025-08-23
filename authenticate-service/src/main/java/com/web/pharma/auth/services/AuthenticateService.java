package com.web.pharma.auth.services;

import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;

public interface AuthenticateService {
    void registerUser(RegisterRequest request);

    String authenticate(AuthRequest request);
}
