package com.pfe.categoryservice.models;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Comments {
    private Long id;
    private String content;
    private Date date;
    private int avis;
    @Transient //ne sera pas mapper dans le base
    private Tourist tourist;
    private Long touristId;

}

