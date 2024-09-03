package com.devops.commentsservice.controllers;


import com.devops.commentsservice.models.Comments;
import com.devops.commentsservice.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@RestController

@RequestMapping("/comments")
public class Controller {

   final CommentService commentService;

    public Controller(CommentService commentService){
        this.commentService=commentService;

    }

    @PostMapping("/save")
    public Comments addComment(@RequestBody Comments comments) {

        return commentService.addComment(comments);
    }


    @GetMapping("/all")
    public List<Comments> allcomment() {
        return commentService.FindAllComment();
    }


    @GetMapping("/getone/{id}")
    public Comments getoneById(@PathVariable Long id) {
        return commentService.CommentById(id);
    }



    @PutMapping("/update/{id}")
    public Comments updateComment(@PathVariable Long id, @RequestBody Comments comments) {
        return commentService.updateComment(id, comments);
    }



    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteComment(@PathVariable Long id) {
        Comments co = commentService.CommentById(id);
        HashMap message = new HashMap();
        if (co != null) {
            try {
                commentService.deleteComment(id);
                message.put("etat", "comment deleted");
                return message;
            } catch (Exception e) {
                message.put("etat", "Error" + e.getMessage());
                return message;
            }
        } else {
            message.put("etat", "comment not found");
            return message;


        }

    }



    //openfeign ==> attraction

    @PostMapping("/ajout/{id_attraction}")
    public Comments add(@RequestBody Comments comments, @PathVariable Long id_attraction) {
        if (comments.getDate() == null) {
            comments.setDate();  // Initialise la date si elle est null
        }
        return commentService.addCommentByAttraction(id_attraction, comments);
    }


    @GetMapping("/allCom/{id_attraction}")
    public List<Comments> getCommentsByIdAttraction(@PathVariable Long id_attraction) {
        return commentService.FindCommentsByIdAttraction(id_attraction);
    }



    @GetMapping("/moyenne/{attractionId}")
    public ResponseEntity<Double> calculerNoteMoyenne(@PathVariable Long attractionId) {
        double noteMoyenne = commentService.calculerNoteMoyenne(attractionId);
        return ResponseEntity.ok().body(noteMoyenne);
    }

    @DeleteMapping("/deleteCommentByAtr/{attractionId}")
    public HashMap<String, String> deleteCommentsByAttractionId(@PathVariable Long attractionId) {
        List<Comments> comments = commentService.FindCommentsByIdAttraction(attractionId);
        HashMap<String, String> message = new HashMap<>();

        if (!comments.isEmpty()) {
            try {
                // Supprimer chaque commentaire associé à l'attraction
                for (Comments comment : comments) {
                    commentService.deleteComment(comment.getId());
                }

                message.put("etat", "Tous les commentaires de l'attraction ont été supprimés avec succès.");
            } catch (Exception e) {
                message.put("etat", "Une erreur s'est produite lors de la suppression des commentaires : " + e.getMessage());
            }
        } else {
            message.put("etat", "Aucun commentaire trouvé pour l'attraction spécifiée.");
        }

        return message;
    }


    //openFeign ==> tourist
    @GetMapping("/touristCom/{id_tourist}")
    public List<Comments> getCommentsByIdTourist(@PathVariable Long id_tourist){
        return commentService.FindCommentsByIdTourist(id_tourist);
    }

    @PostMapping("/commenter/{attractionId}/{touristId}")
    public Comments addCommentByIds(@PathVariable Long attractionId,
                                    @PathVariable Long touristId ,
                                    @RequestBody   Comments comments){
        if (comments.getDate() == null) {
            comments.setDate();  // Initialise la date si elle est null
        }
        return commentService.addCommentsByAtrByIdTourist(attractionId,touristId,comments);
    }


    @DeleteMapping("/deleteByTourist/{touristId}")
    public HashMap<String, String> deleteCommentByTouristId(@PathVariable Long touristId) {
        List<Comments> comments = commentService.FindCommentsByIdTourist(touristId);
        HashMap<String, String> message = new HashMap<>();

        if (!comments.isEmpty()) {
            try {
                // Supprimer chaque reservation associé à l'attraction
                for (Comments comment : comments) {
                    commentService.deleteComment(comment.getId());
                }

                message.put("etat", "Tous les commentaires de la part de tourist ont été supprimés avec succès.");
            } catch (Exception e) {
                message.put("etat", "Une erreur s'est produite lors de la suppression des commentaires : " + e.getMessage());
            }
        } else {
            message.put("etat", "Aucun commentaire trouvé de la part de tourist spécifiée.");
        }

        return message;
    }



}
