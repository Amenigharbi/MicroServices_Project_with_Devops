package com.example.springsecurity.repository;

import java.util.Optional;


import com.example.springsecurity.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  Optional<User> findOneByUsername (String username);
  //User fiindByUsername(String username);
  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  User findByEmail(String email);
  User findFirstByEmail (String email);

  User findByPasswordResetToken(String passwordResetToken);

  Optional<User> findByVerificationCode(String verificationCode);

}
