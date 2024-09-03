package com.devops.reservationservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;




@Getter @Setter  @NoArgsConstructor @AllArgsConstructor
public class Services {

    private Long id;
    private String content;
    private String logo;
    private String name;
    private float price;



}
