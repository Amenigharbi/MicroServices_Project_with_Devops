package com.devops.commentsservice.clientConfig;

import com.devops.commentsservice.models.Tourist;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SECURITY-SERVICE")
public interface TouristRestClient {
    @GetMapping("/tourists/getonet/{id}")
    public Tourist getoneById(@PathVariable Long id);

    @GetMapping("/tourists/all")
    public List<Tourist> AllTourist();


}
