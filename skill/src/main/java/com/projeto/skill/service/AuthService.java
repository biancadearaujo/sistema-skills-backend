package com.projeto.skill.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
	@Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationTime}")
    private long jwtExpirationTime;

    public String generateToken(String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationTime);

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public int obterIdUsuarioDoToken(String token) {
        try {
            String userIdStr = JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token).getSubject();
            return Integer.parseInt(userIdStr);
        } catch (Exception e) {
            return 0;
        }
    }
}
