package com.example.springsecurity.models;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor


public class Comments {
    private Long id;
    private String content;
    private Date date;
    public void setDate() {
        date = new Date();  // Obtient la date et l'heure système
    }
    private int avis;

    public void setAvis(int avis) {
        if (avis >= 1 && avis <= 5) {
            this.avis = avis;
        } else {
            throw new IllegalArgumentException("L'avis doit être un entier compris entre 1 et 5");
        }}

    @Transient
    private List<Comments> commentsList;


}


