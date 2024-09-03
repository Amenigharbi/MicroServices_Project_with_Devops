package com.pfe.categoryservice.repository;

import com.pfe.categoryservice.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public interface ServicesRepository extends JpaRepository<Services,Long> {
    List<Services> findByattractionId(long id);
    void deleteByAttractionId(Long attractionId);


}
