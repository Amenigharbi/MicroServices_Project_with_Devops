package com.pfe.categoryservice.services;

import com.pfe.categoryservice.models.Services;

import java.util.List;

public interface ServicesAtrService {
    Services createService (Services service);
    List<Services> findAllService();
    Services serviceById(Long id);
    Services serviceUpdate(Long id , Services service);

    String deleteService(Long id);
    List<Services>  FindServiceByIdAttraction(Long id);

}
