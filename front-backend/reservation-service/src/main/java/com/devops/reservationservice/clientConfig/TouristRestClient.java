package com.devops.reservationservice.clientConfig;

import com.devops.reservationservice.models.Tourist;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "SECURITY-SERVICE")
public interface TouristRestClient {
    @GetMapping("/tourists/getonet/{id}")
    Tourist getoneById(@PathVariable Long id);

    @GetMapping("/tourists/all")
    List<Tourist> AllTourist();

}
