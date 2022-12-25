package com.tyagi.demoapplication.repository;

import com.tyagi.demoapplication.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);
  Boolean existsByUsername(String username);
}
