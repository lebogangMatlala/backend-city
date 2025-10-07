package org.city.alert.alert.service.rest;

import lombok.RequiredArgsConstructor;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userRepository.findById(id).map(existing -> {
            existing.setName(user.getName());
            existing.setSurname(user.getSurname());
            existing.setUsername(user.getUsername());
            existing.setEmail(user.getEmail());
            existing.setRole(user.getRole());
            if(user.getPassword() != null && !user.getPassword().isEmpty()){
                existing.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userRepository.save(existing);
            return ResponseEntity.ok(Map.of("message", "User updated successfully"));
        }).orElse(ResponseEntity.badRequest().body(Map.of("error", "User not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user); // or implement soft delete
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        }).orElse(ResponseEntity.badRequest().body(Map.of("error", "User not found")));
    }
}

