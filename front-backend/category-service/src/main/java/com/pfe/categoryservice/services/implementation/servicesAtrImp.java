package com.pfe.categoryservice.services.implementation;

import com.pfe.categoryservice.models.Services;


import com.pfe.categoryservice.repository.ServicesRepository;
import com.pfe.categoryservice.services.ServicesAtrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class servicesAtrImp implements ServicesAtrService {


   @Autowired
    private ServicesRepository servicesRepository;

    @Override
    public List<Services> findAllService() {
        return servicesRepository.findAll();
    }

    @Override
    public Services createService(Services services) {
        return servicesRepository.save(services);
    }



    @Override
    public Services serviceById(Long id) {
        Optional<Services> optionalServices = servicesRepository.findById(id);
        return optionalServices.orElse(null);
    }

    @Override
    public Services serviceUpdate(Long id, Services services) {
        // Implémentez la logique pour mettre à jour le service avec l'ID spécifié
        return services;
    }

    @Override
    public String deleteService(Long id) {
        servicesRepository.deleteById(id);
        return "Service supprimé avec succès";
    }

    @Override
    public List<Services> FindServiceByIdAttraction(Long id) {
        List<Services> services = servicesRepository.findByattractionId(id);
        if (services.isEmpty()) {
            return Collections.emptyList();
        } else {
            return services;
        }
    }

}



