package com.devops.reservationservice.controllers;


import com.devops.reservationservice.models.Notification;
import com.devops.reservationservice.models.Reservation;
import com.devops.reservationservice.services.NotificationService;
import com.devops.reservationservice.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController

@RequestMapping("/reservations")

public class ReservationController {

    final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){

        this.reservationService=reservationService;

    }
   @Autowired
   private NotificationService notificationService;
    @PostMapping("/save")
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }


    @PostMapping("/{attractionId}/{touristId}")
    public Reservation createReservation(@PathVariable Long attractionId,@PathVariable Long touristId, @RequestParam(required = false) List<Long> serviceIds, Reservation reservation) {
        try {
            return reservationService.createReservation(attractionId,touristId, serviceIds,reservation);

        } catch (RuntimeException e) {
           throw new RuntimeException("error"+e.getMessage());
        }



    }









    @GetMapping("/all")
    public List<Reservation> all_res() {
        return reservationService.FindAllReservation();
    }

    @GetMapping("/getone/{id}")
    public Reservation getoneById(@PathVariable Long id) {
        return reservationService.reservationById(id);
    }

    @PutMapping("/update/{id}")
    public Reservation reservationUpdate(@PathVariable Long id, @RequestBody Reservation reservation) {
        return reservationService.reservationUpdate(id, reservation);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteComment(@PathVariable Long id) {
        Reservation r = reservationService.reservationById(id);
        HashMap message = new HashMap();
        if (r != null) {
            try {
               reservationService.deleteReservation(id);
                message.put("etat", "reservation deleted");
                return message;
            } catch (Exception e) {
                message.put("etat", "Error" + e.getMessage());
                return message;
            }
        } else {
            message.put("etat", "reservation not found");
            return message;


        }

    }

    // createreservationbyattraction
    @PostMapping("/ajout/{id_attraction}")
    public Reservation create (@RequestBody Reservation reservation , @PathVariable Long id_attraction) {
    return reservationService.createreservationbyattraction(id_attraction,reservation);
    }

    @GetMapping("/allRes/{attractionId}")
    public List<Reservation> getReservationByAttraction(@PathVariable Long attractionId) {
        return reservationService.FindReservationByIdAttraction(attractionId);
    }
    @PutMapping("/statusRes/{id}")
    public Reservation getoneByIdstatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String status = statusMap.get("status");
        return reservationService.updateStatus(id, status);
    }


    @PostMapping("/reserv/{touristId}")
    public Reservation reservationbyIdTourist(@PathVariable Long touristId,@RequestBody Reservation reservation){
        return reservationService.createreservationbyTourist(touristId,reservation);
    }




    @PostMapping("/reserver/{attractionId}/{touristId}")
    public Reservation ReservateAttractionByIdTourist(@PathVariable Long attractionId,
                                                      @PathVariable Long touristId,  @RequestBody Reservation reservation){
        return reservationService.ReservateAttractionByIdTourist(attractionId,touristId,reservation);

    }
    @DeleteMapping("/deleteResByAtr/{attractionId}")
    public HashMap<String, String> deleteReservationByAttractionId(@PathVariable Long attractionId) {
        List<Reservation> reservations = reservationService.FindReservationByIdAttraction(attractionId);
        HashMap<String, String> message = new HashMap<>();

        if (!reservations.isEmpty()) {
            try {
                // Supprimer chaque commentaire associé à l'attraction
                for (Reservation reservation : reservations) {
                   reservationService.deleteReservation(reservation.getId());
                }

                message.put("etat", "Tous les reservations de l'attraction ont été supprimés avec succès.");
            } catch (Exception e) {
                message.put("etat", "Une erreur s'est produite lors de la suppression des reservation : " + e.getMessage());
            }
        } else {
            message.put("etat", "Aucun reservation trouvé pour l'attraction spécifiée.");
        }

        return message;
    }



    @GetMapping("/AllRess/{touristId}")
    public List<Reservation> findREsByIdTourist(@PathVariable Long touristId){
        return reservationService.findReservationByIdTourist(touristId);
    }


    @DeleteMapping("/deleteResByTourist/{touristId}")
    public HashMap<String, String> deleteReservationByTouristId(@PathVariable Long touristId) {
        List<Reservation> reservations = reservationService.findReservationByIdTourist(touristId);
        HashMap<String, String> message = new HashMap<>();

        if (!reservations.isEmpty()) {
            try {
                // Supprimer chaque reservation associé à l'attraction
                for (Reservation reservation : reservations) {
                    reservationService.deleteReservation(reservation.getId());
                }

                message.put("etat", "Tous les reservations de la part de tourist ont été supprimés avec succès.");
            } catch (Exception e) {
                message.put("etat", "Une erreur s'est produite lors de la suppression des reservation : " + e.getMessage());
            }
        } else {
            message.put("etat", "Aucun reservation trouvé de la part de tourist spécifiée.");
        }

        return message;
    }



}



