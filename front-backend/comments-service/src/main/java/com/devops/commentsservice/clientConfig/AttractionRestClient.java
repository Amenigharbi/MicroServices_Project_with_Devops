package com.devops.commentsservice.clientConfig;


import com.devops.commentsservice.models.Attraction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "CATEGORY-SERVICE")
public interface AttractionRestClient {
    @GetMapping("/attractions/getone/{id}")
    Attraction findAttractionById(@PathVariable Long id);

    @GetMapping("/attractions/all")
    List<Attraction> findAllAttraction();

    @DeleteMapping("/attractions/deleteARC/{attractionId}")
    public HashMap<String,String> deleteAttractionAndComments(@PathVariable Long attractionId);



}






