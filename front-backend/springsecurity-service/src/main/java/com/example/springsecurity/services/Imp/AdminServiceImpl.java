package com.example.springsecurity.services.Imp;


import com.example.springsecurity.models.Admin;
import com.example.springsecurity.repository.AdminRepository;
import com.example.springsecurity.services.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {


    final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository){
        this.adminRepository=adminRepository;

    }

    @Override
    public Admin createAdmin(Admin admin) {

        return  adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        Admin admin1 = adminById(id);
        if (admin1 != null) {
            admin.setId(id);
            return adminRepository.save(admin);
        } else
        {
            throw new RuntimeException( "error");
        }
    }

    @Override
    public List<Admin> FindAllAdmin() {
        return adminRepository.findAll();
    }

    @Override
    public Admin adminById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(()->
                new RuntimeException("id not found"));

        return admin;
    }

    @Override
    public String deleteAdmin(Long id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isPresent()) {
            adminRepository.deleteById(id);
            return "Admin deleted ";
        } else {
            return "Admin not found ";
        }

    }

    @Override
    public Optional<Admin> findById(Long id) {
        return Optional.empty();
    }
}



