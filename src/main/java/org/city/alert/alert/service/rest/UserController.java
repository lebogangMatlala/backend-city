package org.city.alert.alert.service.rest;

import lombok.RequiredArgsConstructor;
import org.city.alert.alert.service.enums.UserRoles;
import org.city.alert.alert.service.service.UserService;
import org.city.alert.alert.service.dto.LoginRequest;
import org.city.alert.alert.service.entity.User;
import org.city.alert.alert.service.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/admin/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "id", savedUser.getId()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCitizen(@RequestBody User user) {
        // Force role as CITIZEN to prevent misuse
        user.setRole(UserRoles.CITIZEN);
        User savedUser = userService.register(user);

        return ResponseEntity.ok(Map.of(
                "message", "Citizen registered successfully",
                "id", savedUser.getId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Optional<User> optionalUser = userService.findByEmail(loginRequest.email());

        // Debugging
        optionalUser.ifPresent(user -> {
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role object: " + user.getRole());
            System.out.println("Raw password: " + loginRequest.password());
            System.out.println("Stored hash: " + user.getPassword());
            System.out.println("Matches? " + passwordEncoder.matches(loginRequest.password(), user.getPassword()));
        });

        return optionalUser
                .filter(user -> passwordEncoder.matches(loginRequest.password(), user.getPassword()))
                .map(user -> {
                    String roleName = user.getRole() != null ? user.getRole().name() : "CITIZEN";
                    String departmentName = null;
                    int workloadCount = 0;

                    // Only technicians have departments and workload
                    if (user.getRole() == UserRoles.TECHNICIAN) {
                        departmentName = user.getDepartment() != null ? user.getDepartment().name() : null;
                        workloadCount = user.getWorkloadCount() != null ? user.getWorkloadCount() : 0;
                    }

                    String token = jwtUtil.generateToken(user.getEmail(), roleName);

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("email", user.getEmail());
                    userMap.put("name", user.getName());
                    userMap.put("surname", user.getSurname());
                    userMap.put("role", roleName);
                    userMap.put("department", departmentName);
                    userMap.put("workloadCount", workloadCount);
                    userMap.put("phone", user.getPhone());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("user", userMap);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }




    @GetMapping("/getAllUsers/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get single user by ID
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }
}
