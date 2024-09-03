package com.pfe.categoryservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;


@Entity
@Table(name = "attractions")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     protected String location;
    private String name;
    private String available_Lang;
    private float price;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime opening_hour;
    private LocalTime closing_hour;
    private String description;
    private int visitor_numbers ;
    private String photo;

    //pour collecter les photos d'une attraction donné par id dans une galerie (liste  des photos)
   //@PostMapping("/savelist") dans le controller necessite @ElementCollection
    @ElementCollection
    private List<String> photos;

    @ManyToOne()
    @JoinColumn(name = "cat_id")
    //pour eviter le conflis des donnee et empecher un boucle ouvert
    @JsonIgnoreProperties("attractions")
    //category identifié dans mappedby() dans le model Category
    private Category category;




    //Relation entre attraction et service
    @OneToMany(mappedBy = "attraction",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("attraction")
    private List<Services> Services ;


//Relation entre les microservices category-service et reservations-service
    @Transient
    private List<Reservation> reservation;
    @Transient
    private List<Comments> comments;





}
