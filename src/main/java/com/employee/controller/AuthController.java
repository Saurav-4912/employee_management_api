package com.employee.controller;

import com.employee.model.User;
import com.employee.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication: registration and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/register - Registers a new user.
     * Uses ModelAttribute to accept URL-encoded form parameters.
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User request) {
        User savedUser = authService.register(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", savedUser.getUsername());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * POST /api/auth/login - Authenticates a user and returns a JWT token.
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User request) {
        String token = authService.login(request);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Login successful");

        return ResponseEntity.ok(response);
    }
}
