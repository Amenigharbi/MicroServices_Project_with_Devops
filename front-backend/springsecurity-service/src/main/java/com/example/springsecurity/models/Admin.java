package com.example.springsecurity.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter

@NoArgsConstructor
public class Admin extends User {

    public Admin(String username, String email, String phoneNumber, String password) {
        super(username, email, phoneNumber, password);
    }
}
