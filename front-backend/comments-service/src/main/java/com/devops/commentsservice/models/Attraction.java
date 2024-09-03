package com.devops.commentsservice.models;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Attraction {
    private Long id;
    private String location;
    private String name;
    private String available_Lang;
    private float price;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime opening_hour;
    private LocalTime closing_hour;
    private String description;
    private int visitor_numbers ;
    private String photo;

    @ElementCollection
    private List<String> photos;

}

