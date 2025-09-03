package org.city.alert.alert.service.rest;

import lombok.RequiredArgsConstructor;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/admin/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(user.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Admin User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .filter(user -> loginRequest.getPassword().equals(user.getPassword()))
                .map(user -> ResponseEntity.ok(Map.of("status", "success")))
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }
}