package com.example.springsecurity.services.Imp;

import com.example.springsecurity.clientConfig.CommentRestClient;
import com.example.springsecurity.clientConfig.ReservationRestClient;
import com.example.springsecurity.models.Comments;
import com.example.springsecurity.models.Reservation;
import com.example.springsecurity.models.Tourist;
import com.example.springsecurity.repository.TouristRepository;
import com.example.springsecurity.services.TouristService;

import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;



    @Service
    @Transactional
    public class TouristImpl implements TouristService {


        final TouristRepository touristRepository;
        final CommentRestClient commentRestClient;
        final ReservationRestClient reservationRestClient;

        public TouristImpl(TouristRepository touristRepository,
                           CommentRestClient commentRestClient,
                           ReservationRestClient reservationRestClient) {

            this.touristRepository = touristRepository;
            this.commentRestClient = commentRestClient;
            this.reservationRestClient = reservationRestClient;
        }


        @Override
        public Tourist createTourist(Tourist tourist) {
            return touristRepository.save(tourist);
        }


        @Override
        public Tourist updateTourist(Long id, Tourist tourist) {
            Tourist tourist1 = findTouristById(id);
            if (tourist1 != null) {
                tourist.setId(id);
                return touristRepository.save(tourist);

            } else {
                throw new RuntimeException("error");
            }
        }

        @Override
        public String deleteTourist(Long id) {
            Optional<Tourist> tourist = touristRepository.findById(id);
            if (tourist.isPresent()) {
                touristRepository.deleteById(id);
                return "tourist deleted";
            } else {
                return "count not found";
            }
        }



        @Override
        public HashMap<String, String> deleteTouristAndRelatedData(Long touristId) {
            HashMap<String, String> message = new HashMap<>();

            // Supprimer les commentaires associés au touriste
            try {
                List<Comments> comments = commentRestClient.getCommentsByIdTourist(touristId);
                for (Comments comment : comments) {
                    commentRestClient.deletComment(comment.getId());
                }
            } catch (Exception e) {
                message.put("etat_commentaires", "Erreur lors de la suppression des commentaires : " + e.getMessage());
            }

            // Supprimer les réservations associées au touriste
            try {
                List<Reservation> reservations = reservationRestClient.findREsByIdTourist(touristId);
                for (Reservation reservation : reservations) {
                    reservationRestClient.deleteReservation(reservation.getId());
                }
            } catch (Exception e) {
                message.put("etat_reservations", "Erreur lors de la suppression des réservations : " + e.getMessage());
            }

            // Supprimer le touriste lui-même
            try {
                Optional<Tourist> touristOptional = touristRepository.findById(touristId);
                if (touristOptional.isPresent()) {
                    touristRepository.delete(touristOptional.get());
                    message.put("etat_touriste", "Touriste supprimé avec succès.");
                } else {
                    message.put("etat_touriste", "Touriste non trouvé.");
                }
            } catch (Exception e) {
                message.put("etat_touriste", "Erreur lors de la suppression du touriste : " + e.getMessage());
            }

            return message;
        }





        @Override
        public List<Tourist> findAllTourist() {
            List<Tourist> touristList = touristRepository.findAll();
            touristList.forEach(tourist -> {
                try {
                    tourist.setComments(commentRestClient.getCommentsByIdTourist(tourist.getId()));
                    tourist.setReservation(reservationRestClient.findREsByIdTourist(tourist.getId()));
                    System.out.println("Tourist ID: " + tourist.getComments());
                    System.out.println("Tourist ID: " + tourist.getReservation());
                } catch (FeignException.ServiceUnavailable e) {
                    tourist.setReservation(null);
                    tourist.setComments(null);
                }
            });
            return touristList;
        }

        @Override
        public Optional<Tourist> findById(Long id) {
            return Optional.empty();
        }




        @Override
        public Tourist findTouristById(Long id) {
            Tourist tourist = touristRepository.findById(id).orElseThrow(() ->
                    new RuntimeException("id not found"));
            try {
                List<Reservation> reservations = reservationRestClient.findREsByIdTourist(tourist.getId());
                List<Comments> comments = commentRestClient.getCommentsByIdTourist(tourist.getId());
                System.out.println("Resultas: " + reservations);
                System.out.println("Resultas: " + comments);
                tourist.setReservation(reservations);
                tourist.setComments(comments);

            } catch (FeignException.ServiceUnavailable e) {
                tourist.setReservation(null);
                tourist.setComments(null);
            }
            return tourist;
        }


    }