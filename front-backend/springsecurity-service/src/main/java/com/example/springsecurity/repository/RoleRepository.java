package com.example.springsecurity.repository;

import java.util.Optional;

import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
