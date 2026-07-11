package com.example.shopspringboot.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.shopspringboot.user.entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username, UserRole role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String validateAndGetUsername(String token) {
        return verify(token).getSubject();
    }

    public UserRole validateAndGetRole(String token) {
        String role = verify(token).getClaim("role").asString();
        if (role == null) {
            return UserRole.USER;
        }
        return UserRole.valueOf(role);
    }

    private DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
