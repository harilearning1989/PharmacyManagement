package com.web.pharma.utils;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class JwtUtil {


    private static final String SECRET = System.getenv().getOrDefault("SECURITY_JWT_SECRET", "change-this-secret");


    public static Map<String, Object> validateToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        if (!signedJWT.verify(new MACVerifier(SECRET.getBytes()))) {
            throw new RuntimeException("Invalid JWT signature");
        }
        if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            throw new RuntimeException("JWT expired");
        }
        return signedJWT.getJWTClaimsSet().getClaims();
    }


    @SuppressWarnings("unchecked")
    public static List<String> getRoles(Map<String, Object> claims) {
        Object roles = claims.get("roles");
        return roles instanceof List<?> ? (List<String>) roles : List.of();
    }
}
