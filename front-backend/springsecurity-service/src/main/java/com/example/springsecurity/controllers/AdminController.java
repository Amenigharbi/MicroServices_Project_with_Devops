package com.example.springsecurity.controllers;


import com.example.springsecurity.models.Admin;
import com.example.springsecurity.services.AdminService;
import com.example.springsecurity.utils.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Transactional
@RestController
@CrossOrigin("*")
@RequestMapping("/admins")

public class AdminController {

    final AdminService adminService;
    final StorageService storageService;
    public AdminController(AdminService adminService, StorageService storageService){
        this.adminService=adminService;
        this.storageService=storageService;
    }


    @PostMapping("/register")
    public ResponseEntity<Admin> create(@RequestParam("file") MultipartFile file, Admin admin) {
        String filename = storageService.store(file);
        admin.setImage(filename);
        Admin createAdmin = adminService.createAdmin(admin);
        return ResponseEntity.ok().body(createAdmin);
    }


    @GetMapping("/all")
    public List<Admin> AllAdmin() {
        return adminService.FindAllAdmin();
    }



    @GetMapping("/getone/{id}")
    public Admin getoneById(@PathVariable Long id) {
        return adminService.adminById(id);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Admin> UpdateAdmin(@PathVariable Long id, MultipartFile file, Admin admin){
        String filename = storageService.store(file);
        admin.setImage(filename);
        Admin updateAdmin = adminService.updateAdmin(id, admin);

        if(admin == null){
            return ResponseEntity.notFound().build();
        }
        admin.setId(id);
        admin.setImage(admin.getImage());
        admin.setUsername(admin.getUsername());
        admin.setUsername(admin.getUsername() == null ? updateAdmin.getUsername(): admin.getUsername());
        admin.setImage(admin.getImage() == null ? updateAdmin.getImage(): admin.getImage());

        List<String> filenames = new ArrayList<>();

        storageService.store(file);
        Admin admin1 = adminService.createAdmin(admin);


        return ResponseEntity.ok().body(updateAdmin);
    }


}
