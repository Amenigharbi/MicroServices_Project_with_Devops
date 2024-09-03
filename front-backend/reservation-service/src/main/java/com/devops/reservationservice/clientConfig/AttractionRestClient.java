package com.devops.reservationservice.clientConfig;

import com.devops.reservationservice.models.Attraction;
import com.devops.reservationservice.models.Reservation;
import com.devops.reservationservice.models.Services;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CATEGORY-SERVICE")
public interface AttractionRestClient {
    @GetMapping("/attractions/getone/{id}")
    Attraction findAttractionById(@PathVariable Long id);

    @GetMapping("/attractions/all")
    List<Attraction> findAllAttraction();

    @GetMapping("/services/{id}")
    Services findserviceById(@PathVariable Long id);
    }





