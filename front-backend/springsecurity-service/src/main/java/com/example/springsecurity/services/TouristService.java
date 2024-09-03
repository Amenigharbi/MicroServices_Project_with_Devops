package com.example.springsecurity.services;

import com.example.springsecurity.models.Tourist;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface TouristService {
    Tourist createTourist (Tourist tourist);
    Tourist updateTourist (Long id, Tourist tourist);
    String deleteTourist (Long id);
    List<Tourist> findAllTourist();
    Optional<Tourist> findById(Long id);
    Tourist findTouristById(Long id);
    //String deleteTourist_res_com(Long id);
    HashMap<String,String> deleteTouristAndRelatedData(Long id);
}
