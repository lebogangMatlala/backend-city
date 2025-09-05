package org.city.alert.alert.service.rest;

import lombok.RequiredArgsConstructor;
import org.city.alert.alert.service.dto.LoginRequest;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.repository.UserRepository;
import org.city.alert.alert.service.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/admin/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(user.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .filter(user -> loginRequest.getPassword().equals(user.getPassword()))
                .map(user -> ResponseEntity.ok(Map.of("status", "success")))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userRepository.findByUsername(loginRequest.username())
                .filter(user -> loginRequest.password().equals(user.getPassword()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }
}