package com.aathirai.microservices.user_service.controller;

import com.aathirai.microservices.user_service.component.JwtUtil;
import com.aathirai.microservices.user_service.dto.LoginRequest;
import com.aathirai.microservices.user_service.dto.RegisterRequest;
import com.aathirai.microservices.user_service.entity.User;
import com.aathirai.microservices.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController(UserService service, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) {
        return service.register(req);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        User user = service.findByEmail(req.getEmail());

        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}