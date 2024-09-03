package com.devops.reservationservice.repository;

import com.devops.reservationservice.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReservationRepository extends JpaRepository <Reservation, Long>{
    List<Reservation>findByAttractionId(Long id);
    List<Reservation> findReservationByTouristId(Long id);


}
