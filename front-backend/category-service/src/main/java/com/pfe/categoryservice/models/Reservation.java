package com.pfe.categoryservice.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class Reservation {
    private Long id;
    private String name;

    private Status status;
    private boolean payment = false;
    private String description;
    private Date date_res;
    private Date date_debut;
    private Date date_fin;
    private float price;
    private float addition;
    public enum Status {
        APPROVED,
        PENDING,
        REFUSED
    }

    private Long touristId;

}
