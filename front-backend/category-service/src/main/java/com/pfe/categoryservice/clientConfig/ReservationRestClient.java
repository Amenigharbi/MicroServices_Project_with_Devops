package com.pfe.categoryservice.clientConfig;


import com.pfe.categoryservice.models.Comments;
import com.pfe.categoryservice.models.Reservation;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "RESERVATION-SERVICE")
public interface ReservationRestClient {
    @GetMapping("/reservations/getone/{id}")
    Reservation findReservationById(@PathVariable Long id);

    @GetMapping("/reservations/all")
    List<Reservation> finAllReservation();

    @GetMapping("/reservations/allRes/{id}")
    List<Reservation> showReservationByAttraction(@PathVariable Long id);
    @DeleteMapping("/reservations/deleteResByAtr/{attractionId}")
    @CircuitBreaker(name = "reservation-S",fallbackMethod = "getDefaultReservation")
    public HashMap<String, String> deleteReservationByAttractionId(@PathVariable Long attractionId);


    default List<Reservation> getDefaultReservation(Long id, Exception exception) {
        Reservation r = new Reservation();

        r.setPrice(0);
        r.setName("service not running");


        return List.of(r);
    }
}
