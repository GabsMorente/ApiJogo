package com.example.crudproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String password = body.get("password");

        if (
                "usuario@esoft.com".equals(email)
                        &&
                        "Abc123".equals(password)
        ) {

            return ResponseEntity.ok(
                    Map.of(
                            "token",
                            UUID.randomUUID().toString()
                    )
            );
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}