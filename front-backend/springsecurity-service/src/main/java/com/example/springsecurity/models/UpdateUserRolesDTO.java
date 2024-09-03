package com.example.springsecurity.models;

// UpdateUserRolesDTO.java
import com.example.springsecurity.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UpdateUserRolesDTO {


    private Set<ERole> roles;

    // Getters and setters
    public Set<ERole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }
}
