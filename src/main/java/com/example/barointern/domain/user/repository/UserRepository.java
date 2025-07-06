package com.example.barointern.domain.user.repository;

import com.example.barointern.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> emailIndex = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(User user) {
        users.put(user.getId(), user);
        emailIndex.put(user.getEmail(), user.getId());
        return user;
    }

    public Optional<User> findByEmail(String email) {
        Long id = emailIndex.get(email);
        return id != null ? Optional.of(users.get(id)) : Optional.empty();
    }

    public Long generateId() {
        return idGenerator.getAndIncrement();
    }

    public boolean existByEmail(String email) {
        return emailIndex.containsKey(email);
    }
}
