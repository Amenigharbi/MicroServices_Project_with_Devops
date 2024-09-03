package com.devops.reservationservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tourist {
    private Long id;
    private String addresse;
    private String email;
    private String username;
    private int passport_num;
    private String image;


}

