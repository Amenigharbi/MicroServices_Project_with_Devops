package com.devops.commentsservice.service.implementation;

import com.devops.commentsservice.clientConfig.AttractionRestClient;
import com.devops.commentsservice.clientConfig.TouristRestClient;
import com.devops.commentsservice.models.Attraction;
import com.devops.commentsservice.models.Comments;
import com.devops.commentsservice.models.Tourist;
import com.devops.commentsservice.repository.CommentsRepository;
import com.devops.commentsservice.service.CommentService;
import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;

@Service




public class CommentServiceImpl implements CommentService {
    final CommentsRepository commentsRepository;

    final AttractionRestClient attractionRestClient;
    final TouristRestClient touristRestClient;

    public CommentServiceImpl(CommentsRepository commentsRepository,
                              TouristRestClient touristRestClient,
                              AttractionRestClient attractionRestClient){
        this.commentsRepository= commentsRepository;
        this.touristRestClient = touristRestClient;
        this.attractionRestClient= attractionRestClient;
    }

    @Override
    public Comments addComment(Comments comments) {

        return commentsRepository.save(comments);
    }

    @Override
    public Comments CommentById(Long id) {
        Comments comments = commentsRepository.findById(id).orElseThrow(() ->new RuntimeException("id not found"));
        Attraction attraction = attractionRestClient.findAttractionById(comments.getAttractionId());
        Tourist tourist = touristRestClient.getoneById(comments.getTouristId());
        comments.setAttraction(attraction);
        comments.setTourist(tourist);
        return comments;
    }

    @Override
    public Comments updateComment(Long id, Comments comment) {
        Comments comment1 = CommentById(id);
        if (comment1 != null) {
            comment.setId(id);
            return commentsRepository.save(comment);
        } else {
            throw new RuntimeException("error");

        }
    }

    @Override
    public void deleteComment(Long id) {

        commentsRepository.deleteById(id);
    }




    @Override
    public Comments addCommentByAttraction(Long id, Comments comments) {
        Attraction attraction = attractionRestClient.findAttractionById(id);
        comments.setAttractionId(attraction.getId());
        return
                commentsRepository.save(comments);
    }

    @Override
    public Comments addCommentsByAtrByIdTourist(Long attractionId, Long touristId, Comments comments) {
        Attraction attraction=attractionRestClient.findAttractionById(attractionId);
        Tourist tourist=touristRestClient.getoneById(touristId);
        comments.setAttractionId(attraction.getId());
        comments.setTouristId(tourist.getId());
        return
                commentsRepository.save(comments);

    }




    @Override
    public List<Comments> FindCommentsByIdAttraction(Long id) {
        List<Comments> comments = commentsRepository.findByattractionId(id);
        if (comments.isEmpty()) {
            return Collections.emptyList();
        } else {
            comments.forEach(c -> {
                Comments com = commentsRepository.findById(c.getId()).orElseThrow(() ->new RuntimeException("id not found"));
                try {
                    c.setTourist(touristRestClient.getoneById(com.getTouristId()));
                }
                catch (FeignException.ServiceUnavailable e) {
                    c.setTourist(null);
                }
            });
            return comments;
        }
    }


    @Override
    public List<Comments> FindCommentsByIdTourist(Long id) {
        List<Comments> comments = commentsRepository.findByTouristId(id);
        if (comments.isEmpty()) {
            return Collections.emptyList();
        } else {
            return comments;
        }
    }



    @Override
    public List<Comments> FindAllComment() {
        // Récupérer la liste de tous les commentaires depuis le repository
        List<Comments> commentsList = commentsRepository.findAll();
        // Pour chaque commentaire dans la liste
        commentsList.forEach(c -> {
            // Vérifier si l'ID de l'attraction associée au commentaire n'est pas nul
            if (c.getAttractionId() != null || c.getTouristId() !=null) {
                try {
                    // Essayer de récupérer les détails de l'attraction associée en utilisant le client Feign
                    c.setAttraction(attractionRestClient.findAttractionById(c.getAttractionId()));
                    //  c.setTourist(touristRestClient.getoneById(c.getTouristId()));

                } catch (FeignException.NotFound e) {
                    // Si l'attraction n'est pas trouvée (erreur 404), définir l'attraction du commentaire comme nulle
                    c.setAttraction(null);
                    //   c.setTourist(null);

                }
                try {
                    // Essayer de récupérer les détails de l'attraction associée en utilisant le client Feign
                  c.setTourist(touristRestClient.getoneById(c.getTouristId()));
                    //  c.setTourist(touristRestClient.getoneById(c.getTouristId()));

               } catch (FeignException.NotFound e) {
                    // Si l'attraction n'est pas trouvée (erreur 404), définir l'attraction du commentaire comme nulle
                    c.setTourist(null);
                    //   c.setTourist(null);

               }
            } else {
                // Si l'ID de l'attraction est nul, définir l'attraction du commentaire comme nulle
                c.setAttraction(null);
                 c.setTourist(null);

            }

            Long attractionId = c.getAttractionId();

            // Si l'ID de l'attraction n'est pas nul
            if (attractionId != null) {
                // Calculer la moyenne des avis pour cette attraction
                double moy = commentsRepository.findByattractionId(attractionId)
                        .stream()
                        .mapToInt(Comments::getAvis)
                        .average()
                        .orElse(0.0);

                // Définir la moyenne calculée sur l'objet Comment correspondant
                c.setMoyenne(moy);
            }

        });

        return commentsList;
    }
    @Override
    public double calculerNoteMoyenne(Long attractionId) {
        return commentsRepository.findByattractionId(attractionId)
                .stream()
                .mapToInt(Comments::getAvis)
                .average()
                .orElse(0.0);
    }


    @Override
    public void deleteCommentByTouristId(Long touristId) {
        List<Comments> comments = commentsRepository.findByTouristId(touristId);
        for (Comments comment : comments) {
            commentsRepository.delete(comment);
        }

    }





}

