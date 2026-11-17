package com.example.jwtvalidation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtGenerator implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(JwtGenerator.class);

    private final SecretKey secretKey;
    private final String username;
    private final String email;
    private final String role;

    public JwtGenerator(
            SecretKey secretKey,
            @Value("${jwt.sample.username:john}") String username,
            @Value("${jwt.sample.email:satish@gamil.com.com}") String email,
            @Value("${jwt.sample.role:admin}") String role) {
        this.secretKey = secretKey;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String generateSampleToken() {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("email", email)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(1, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Sample JWT (valid for 1 hour): {}", generateSampleToken());
    }
}
