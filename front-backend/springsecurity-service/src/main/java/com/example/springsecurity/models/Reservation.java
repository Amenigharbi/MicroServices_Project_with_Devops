package com.example.springsecurity.models;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Reservation {
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
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



    public Reservation() {
        //par defaut pending
        this.status = Status.PENDING;
    }
    public void changeStatusToRefused() {
        this.status = Status.REFUSED;
    }

    public void changeStatusToApproved() {
        this.status = Status.APPROVED;
    }

    @Transient
    List<Reservation> reservationList;
}


