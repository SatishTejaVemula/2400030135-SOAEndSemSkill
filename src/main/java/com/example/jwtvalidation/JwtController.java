package com.example.jwtvalidation;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
public class JwtController {

    private final JwtValidator jwtValidator;

    public JwtController(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @GetMapping("/validate")
    @Operation(
            summary = "Validate JWT Token",
            security = @SecurityRequirement(
                    name = "Bearer Authentication"
            )
    )
    public ResponseEntity<?> validateToken(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            ) String authorization) {

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            "Missing or invalid Authorization header"
                    ));
        }

        String token = authorization.substring(7).trim();

        if (token.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "JWT token is empty"));
        }

        Claims claims = jwtValidator.validateToken(token);

        Map<String, Object> response = Map.of(
                "message", "JWT token is valid",
                "username", claims.getSubject(),
                "email", claims.get("email", String.class),
                "role", claims.get("role", String.class)
        );

        return ResponseEntity.ok(response);
    }
}
