package com.example.springsecurity.models;


import com.google.j2objc.annotations.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Entity
@Table(name = "tourists")
@Getter @Setter  @NoArgsConstructor
public class Tourist extends User {

    private String addresse;
    @Column(nullable = true)
    private int passport_num;

    public Tourist(String username, String email, String phoneNumber, String password, String addresse) {
        super(username, email, phoneNumber, password);
        this.addresse = addresse;
    }

    @Transient
    private List<Comments> comments;
    @Transient
    private List<Reservation> reservation;


    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public int getPassport_num() {
        return passport_num;
    }

    public void setPassport_num(int passport_num) {
        this.passport_num = passport_num;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
    }
}

