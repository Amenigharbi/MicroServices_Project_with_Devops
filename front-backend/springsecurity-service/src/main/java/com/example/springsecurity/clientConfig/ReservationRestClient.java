package com.example.springsecurity.clientConfig;


import com.example.springsecurity.models.Reservation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FeignClient(name = "RESERVATION-SERVICE")
public interface ReservationRestClient {

    @GetMapping("/reservations/getone/{id}")
    Reservation findReservationById(@PathVariable Long id);

    @GetMapping("/reservations/all")
    List<Reservation> finAllReservation();
    @PutMapping("/reservations/update/{id}")
    Reservation reservationUpdate(@PathVariable Long id, @RequestBody Reservation reservation);
    @DeleteMapping("/reservations/delete/{id}")
    HashMap<String, String> deleteReservation(@PathVariable Long id);
    @PostMapping("/reservations/reserver/{attractionId}/{touristId}")
    Reservation ReservateAttractionByIdTourist(@PathVariable Long attractionId,
                                               @PathVariable Long touristId,  @RequestBody Reservation reservation);
    @PostMapping("/reservayions/reserv/{touristId}")
    Reservation reservationbyIdTourist(@PathVariable Long touristId,@RequestBody Reservation reservation);
    @GetMapping("/reservations/AllRess/{touristId}")
    List<Reservation> findREsByIdTourist(@PathVariable Long touristId);
    @GetMapping("/reservations/allRes/{id_attraction}")
    public List<Reservation> getReservationByAttraction(@PathVariable Long id_attraction);


    @DeleteMapping("/reservations/deleteResByTourist/{touristId}")
    public HashMap<String, String> deleteReservationByTouristId(@PathVariable Long touristId);



}

