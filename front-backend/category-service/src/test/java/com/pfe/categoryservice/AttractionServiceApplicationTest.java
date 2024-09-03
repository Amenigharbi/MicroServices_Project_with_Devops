package com.pfe.categoryservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.pfe.categoryservice.clientConfig.CommentRestClient;
import com.pfe.categoryservice.clientConfig.ReservationRestClient;
import com.pfe.categoryservice.controllers.AttractionController;
import com.pfe.categoryservice.models.Attraction;
import com.pfe.categoryservice.models.Comments;
import com.pfe.categoryservice.models.Reservation;
import com.pfe.categoryservice.repository.AttractionRepository;
import com.pfe.categoryservice.services.AttractionService;
import com.pfe.categoryservice.services.implementation.AttractionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
public class AttractionServiceApplicationTest {

    @Mock
    private ReservationRestClient reservationRestClient;

    @Mock
    private CommentRestClient commentRestClient;
    @Mock
    private AttractionService attractionService;

    @Mock
    private AttractionController attractionController;
    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private Comments comments;
    @Mock
    private Reservation reservation;

    @InjectMocks
    private AttractionImpl attractionImpl;

    @Mock MockMultipartFile mockMultipartFile;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllAttraction() {
        // Créer une liste d'attractions simulée
        List<Attraction> attractions = new ArrayList<>();
        attractions.add(new Attraction(/* Ajoutez les détails de l'attraction ici */));

        // Définir le comportement simulé des clients Feign
        // Définir le comportement simulé des clients Feign
        when(reservationRestClient.showReservationByAttraction(anyLong())).thenReturn(new ArrayList<>());
        when(commentRestClient.showCommentByIdAttraction(anyLong())).thenReturn(new ArrayList<>());


        // Appeler la méthode à tester
        List<Attraction> result = attractionService.FindAllAttraction();

        // Vérifier le résultat
        assertNotNull(result);
        // Ajoutez d'autres assertions pour vérifier le résultat selon votre logique métier
    }
//    @Test
//    public void testFindAttractionById() {
//        // Créer une attraction simulée avec un ID spécifique
//        Long id = 1L;
//        Attraction simulatedAttraction = new Attraction();
//        simulatedAttraction.setId(id);
//
//        // Définir le comportement simulé des clients Feign pour cet ID d'attraction spécifique
//        when(reservationRestClient.showReservationByAttraction(id)).thenReturn(new ArrayList<>());
//        when(commentRestClient.showCommentByIdAttraction(id)).thenReturn(new ArrayList<>());
//
//        // Définir le comportement simulé de l'attractionService pour retourner l'attraction simulée
//        when(attractionRepository.findById(id)).thenReturn(simulatedAttraction); // Notez l'utilisation de Optional.of()
//
//        // Appeler la méthode à tester
//        Attraction result = attractionImpl.attractionById(id);
//
//        // Vérifier le résultat
//        assertNotNull(result);
//        assertEquals(id, result.getId()); // Vérifiez si l'ID de l'attraction retournée correspond à celui spécifié
//    }
@Test
    public void testAttractionById() {
        // Données de test
        Long attractionId = 1L;
        Attraction mockAttraction = new Attraction();
        mockAttraction.setId(attractionId);
        List<Reservation> mockReservations = Arrays.asList(new Reservation(), new Reservation());
        List<Comments> mockComments = Arrays.asList(new Comments(), new Comments());
        // Mock du comportement du repository
        when(attractionRepository.findById(attractionId)).thenReturn(Optional.of(mockAttraction));
        // Mock du comportement du client Feign pour les réservations
        when(reservationRestClient.showReservationByAttraction(attractionId)).thenReturn(mockReservations);
        // Mock du comportement du client Feign pour les commentaires
        when(commentRestClient.showCommentByIdAttraction(attractionId)).thenReturn(mockComments);
        // Exécution de la méthode à tester
        Attraction result = attractionService.attractionById(attractionId);
        // Vérification
        assertEquals(mockAttraction, result); // Vérifie que l'attraction retournée est celle attendue
        assertEquals(mockReservations, result.getReservation()); // Vérifie que les réservations sont correctement définies
        assertEquals(mockComments, result.getComments()); // Vérifie que les commentaires sont correctement définis
    }

    @Test
    public void testCreateAttraction() {
        // Créer une attraction simulée
        Attraction attraction = new Attraction();
        attraction.setLocation("Location");
        attraction.setName("Nom de l'attraction");
        attraction.setAvailable_Lang("Langue disponible");
        attraction.setPrice(10.0f);

        // Simuler le stockage des images
        List<MultipartFile> files = new ArrayList<>();
        // Ajouter des fichiers simulés à la liste
        files.add(new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "content1".getBytes()));
        files.add(new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "content2".getBytes()));

        // Définir le comportement simulé du service attraction lors de l'appel de la méthode savelist
        when(attractionService.createAttraction(any(Attraction.class))).thenReturn(attraction);

        // Appeler la méthode à tester pour créer l'attraction
        Attraction createdAttraction = attractionService.createAttraction(attraction);

        // Vérifier si l'attraction a été créée avec succès en comparant les propriétés
        assertEquals(attraction.getLocation(), createdAttraction.getLocation());
        assertEquals(attraction.getName(), createdAttraction.getName());
        assertEquals(attraction.getAvailable_Lang(), createdAttraction.getAvailable_Lang());
        assertEquals(attraction.getPrice(), createdAttraction.getPrice());
    }


    @Test
    public void testUpdateCategoryAttraction() {
        // Créer une attraction simulée avec les nouvelles valeurs
        Attraction attraction = new Attraction();
        attraction.setId(1L); // ID de l'attraction à mettre à jour
        attraction.setName("Nouveau nom de l'attraction");
        // Définir d'autres propriétés de l'attraction selon les besoins du test

        // Simuler le stockage des images
        MultipartFile[] files = new MultipartFile[2];
        // Ajouter des fichiers simulés à la liste
        files[0] = new MockMultipartFile("file", "filename1.jpg", "image/jpeg", "content1".getBytes());
        files[1] = new MockMultipartFile("file", "filename2.jpg", "image/jpeg", "content2".getBytes());

        // Définir le comportement simulé du service attraction lors de l'appel de la méthode attractionById
        when(attractionService.attractionById(any(Long.class))).thenReturn(attraction);

        // Appeler la méthode à tester pour mettre à jour l'attraction
        ResponseEntity<Attraction> updatedAttractionResponse = attractionController.updateCategoryAttraction(1L, 2L, files, attraction);

        // Vérifier si la réponse est null
        assertNotNull(updatedAttractionResponse);

        // Vérifier si la réponse n'est pas null avant de vérifier le code d'état
        if (updatedAttractionResponse != null) {
            // Vérifier si la réponse est un succès (code 200)
            assertEquals(HttpStatus.OK, updatedAttractionResponse.getStatusCode());

            // Vérifier si l'attraction mise à jour a les bonnes propriétés
            Attraction updatedAttraction = updatedAttractionResponse.getBody();
            assertNotNull(updatedAttraction);
            assertEquals(attraction.getId(), updatedAttraction.getId());
            assertEquals(attraction.getName(), updatedAttraction.getName());
            // Ajouter d'autres assertions pour vérifier d'autres propriétés selon votre logique métier
        }
    }



//    @Test
//    public void testGetCommentsByAttractionId() {
//        // Préparer les données de test
//        Long attractionId = 1L;
//        List<Comments> expectedComments = new ArrayList<>();
//        expectedComments.add(new Comments(1L, "Contenu du commentaire", new Date(), 5));
//        expectedComments.add(new Comments(2L, "Autre contenu du commentaire", new Date(), 4));
//
//        // Définir le comportement simulé de CommentRestClient pour retourner les commentaires attendus
//        when(commentRestClient.showCommentByIdAttraction(anyLong())).thenReturn(expectedComments);
//
//        // Appeler la méthode à tester pour récupérer les commentaires de l'attraction
//        List<Comments> actualComments = commentRestClient.showCommentByIdAttraction(attractionId);
//
//        // Vérifier si les commentaires retournés correspondent aux commentaires attendus
//        Assertions.assertEquals(expectedComments, actualComments);
//    }


    @Test
    public void testGetReservationsByAttractionId() {
        // Préparer les données de test
        Long attractionId = 1L;
        List<Reservation> expectedReservations = new ArrayList<>();
        expectedReservations.add(new Reservation(1L, "Nom du client", Reservation.Status.PENDING, false, null, new Date(), null, null, 2, 0, null));
        expectedReservations.add(new Reservation(2L, "Autre nom du client", Reservation.Status.PENDING, false, null, new Date(), null, null, 3, 0, null));

        // Définir le comportement simulé de ReservationRestClient pour retourner les réservations attendues
        when(reservationRestClient.showReservationByAttraction(anyLong())).thenReturn(expectedReservations);

        // Appeler la méthode à tester pour récupérer les réservations de l'attraction
        List<Reservation> actualReservations = reservationRestClient.showReservationByAttraction(attractionId);

        // Vérifier si les réservations retournées correspondent aux réservations attendues
        Assertions.assertEquals(expectedReservations, actualReservations);
    }


}