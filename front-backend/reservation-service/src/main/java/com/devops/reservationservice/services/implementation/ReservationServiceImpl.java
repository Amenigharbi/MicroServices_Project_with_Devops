package com.devops.reservationservice.services.implementation;

import com.devops.reservationservice.clientConfig.AttractionRestClient;
import com.devops.reservationservice.clientConfig.TouristRestClient;
import com.devops.reservationservice.models.*;
import com.devops.reservationservice.repository.ReservationRepository;
import com.devops.reservationservice.services.NotificationService;
import com.devops.reservationservice.services.ReservationService;
import com.devops.reservationservice.utils.EmailService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service

@Transactional

public class ReservationServiceImpl implements ReservationService {
    final NotificationService notificationService;
    final ReservationRepository reservationRepository;   //variable de reference
    final AttractionRestClient attractionRestClient;
    final TouristRestClient touristRestClient;
    final EmailService emailService;



    public ReservationServiceImpl(NotificationService notificationService,ReservationRepository reservationRepository, AttractionRestClient attractionRestClient, TouristRestClient touristRestClient, EmailService emailService) {
        this.attractionRestClient = attractionRestClient;
        this.notificationService = notificationService;
        this.reservationRepository = reservationRepository;
        this.touristRestClient = touristRestClient;
        this.emailService = emailService;


    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationServiceImpl notificationServiceimpl;
    @Override
    public Reservation createReservation(Reservation reservation) {

        return reservationRepository.save(reservation);
    }


    @Override
    public List<Reservation> FindAllReservation() {
        List<Reservation> reservationsList = reservationRepository.findAll();

        List<CompletableFuture<Void>> futures = reservationsList.stream()
                .map(r -> CompletableFuture.runAsync(() -> {
                    if (r.getAttractionId() != null) {
                        try {
                            r.setAttraction(attractionRestClient.findAttractionById(r.getAttractionId()));
                        } catch (FeignException.NotFound e) {
                            r.setAttraction(null); // Handle FeignException
                        }
                    } else {
                        r.setAttraction(null);
                    }

                    if (r.getTouristId() != null) {
                        try {
                            r.setTourist(touristRestClient.getoneById(r.getTouristId()));
                        } catch (FeignException.NotFound e) {
                            r.setTourist(null); // Handle FeignException
                        }
                    } else {
                        r.setTourist(null);
                    }
                }))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return reservationsList;
    }



    @Override
    public Reservation reservationById(Long id) {

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("id not found"));
        Attraction attraction = attractionRestClient.findAttractionById(reservation.getAttractionId());
        Tourist tourist = touristRestClient.getoneById(reservation.getTouristId());
        reservation.setAttraction(attraction);
        reservation.setTourist(tourist);
        return reservation;


    }

    @Override

    public List<Reservation> FindReservationByIdAttraction(Long attractionId) {
        List<Reservation> reservations = reservationRepository.findByAttractionId(attractionId);
        if (reservations.isEmpty()) {
            return Collections.emptyList();
        } else {
            return reservations;
        }
    }


//    @Override
//    public List<Reservation> FindReservationByIdAttraction(Long id) {
//        List<Reservation> reservationsList = reservationRepository.findByAttractionId(id);
//        reservationsList.forEach(r2 -> {
//            if(r2.getAttractionId() != null ){
//                try {
//                    r2.setAttraction(attractionRestClient.findAttractionById(r2.getAttractionId()));
//                    //r2.setTourist(touristRestClient.getoneById(r2.getTouristId()));
//                }
//                catch (FeignException.NotFound f){
//                    r2.setAttraction(null);
//                    //r2.setTourist(null);
//                }} else {
//                r2.setAttraction(null);
//                //r2.setTourist(null);
//            }
//
//        });
//        return reservationsList;
//
//    }

    @Override
    public Reservation reservationUpdate(Long id, Reservation reservation) {
        Reservation reservation1 = reservationById(id);
        if (reservation1 != null) {
            reservation.setId(id);
            return reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("error");
        }
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);

    }

    @Override

    //AttractionRestClient
    public Reservation createreservationbyattraction(Long id, Reservation reservation) {
        Attraction attraction = attractionRestClient.findAttractionById(id);


        //affectation

        reservation.setAttractionId(attraction.getId());
        return
                reservationRepository.save(reservation);


    }

    //    @Override
//    public Reservation updateStatus(Long entityId, String newStatus) {
//        Reservation entity = reservationRepository.findById(entityId)
//                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));
//
//        // Comparaison avec equals() au lieu de ==
//        if ("REFUSED".equals(newStatus)) {
//            entity.setStatus(Reservation.Status.REFUSED);
//        } else {
//            entity.setStatus(Reservation.Status.APPROVED);
//        }
//
//        return reservationRepository.save(entity);
//    }
/*   @Override
    public Reservation updateStatus(Long entityId, String newStatus) {
       Reservation entity = reservationRepository.findById(entityId)
               .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

       // Comparaison avec equals() au lieu de ==
       if ("REFUSED".equals(newStatus)) {
           entity.setStatus(Reservation.Status.REFUSED);
       } else if ("PENDING".equals(newStatus)) {
           entity.setStatus(Reservation.Status.PENDING);
       } else {
           entity.setStatus(Reservation.Status.APPROVED);
       }

       return reservationRepository.save(entity);

   }*/

 /*   @Override
    public Reservation updateStatus(Long entityId, String newStatus) {
        Reservation entity = reservationRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

        // Mise à jour du statut de la réservation
        if ("REFUSED".equals(newStatus)) {
            entity.setStatus(Reservation.Status.REFUSED);
        } else if ("APPROVED".equals(newStatus)) {
            entity.setStatus(Reservation.Status.APPROVED);
        } else {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
        entity.setAttraction(entity.getAttraction());
        entity.setTourist(entity.getTourist());
             reservationRepository.save(entity);
             



        return entity;
    }*/



    @Override
    public Reservation updateStatus(Long entityId, String newStatus) {
        Reservation entity = reservationRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + entityId));

        // Mise à jour du statut de la réservation
        if ("REFUSED".equals(newStatus)) {
            entity.setStatus(Reservation.Status.REFUSED);
        } else if ("APPROVED".equals(newStatus)) {
            entity.setStatus(Reservation.Status.APPROVED);
        } else {
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        // Vérifiez si l'attraction est null, si oui, chargez-la
        if (entity.getAttraction() == null) {
            Attraction attraction = attractionRestClient.findAttractionById(entity.getAttractionId());
            entity.setAttraction(attraction);
        }
        if (entity.getTourist() == null) {
            Tourist tourist = touristRestClient.getoneById(entity.getTouristId());
            entity.setTourist(tourist);
        }
        Reservation updatedReservation = reservationRepository.save(entity);

        // Récupérer les services associés aux identifiants via AttractionRestClient
        List<Services> validServices = entity.getServices().stream()
                .map(serviceId -> attractionRestClient.findserviceById(serviceId))
                .collect(Collectors.toList());

        // Préparation du message email
        String subject = "Reservation Update";
        String statusMessage;

        switch (updatedReservation.getStatus()) {
            case APPROVED:
                statusMessage = "Your reservation has been confirmed.";
                break;
            case REFUSED:
            default:
                statusMessage = "We regret to inform you that your reservation has been refused.";
                break;
        }

        String message = "Dear " + updatedReservation.getTourist().getUsername() + ",\n\n" +
                statusMessage + "\n\n" +
                "Reservation Details:\n" +
                "Attraction: " + updatedReservation.getAttraction().getName() + "\n" +
                "Services: " + validServices.stream()
                .map(Services::getName) // Utilisation d'une référence de méthode après avoir récupéré les objets Services
                .collect(Collectors.joining(", ")) + "\n" +
                "Reservation Date: " + updatedReservation.getDate_debut() + "\n" +
                "Price: " + updatedReservation.getPrice() + "\n\n" +
                "Thank you for your patience.\n" +
                "Best regards,\n" +
                "The Team";

        // Envoyer l'email
        emailService.sendEmail(updatedReservation.getTourist().getEmail(), subject, message);

        return updatedReservation;
    }


    @Override
    public Reservation createreservationbyTourist(Long id, Reservation reservation) {
        Tourist tourist = touristRestClient.getoneById(id);
        reservation.setTouristId(tourist.getId());
        return
                reservationRepository.save(reservation);
    }

    @Override
    public Reservation ReservateAttractionByIdTourist(Long attractionId, Long touristId, Reservation reservation) {
        Attraction attraction = attractionRestClient.findAttractionById(attractionId);
        Tourist tourist = touristRestClient.getoneById(touristId);
        reservation.setAttractionId(attraction.getId());
        reservation.setTouristId(tourist.getId());
        return reservationRepository.save(reservation);
    }


    @Override
    public List<Reservation> findReservationByIdTourist(Long id) {
        List<Reservation> reservations = reservationRepository.findReservationByTouristId(id);
        if (reservations.isEmpty()) {
            return Collections.emptyList();
        } else {
            return reservations;

        }
    }

    @Override
    public void deleteReservationsByTouristId(Long touristId) {
        List<Reservation> reservations = reservationRepository.findReservationByTouristId(touristId);
        for (Reservation reservation : reservations) {
            reservationRepository.delete(reservation);
        }
    }

    @Override
    public Reservation createReservation(Long attractionId, Long touristId, List<Long> serviceIds, Reservation reservation) {
        Attraction attraction = attractionRestClient.findAttractionById(attractionId);
        Tourist tourist = touristRestClient.getoneById(touristId);
        if (attraction == null) {
            throw new RuntimeException("Attraction not found");
        }

    /*    if (!isReservationDatesValid(attractionId, reservation.getDate_debut(), reservation.getDate_fin())) {
            throw new RuntimeException("Invalid reservation dates [ "+ reservation.getDate_debut() + ","+ reservation.getDate_fin() + "]" +". There is already a reservation that overlaps with this period.");

        }*/


        // Filtrer les services existants en fonction des identifiants fournis
        List<Services> validServices = (serviceIds == null || serviceIds.isEmpty()) ?
                Collections.emptyList() :
                attraction.getServices().stream()
                        .filter(service -> serviceIds.contains(service.getId()))
                        .collect(Collectors.toList());

        // Extraire les identifiants des services valides
        List<Long> validServiceIds = validServices.stream()
                .map(Services::getId)
                .collect(Collectors.toList());

        // Créer la réservation même si aucun service valide n'est trouvé
        reservation.setAttraction(attraction);
        reservation.setAttractionId(attraction.getId());
        reservation.setTouristId(tourist.getId());
        reservation.setTourist(tourist);
        reservation.setServices(validServiceIds); // Set the service IDs for persistence
        reservation.setServiceslist(validServices); // Set the full service objects for transient use

        Reservation savedReservation = reservationRepository.save(reservation);

        // Construire le sujet et le message de l'e-mail
        String subject = "Reservation Received";
        String message = "Dear " + tourist.getUsername() + ",\n\n" +
                "We have received your reservation at " + attraction.getName() + ".\n" +
                "Please wait for a confirmation.\n\n" +
                "Reservation Details:\n" +
                "Attraction: " + attraction.getName() + "\n" +
                "Services: " + validServices.stream().map(Services::getName).collect(Collectors.joining(", ")) + "\n" +
                "Reservation Date: " + savedReservation.getDate_debut() + "\n\n" +
                "Thank you for your patience.\n" +
                "Best regards,\n" +
                "The Team";

        // Envoyer un e-mail à l'entreprise
        emailService.sendEmail(tourist.getEmail(), subject, message);
/*
        String notificationMessage = "Une nouvelle réservation a été créée pour l'attraction " + attraction.getName();
        notificationService.sendNotification(notificationMessage,tourist.getId());*/


        // Send notification
        String notificationMessage = "Une nouvelle réservation a été créée pour l'attraction " + reservation.getAttraction().getName();

        Notification notification = new Notification(notificationMessage);
        notification.setTouristId(touristId); // Ajouter l'ID du touriste
        notificationService.sendNotification(notificationMessage,tourist.getId(),attraction.getId());

        // Envoyer la notification par WebSocket
       // messagingTemplate.convertAndSend("/topic/notifications", notification);


        return savedReservation;
    }

    private boolean isReservationDatesValid(Long attractionId, Date startDate, Date endDate) {
        List<Reservation> existingReservations = reservationRepository.findByAttractionId(attractionId);
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (Reservation existingReservation : existingReservations) {
            LocalDate existingStartLocalDate = existingReservation.getDate_debut().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate existingEndLocalDate = existingReservation.getDate_fin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (startLocalDate.isBefore(existingEndLocalDate) && endLocalDate.isAfter(existingStartLocalDate)) {
                return false; // Il y a un chevauchement de dates
            }
        }
        return true; // Aucun chevauchement trouvé
    }

}


