package com.devops.commentsservice.service;

import com.devops.commentsservice.models.Comments;


import java.util.List;

public interface CommentService {

   Comments addComment (Comments comments);
    List<Comments> FindAllComment() ;

    Comments CommentById(Long id);
     Comments updateComment(Long id, Comments comments);

     void deleteComment(Long id);
    List<Comments> FindCommentsByIdAttraction(Long id);
    Comments addCommentByAttraction(Long id,Comments comments);
     double calculerNoteMoyenne(Long Id);
 Comments addCommentsByAtrByIdTourist(Long attractionId,Long touristId ,Comments comments);
 List<Comments> FindCommentsByIdTourist(Long id);
void deleteCommentByTouristId(Long touristId);
// String deletAllComments();

}
