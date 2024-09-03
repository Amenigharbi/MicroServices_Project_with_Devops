package com.pfe.categoryservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "services")
@Getter @Setter  @NoArgsConstructor
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String logo;
    private String name;
    private float price;
    @ElementCollection
    private List<String> photos;



    @ManyToOne
    @JoinColumn(name="attraction_id")
    @JsonIgnoreProperties("services")
    private Attraction attraction;


    public void setLogo(String logo) {
        this.logo = logo;
    }
}
