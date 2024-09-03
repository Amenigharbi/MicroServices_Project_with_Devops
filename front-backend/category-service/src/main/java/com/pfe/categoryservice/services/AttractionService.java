package com.pfe.categoryservice.services;


import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Reservation;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AttractionService {
    Attraction createAttraction(Attraction attraction);
  //  Attraction showAttractionByReservation(Long id, Attraction attraction);


    List<Attraction> FindAllAttraction();

    Attraction attractionById(Long id);

   Optional<Attraction> findByID(Long id );
    Attraction attractionUpdate(Long id , Attraction attraction);

    HashMap<String,String> deleteAttraction(Long id);
    Optional<Attraction> getAttractionByCategoryId(Long id);
    HashMap<String,String> deleteAttractionAndCommentsAndRes(Long id);


}
