package com.pfe.categoryservice.controllers;

import com.pfe.categoryservice.clientConfig.CommentRestClient;
import com.pfe.categoryservice.clientConfig.ReservationRestClient;
import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Category;
import com.pfe.categoryservice.repository.AttractionRepository;
import com.pfe.categoryservice.repository.ServicesRepository;
import com.pfe.categoryservice.services.AttractionService;
import com.pfe.categoryservice.services.CategoryService;


import com.pfe.categoryservice.utils.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController

@RequestMapping("/attractions")

public class AttractionController {
    @Autowired
    private AttractionService attractionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private AttractionRepository attractionRepository;
    @Autowired
    CommentRestClient commentRestClient;
    @Autowired
    ReservationRestClient reservationRestClient;
    @Autowired
    private ServicesRepository servicesRepository;



    @PostMapping("/save")
    public ResponseEntity<Attraction> create(@RequestParam("file") MultipartFile file, Attraction attraction) {
        // Store the image
        String filename = storageService.store(file);
        // Assuming photo is a field in Attraction entity
        attraction.setPhoto(filename);
        Attraction createdAttraction = attractionService.createAttraction(attraction);
        return ResponseEntity.ok().body(createdAttraction);
        //return attractionService.createAttraction(attraction);
    }

    @PostMapping("/savelist")
    public ResponseEntity<Attraction> create(@RequestParam("files") MultipartFile[] files, Attraction attraction) {
        // Store the images
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = storageService.store(file);
            filenames.add(filename);
        }

        //Assuming photo is a field in Attraction entity
        attraction.setPhotos(filenames);

        Attraction createdAttraction = attractionService.createAttraction(attraction);
        return ResponseEntity.ok().body(createdAttraction);
    }
//@PostMapping("/savelist")
//public ResponseEntity<Attraction> create(@RequestParam("files") MultipartFile[] files, @RequestParam("services") MultipartFile[]  services, Attraction attraction) {
//    // Store the images
//    List<String> filenames = new ArrayList<>();
//    for (MultipartFile file : files) {
//        String filename = storageService.store(file);
//        filenames.add(filename);
//    }
//    List<String> servicenames = new ArrayList<>();
//    for (MultipartFile file : services) {
//        String servicename = storageService.store(file);
//        servicenames.add(servicename);
//    }
//
//    // Set the photos for the attraction
//    attraction.setPhotos(filenames);
//
//    // Set the services for the attraction
//    attraction.setServices(servicenames);
//
//    // Create the attraction
//    Attraction createdAttraction = attractionService.createAttraction(attraction);
//
//    // Return the response
//    return ResponseEntity.ok().body(createdAttraction);
//}



    @GetMapping("/all")
    public List<Attraction> allAtr() {
        return attractionService.FindAllAttraction();

    }


    @GetMapping("/getone/{id}")
    public Attraction getoneById(@PathVariable Long id) {

        return attractionService.attractionById(id);
    }

    @PutMapping("/update/{id}")
    public Attraction updateAttraction(@PathVariable Long id, Attraction attraction) {

        return attractionService.attractionUpdate(id, attraction);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String,String> deleteattraction(@PathVariable Long id) {

        return attractionService.deleteAttraction(id);
    }

    @PostMapping("/addAttrWithCategory/{cat_id}")
    public ResponseEntity<Attraction> addAttrWithCategory(@PathVariable Long cat_id, @RequestParam("files") MultipartFile[] files, Attraction attraction) {
        Category category = categoryService.categoryById(cat_id);
        if (category == null) {
            return ResponseEntity.notFound().build();

        }
        List<String> filenames = new ArrayList<>();
        for (MultipartFile file : files) {
            String filename = storageService.storee(file,300,500);
            filenames.add(filename);
        }

        //Assuming photo is a field in Attraction entity
        attraction.setPhotos(filenames);


        attraction.setCategory(category);
        Attraction savedAttraction = attractionService.createAttraction(attraction);
        return ResponseEntity.ok().body(savedAttraction);
    }

    @GetMapping("/attractionsByCategory/{cat_id}")
    public ResponseEntity<List<Attraction>> getAttractionsByCategory(@PathVariable Long cat_id) {
        Category category = categoryService.categoryById(cat_id);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<Attraction> attractionOptional = attractionService.getAttractionByCategoryId(cat_id);
        if (attractionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Attraction attraction = attractionOptional.get();
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(attraction);
        return ResponseEntity.ok(attractions);
    }


    @PutMapping("/updateCategory/{cat_id}/{id}")
    public ResponseEntity<Attraction> updateCategoryAttraction(@PathVariable Long id,  @PathVariable Long cat_id,@RequestParam(value = "files", required = false) MultipartFile[] files, Attraction attraction) {
        Category category = categoryService.categoryById(cat_id);
        Attraction attraction2 = attractionService.attractionById(id);

        if (category == null || attraction2 == null) {
            return ResponseEntity.notFound().build();
        }
        attraction.setId(id);    //pour modifier selon l'id donné dans l'url
        attraction.setCategory(category);
        // si on fait modifier un seul attribut les autre prennent l'ancient valeur :expression ternaire
        attraction.setName(attraction.getName() == null ? attraction2.getName() : attraction.getName());
        attraction.setAvailable_Lang(attraction.getAvailable_Lang() == null ? attraction2.getAvailable_Lang() : attraction.getName());
        attraction.setPrice(attraction.getPrice() == 0 ? attraction2.getPrice() : attraction.getPrice());
        attraction.setLocation(attraction.getLocation() == null ? attraction2.getLocation() : attraction.getLocation());
        attraction.setPhoto(attraction.getPhoto() == null ? attraction2.getPhoto() : attraction.getPhoto());
        attraction.setOpening_hour(attraction.getOpening_hour() == null ? attraction2.getOpening_hour() : attraction.getOpening_hour());
        attraction.setClosing_hour(attraction.getClosing_hour() == null ? attraction2.getClosing_hour() : attraction.getClosing_hour());
        attraction.setVisitor_numbers(attraction.getVisitor_numbers() == 0 ? attraction2.getVisitor_numbers() : attraction.getVisitor_numbers());

        List<String> filenames = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                String filename = storageService.store(file);
                filenames.add(filename);
            }
        }else {
            filenames = attraction2.getPhotos(); // Utilisez les anciens noms de fichiers s'il n'y a pas de nouveaux téléchargements
        }
        attraction.setPhotos(filenames);
        Attraction attractionUpdate = attractionService.createAttraction(attraction);
        return ResponseEntity.ok().body(attractionUpdate);

    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> extensionToContentType = new HashMap<>();
        extensionToContentType.put("pdf", "application/pdf");
        extensionToContentType.put("jpg", "image/jpeg");
        extensionToContentType.put("jpeg", "image/jpeg");
        extensionToContentType.put("png", "image/png");
        // Obtenez l'extension du fichier à partir du nom de fichier
        String fileExtension = FilenameUtils.getExtension(filename);
        // Obtenez le type de contenu à partir de la correspondance
        String contentType = extensionToContentType.getOrDefault(fileExtension.toLowerCase(),
                MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // Définissez le type de contenu dans les en-têtes de réponse
        headers.setContentType(MediaType.parseMediaType(contentType));
        return ResponseEntity.ok().headers(headers).body(file);
    }

    @DeleteMapping("/deleteARC/{attractionId}")
    public HashMap<String, String> deleteAttractionAndCommentsAndRes(@PathVariable Long attractionId) {
        return attractionService.deleteAttractionAndCommentsAndRes(attractionId);
    }



    //@Override
//public HashMap<String, String> deleteAttraction_res_com(Long id) {
//    Attraction attraction = this.attractionById(id);
//    HashMap<String, String> message = new HashMap<>();
//
//    if (attraction != null) {
//        try {
//            // Supprimer les commentaires associés à l'attraction
//            commentRestClient.deleteCommentsByAttractionId(id);
//
//            // Supprimer les réservations associées à l'attraction
//            reservationServiceClient.deleteReservationsByAttractionId(id);
//
//            // Supprimer l'attraction elle-même
//            attractionDao.deleteById(id);
//
//            message.put("etat", "Attraction, comments, and reservations deleted");
//        } catch (Exception e) {
//            message.put("etat", "Error: " + e.getMessage());
//        }
//    } else {
//        message.put("etat", "Attraction not found");
//    }
//    return message;
//}
}


