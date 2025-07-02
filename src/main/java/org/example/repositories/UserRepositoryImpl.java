package org.example.repositories;

import org.example.models.User;

import java.util.HashMap;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    HashMap<Long, User> userMap = new HashMap<>();

    @Override
    public Optional<User> findById(long userID) {
        return Optional.ofNullable(userMap.get(userID));
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        return user;
    }
}
