package com.web.pharma.auth.services;

import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.records.request.AuthRequest;
import com.web.pharma.auth.records.request.RegisterRequest;

public interface AuthenticateService {
    void registerUser(RegisterRequest request);

    String authenticate(AuthRequest request);

    User getUserProfile(String name);

    void changePassword(String name, String oldPassword, String newPassword);
}
