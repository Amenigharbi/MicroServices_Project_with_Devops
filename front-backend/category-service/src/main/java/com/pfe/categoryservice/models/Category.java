package com.pfe.categoryservice.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table (name = "categories")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Category {
    //declaration de id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;


    //liason entre table category et table attraction
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    //@JsonIgnore pour eviter la redondance des donné et blockage
    @JsonIgnoreProperties("category")

    private List<Attraction> attractions ; //Relation

    //NoArgs constructor
//    public Category() {
//    }

    //Args constructor
//    public Category(Long id, String name, String description) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//    }


    //    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}


