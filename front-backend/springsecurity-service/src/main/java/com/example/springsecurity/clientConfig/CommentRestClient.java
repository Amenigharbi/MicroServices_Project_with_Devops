package com.example.springsecurity.clientConfig;

import com.example.springsecurity.models.Comments;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentRestClient {

    @GetMapping("/comments/all")
    List<Comments> allcomment();
    @GetMapping("/comments/getone/{id}")
    Comments getoneById(@PathVariable Long id);
    @PutMapping("/comments/update/{id}")
    Comments updateComment(@PathVariable Long id, @RequestBody Comments comments);
    @DeleteMapping("/comments/delete/{id}")
    HashMap<String, String> deletComment(@PathVariable Long id);
    @PostMapping("/comments/commenter/{id_attraction}/{id_tourist}")
    Comments comment(@RequestBody Comments comments, @PathVariable Long id_attraction, @PathVariable Long id_tourist);

    @GetMapping("/comments/touristCom/{id_tourist}")
    List<Comments> getCommentsByIdTourist(@PathVariable Long id_tourist);

    @DeleteMapping("/comments/deleteByTourist/{touristId}")
    public HashMap<String, String> deleteCommentByTouristId(@PathVariable Long touristId);




}

