package com.pfe.categoryservice;

import com.pfe.categoryservice.controllers.AttractionController;
import com.pfe.categoryservice.models.Attraction;

import com.pfe.categoryservice.repository.AttractionRepository;

import com.pfe.categoryservice.services.AttractionService;


import com.pfe.categoryservice.utils.StorageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TestUnitaireAttraction {

    @Mock
    private AttractionService attractionService;
    @Mock
    private AttractionRepository attractionRepository;
    @Mock
    private StorageService storageService;

    @InjectMocks
    private AttractionController attractionController;
    //Test Attraction Controller
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialiser mocks
    }


    @Test
    public void testCreateAttraction() {
        MockMultipartFile file = new MockMultipartFile("file", "filename.png", "image/png", "some image".getBytes()); // Fichier fictif
        Attraction attraction = new Attraction();
        attraction.setName("Test Attraction"); // Définir nom

        when(storageService.store(file)).thenReturn("storedFilename.png"); // Simuler stockage
        when(attractionService.createAttraction(any(Attraction.class))).thenReturn(attraction); // Simuler création

        ResponseEntity<Attraction> response = attractionController.create(file, attraction); // Appeler méthode

        assertEquals(200, response.getStatusCodeValue()); // Vérifier statut
        assertEquals(attraction, response.getBody()); // Vérifier réponse
        verify(storageService, times(1)).store(file); // Vérifier stockage
        verify(attractionService, times(1)).createAttraction(any(Attraction.class)); // Vérifier création
    }

    @Test
    public void testGetAllAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction()); // Ajouter attraction
        when(attractionService.FindAllAttraction()).thenReturn(attractions); // Simuler recherche

        List<Attraction> response = attractionController.allAtr(); // Appeler méthode

        assertEquals(1, response.size()); // Vérifier taille
        verify(attractionService, times(1)).FindAllAttraction(); // Vérifier recherche
    }



    @Test
    public void testGetAttractionById() {
        Attraction attraction = new Attraction();
        when(attractionService.attractionById(1L)).thenReturn(attraction); // Simuler recherche

        Attraction response = attractionController.getoneById(1L); // Appeler méthode

        assertEquals(attraction, response); // Vérifier réponse
        verify(attractionService, times(1)).attractionById(1L); // Vérifier recherche
    }

    @Test
    public void testUpdateAttraction() {
        Attraction attraction = new Attraction();
        when(attractionService.attractionUpdate(1L, attraction)).thenReturn(attraction); // Simuler mise à jour

        Attraction response = attractionController.updateAttraction(1L, attraction); // Appeler méthode

        assertEquals(attraction, response); // Vérifier réponse
        verify(attractionService, times(1)).attractionUpdate(1L, attraction); // Vérifier mise à jour
    }

    @Test
    public void testDeleteAttraction() {
        HashMap<String, String> message = new HashMap<>();
        message.put("etat", "attraction deleted"); // Ajouter message
        when(attractionService.deleteAttraction(1L)).thenReturn(message); // Simuler suppression

        HashMap<String, String> response = attractionController.deleteattraction(1L); // Appeler méthode

        assertEquals("attraction deleted", response.get("etat")); // Vérifier message
        verify(attractionService, times(1)).deleteAttraction(1L); // Vérifier suppression
    }


}
