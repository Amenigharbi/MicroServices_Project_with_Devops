package com.example.springsecurity.services;


import com.example.springsecurity.models.Admin;

import java.util.List;
import java.util.Optional;

public interface AdminService  {
    Admin createAdmin (Admin admin);
    Admin updateAdmin (Long id , Admin admin);

    List<Admin> FindAllAdmin();
    Admin adminById(Long id);
    String deleteAdmin (Long id);
    Optional<Admin> findById(Long id );

}
