package com.server.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.server.app.config.JsonWebToken;
import com.server.app.dto.user.LoginDto;
import com.server.app.dto.user.TokenResponse;
import com.server.app.dto.user.UpdatePasswordDto;
import com.server.app.dto.user.UserCreateDto;
import com.server.app.dto.user.UserUpdateDto;
import com.server.app.entities.User;
import com.server.app.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JsonWebToken jwtUtil;

    public AuthController(UserService userService, JsonWebToken jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginDto dto) {
        User user = userService.login(dto.getUsername(), dto.getPassword());
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserCreateDto dto) {
        return ResponseEntity.ok(userService.signUp(dto));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<User> updateProfile(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.updateProfile(user.getId(), dto));
    }

    @PutMapping("/update/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User user, @Valid @RequestBody UpdatePasswordDto dto) {
        userService.updatePassword(user.getId(), dto);
        return ResponseEntity.ok().build();
    }
}
