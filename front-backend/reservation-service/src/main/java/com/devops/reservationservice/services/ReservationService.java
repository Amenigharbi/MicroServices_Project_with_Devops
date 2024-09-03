package com.devops.reservationservice.services;

import com.devops.reservationservice.models.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ReservationService {
    Reservation createReservation(Reservation reservation);
    Reservation createreservationbyattraction(Long id ,Reservation reservation);
    List<Reservation> FindAllReservation();
    List<Reservation> FindReservationByIdAttraction(Long id);
    Reservation reservationById(Long id);
    Reservation reservationUpdate(Long id, Reservation reservation);
    void deleteReservation(Long id);

    Reservation updateStatus(Long entityId, String newStatus);

    Reservation ReservateAttractionByIdTourist(Long attractionId, Long touristId, Reservation reservation);

    List<Reservation> findReservationByIdTourist(Long id);
    Reservation createreservationbyTourist(Long id, Reservation reservation);
    void deleteReservationsByTouristId(Long touristId);

    Reservation createReservation(Long attractionId,Long touristId, List<Long> serviceIds,Reservation reservation);

}
