package com.pfe.categoryservice.services.implementation;

import com.pfe.categoryservice.clientConfig.CommentRestClient;
import com.pfe.categoryservice.clientConfig.ReservationRestClient;
import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Category;
import com.pfe.categoryservice.models.Comments;
import com.pfe.categoryservice.models.Reservation;
import com.pfe.categoryservice.repository.AttractionRepository;
import com.pfe.categoryservice.repository.ServicesRepository;
import com.pfe.categoryservice.services.AttractionService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.module.FindException;
import java.util.*;

@Service
//gestion de transation ==>    openconxion == insert into   commit  or rollbak
@Transactional
public class AttractionImpl implements AttractionService {


    //3  type - autowird ==> sera  obsolte   / constructor   set or get

    final private AttractionRepository attractionRepository;

    final private ReservationRestClient reservationRestClient;

    final private CommentRestClient commentRestClient;
    final private ServicesRepository servicesRepository;

    public AttractionImpl(AttractionRepository attractionRepository,
                          ReservationRestClient reservationRestClient,
                          CommentRestClient commentRestClient,
                          ServicesRepository servicesRepository) {
        this.attractionRepository = attractionRepository;
        this.reservationRestClient = reservationRestClient;
        this.commentRestClient = commentRestClient;
        this.servicesRepository=servicesRepository;
    }

    @Override
    public Attraction createAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }


//    @Override
//    public List<Attraction> FindAllAttraction() {
//        List<Attraction> attraction = attractionRepository.findAll();
//        attraction.forEach((attractions -> {
//            try {
//                attractions.setReservation(reservationRestClient.showReservationByAttraction((attractions.getId())));
//                attractions.setComments(commentRestClient.showCommentByIdAttraction((attractions.getId())));
//            } catch (FeignException.ServiceUnavailable e) {
//
//                attractions.setReservation(null);
//                attractions.setComments(null);
//            }
//
//        }));
//        return attraction;
//    }
public List<Attraction> FindAllAttraction() {
    List<Attraction> attractions = attractionRepository.findAll();
    attractions.forEach(attraction -> {
        try {
            attraction.setReservation(reservationRestClient.showReservationByAttraction(attraction.getId()));
            System.out.println("Attraction ID: " + attraction.getReservation());
        }
        catch (FeignException.ServiceUnavailable e) {
            // Log error or handle the situation where reservation service is unavailable
            // For example, you can set reservation as empty or null for this attraction
            attraction.setReservation(null);

        }
        try {
            attraction.setComments(commentRestClient.showCommentByIdAttraction((attraction.getId())));
            System.out.println("Attraction ID: " + attraction.getComments());

        }
        catch (FeignException.ServiceUnavailable e) {
            // Log error or handle the situation where reservation service is unavailable
            // For example, you can set reservation as empty or null for this attraction
            attraction.setComments(null);
        }
    });
    return attractions;
}

    @Override
    public Optional<Attraction> findByID(Long id) {
        return Optional.empty();
    }

    @Override
    public Attraction attractionById(Long id) {

        Attraction attraction = attractionRepository.findById(id).orElseThrow(() -> new RuntimeException("id not found"));
        try {
            List<Reservation> reservations = reservationRestClient.showReservationByAttraction(attraction.getId());

            System.out.println("Resultas: " + reservations);

            attraction.setReservation(reservations);


        } catch (FeignException.ServiceUnavailable e) {
            attraction.setReservation(null);

        }
        try {

            List<Comments> comments = commentRestClient.showCommentByIdAttraction(attraction.getId());

            System.out.println("Resultas: " + comments);

            attraction.setComments(comments);

        } catch (FeignException.ServiceUnavailable e) {

            attraction.setComments(null);
        }

        return attraction;
    }

//    @Override
//    public Optional<Attraction> findByID(Long id) {
//        return attractionRepository.findById(id);
//    }

    @Override
    public Attraction attractionUpdate(Long id, Attraction attraction) {
        Attraction attraction1 = attractionById(id);
        if (attraction1 != null) {
            attraction.setId(id);
            return attractionRepository.save(attraction);
        } else {
            throw new RuntimeException("error");
        }
    }

//    @Override
//    public Optional<Attraction> getAttractionByCategoryId(Long id) {
//        return Optional.empty();
//    }
@Override
public Optional<Attraction> getAttractionByCategoryId(Long id) {
    return attractionRepository.findByCategoryId(id);
}



    @Override
        public HashMap<String,String> deleteAttraction(@PathVariable Long id){
            Optional<Attraction> attraction = attractionRepository.findById(id);
            HashMap message =  new HashMap();
            if(attraction != null) {
                try {
                   attractionRepository.deleteById(id);
                    message.put("etat", "attraction deleted");
                    return message;
                } catch (Exception e) {
                    message.put("etat", "Error" + e.getMessage());
                    return message;
                }
            }else {
                message.put("etat", "attraction not found");
                return message;
            }
        }

//    @Override
//    public HashMap<String, String> deleteAttractionAndCommentsAndRes(Long id) {
//            Optional<Attraction> attractionOptional = attractionRepository.findById(id);
//            HashMap<String, String> message = new HashMap<>();
//
//            if (attractionOptional.isPresent()) {
//                try {
//                    Attraction attraction = attractionOptional.get();
//                    // Supprimer les commentaires associés à l'attraction
//                   // List<Comments> comments = commentRestClient.showCommentByIdAttraction(attraction.getId());
//                    commentRestClient.deleteCommentsByAttractionId(id);
//                    // Supprimer l'attraction elle-même
//                    attractionRepository.deleteById(id);
//                    message.put("etat", "Attraction et les commentaires associés supprimés avec succès.");
//                } catch (Exception e) {
//                    message.put("etat", "Erreur lors de la suppression de l'attraction et des commentaires associés : " + e.getMessage());
//                }
//                try {
//                    Attraction attraction = attractionOptional.get();
//                    // Supprimer les commentaires associés à l'attraction
//                    // List<Comments> comments = commentRestClient.showCommentByIdAttraction(attraction.getId());
//                    reservationRestClient.deleteReservationByAttractionId(id);
//                    // Supprimer l'attraction elle-même
//                    attractionRepository.deleteById(id);
//                    message.put("etat", "Attraction , commentaires et  reservations associés supprimés avec succès.");
//                } catch (Exception e) {
//                    message.put("etat", "Erreur lors de la suppression de l'attraction , commentaires et reservations associés : " + e.getMessage());
//                }
//            } else {
//                message.put("etat", "Attraction non trouvée.");
//            }
//
//            return message;
//        }
//    }

    @Override
    public HashMap<String, String> deleteAttractionAndCommentsAndRes(Long id) {
        Optional<Attraction> attractionOptional = attractionRepository.findById(id);
        HashMap<String, String> message = new HashMap<>();

        if (attractionOptional.isPresent()) {
            try {
                Attraction attraction = attractionOptional.get();

                // Supprimer les services associés à l'attraction
                servicesRepository.deleteByAttractionId(id);

                // Supprimer les commentaires associés à l'attraction
                commentRestClient.deleteCommentsByAttractionId(id);
                reservationRestClient.deleteReservationByAttractionId(id);

                // Supprimer l'attraction elle-même
                attractionRepository.deleteById(id);

                message.put("etat", "Attraction, commentaires et services associés supprimés avec succès.");
            } catch (Exception e) {
                message.put("etat", "Erreur lors de la suppression de l'attraction, des commentaires et des services associés : " + e.getMessage());
            }
        } else {
            message.put("etat", "Attraction non trouvée.");
        }

        return message;
    }

}






