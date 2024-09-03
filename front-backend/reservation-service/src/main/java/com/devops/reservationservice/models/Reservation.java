package com.devops.reservationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reservations")
@Getter @Setter @AllArgsConstructor


public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
   @Enumerated(EnumType.STRING)
    private Status status;
    private boolean payment = false;
    private String description;
    private Date date_res;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date_debut;
    private Date date_fin;
    private float price;
    private float addition;
    public enum Status {
        APPROVED,
        PENDING,
        REFUSED
    }
    @Transient //ne sera pas mapper dans le base
    private Attraction attraction;
    private Long attractionId;

    @Transient //ne sera pas mapper dans le base
    private Tourist tourist;
    private Long touristId;


    public Reservation() {
        //par defaut pending
        this.status = Status.PENDING;

    }
    public void changeStatusToRefused(){this.status = Status.REFUSED;}
    public void changeStatusToApproved() {
        this.status = Status.APPROVED;
    }



    @ElementCollection
    private List<Long> services = new ArrayList<>();

    @Transient //ne sera pas mapper dans le base
    private List<Services> serviceslist;

}
