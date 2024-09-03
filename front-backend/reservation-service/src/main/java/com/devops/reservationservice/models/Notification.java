package com.devops.reservationservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Notifications")
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    @Transient // ne sera pas mappé dans la base
    private Tourist tourist;
    private Long touristId;
    private  boolean readnotif = false;

    @Transient //ne sera pas mapper dans le base
    private Attraction attraction;
    private Long attractionId;
    private LocalDateTime createdAt; // Date et heure de création

    public Notification(String message) {
        this.message = message;
    }

    // Méthode pour initialiser createdAt avec la date et l'heure actuelles
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Autres getters et setters
}
