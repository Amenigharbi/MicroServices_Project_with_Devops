package com.pfe.categoryservice.repository;

import com.pfe.categoryservice.models.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface AttractionRepository extends JpaRepository <Attraction , Long> {
    Optional<Attraction> findByCategoryId(Long id);
    void deleteByCategoryId(Long categoryId);

}
