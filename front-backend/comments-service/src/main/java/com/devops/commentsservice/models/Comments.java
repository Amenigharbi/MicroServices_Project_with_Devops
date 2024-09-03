package com.devops.commentsservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "comments")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Date date;
    private double moyenne;
    public void setDate() {
        date = new Date();  // Obtient la date et l'heure système
    }
    private int avis;

    @Transient //ne sera pas mapper dans le base
    private Attraction attraction;
    private Long attractionId;
    @Transient //ne sera pas mapper dans le base
    private Tourist tourist;
    private Long touristId;
    public void setAvis(int avis) {
        if (avis >= 1 && avis <= 5) {
            this.avis = avis;
        } else {
            throw new IllegalArgumentException("L'avis doit être un entier compris entre 1 et 5");
        }}


}
