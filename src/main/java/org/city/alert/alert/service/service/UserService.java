package org.city.alert.alert.service.service;

import org.city.alert.alert.service.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);
    Optional<User> findByEmail(String username);
    List<User> getAllUsers();
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);

    User getUserById(Long id);
}
