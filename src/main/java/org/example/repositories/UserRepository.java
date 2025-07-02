package org.example.repositories;

import org.example.models.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long userID);
    User save(User user);
}
