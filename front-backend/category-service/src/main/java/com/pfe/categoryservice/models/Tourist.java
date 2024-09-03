package com.pfe.categoryservice.models;

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
    private String username;
    private String addresse;
    private int passport_num;
    private String image;


}
