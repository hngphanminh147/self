package com.self.uaa.repository;

import com.self.uaa.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findById(String id);
}
