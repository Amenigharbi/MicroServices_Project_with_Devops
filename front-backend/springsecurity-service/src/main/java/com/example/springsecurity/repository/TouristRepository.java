package com.example.springsecurity.repository;

import com.example.springsecurity.models.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TouristRepository extends JpaRepository<Tourist,Long> {
    Optional<Tourist> findByUsername (String username);
}
