package com.pfe.categoryservice.controllers;


import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Services;
import com.pfe.categoryservice.repository.ServicesRepository;
import com.pfe.categoryservice.services.AttractionService;
import com.pfe.categoryservice.services.CategoryService;


import com.pfe.categoryservice.services.ServicesAtrService;
import com.pfe.categoryservice.utils.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/services")
public class ServicesController {
@Autowired ServicesAtrService servicesAtrService;
    @Autowired
    private ServicesRepository servicesRepository;
   @Autowired
    private AttractionService attractionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StorageService storageService;

   @PostMapping("/save")
    public ResponseEntity<Services> create(@RequestParam("file") MultipartFile file, Services services) {
       // Store the image
       String filename = storageService.store(file);
       // Assuming photo is a field in Attraction entity
       services.setLogo(filename);
       Services createdServices = servicesAtrService.createService(services);
       return ResponseEntity.ok().body(createdServices);
   }

@PostMapping("/SvcByAtr/{attraction_id}")
public ResponseEntity<Services> addSvcWithIdAttraction(@PathVariable Long attraction_id,@RequestParam (value = "files",
        required = false) MultipartFile[] files ,@RequestParam("file") MultipartFile file ,Services services) {

    Attraction attraction = attractionService.attractionById(attraction_id);
    if (attraction == null) {
        return ResponseEntity.notFound().build();
    }
    List<String> filnames = new ArrayList<>();
    if(files != null){
    for (MultipartFile x : files) {
        String filename = storageService.store(x);
        filnames.add(filename);

    }}
    services.setPhotos(filnames);

    String logoFilename = storageService.store(file);

    services.setLogo(logoFilename);

    services.setAttraction(attraction);
    Services savedServices = servicesAtrService.createService(services);
    return ResponseEntity.ok().body(savedServices);


}


@GetMapping("/svc/{attraction_id}")
public List<Services> getServicesByIdAttraction(@PathVariable Long attraction_id) {
    return servicesAtrService.FindServiceByIdAttraction(attraction_id);
}

    @GetMapping("/{id}")
    public Services getServicesById(@PathVariable Long id) {
        return servicesAtrService.serviceById(id);
    }
    }

