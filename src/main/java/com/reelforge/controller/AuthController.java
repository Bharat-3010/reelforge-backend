package com.reelforge.controller;


import com.reelforge.dto.AuthRequest;
import com.reelforge.dto.AuthResponse;
import com.reelforge.model.User;
import com.reelforge.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody AuthRequest request
    ) {
        return authService.login(request);
    }
}