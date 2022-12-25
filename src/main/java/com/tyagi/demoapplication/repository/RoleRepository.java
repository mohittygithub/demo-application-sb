package com.tyagi.demoapplication.repository;

import com.tyagi.demoapplication.enums.RoleEnum;
import com.tyagi.demoapplication.model.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByName(RoleEnum name);
}
