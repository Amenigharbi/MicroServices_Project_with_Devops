package com.pfe.categoryservice.clientConfig;

import com.pfe.categoryservice.models.Comments;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "COMMENT-SERVICE")

public interface CommentRestClient {
    @GetMapping("/comments/getone/{id}")
    Comments findCommentsById(@PathVariable Long id);

    @GetMapping("comments/all")
    List<Comments> findAllComments();

    @GetMapping("/comments/allCom/{id_attraction}")
    @CircuitBreaker(name = "comment-S",fallbackMethod = "getDefaultComments")
    List<Comments> showCommentByIdAttraction(@PathVariable Long id_attraction);


    @PostMapping("/ajout/{id_attraction}")
    public Comments add(@RequestBody Comments comments, @PathVariable Long id_attraction);

    @DeleteMapping("/comments/deleteCommentByAtr/{attractionId}")
    public HashMap<String, String> deleteCommentsByAttractionId(@PathVariable Long attractionId);
    @DeleteMapping("comments/delete/{id}")
    public HashMap<String, String> deleteComment(@PathVariable Long id);

    default List<Comments> getDefaultComments(Long id, Exception exception) {
        Comments c = new Comments();
        c.setId(id);
        c.setAvis(0);
        c.setContent("Not Available");

        return List.of(c);
    }

//    default Subcateorydto getDefaultsubcategory(Long id, Exception exception){
//        Subcateorydto subcateorydto=new Subcateorydto();
//        subcateorydto.setId(id);
//        subcateorydto.setName("Not Vailable");
//        subcateorydto.setDescription("Not Vailable");
//
//        return subcateorydto;
//    }




//    default List<Subcateorydto> getAllCategorys(Exception exception){
//        return List.of();
//    }












}
