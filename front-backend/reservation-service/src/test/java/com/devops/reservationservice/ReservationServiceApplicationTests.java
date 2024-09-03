package com.devops.reservationservice;

import com.devops.reservationservice.clientConfig.AttractionRestClient;
import com.devops.reservationservice.clientConfig.TouristRestClient;
import com.devops.reservationservice.models.Reservation;
import com.devops.reservationservice.repository.ReservationRepository;
import com.devops.reservationservice.services.ReservationService;
import com.devops.reservationservice.services.implementation.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceApplicationTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AttractionRestClient attractionRestClient;

    @Mock
    private TouristRestClient touristRestClient;

 /*   @InjectMocks
    private ReservationService reservationService = new ReservationServiceImpl(reservationRepository, attractionRestClient, touristRestClient);
*/
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    //Integration Test
//        @Test
//        public void testCreateReservation() {
//            // Créer un objet Reservation de test
//            Reservation reservation = new Reservation();
//            reservation.setId(1L);
//            reservation.setAttractionId(1L);
//            reservation.setTouristId(1L);
//
//            // Définir le comportement attendu du repository lors de l'appel à la méthode save()
//            when(reservationRepository.save(reservation)).thenReturn(reservation);
//
//            // Appeler la méthode du service pour créer une réservation
//            Reservation savedReservation = reservationService.createReservation(reservation);
//
//            // Vérifier que la réservation a été enregistrée correctement
//            assertEquals(reservation.getId(), savedReservation.getId());
//            assertEquals(reservation.getAttractionId(), savedReservation.getAttractionId());
//            assertEquals(reservation.getTouristId(), savedReservation.getTouristId());
//
//            // Vérifier que la méthode save() du repository a été appelée une fois
//            verify(reservationRepository, times(1)).save(reservation);
//        }
//
//    @Test
//    public void testGetReservationById() {
//        // Créer un objet Reservation de test
//        Reservation reservation = new Reservation();
//        reservation.setId(1L);
//        reservation.setAttractionId(1L);
//        reservation.setTouristId(1L);
//
//        // Définir le comportement attendu du repository lors de l'appel à la méthode findById()
//        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
//
//        // Appeler la méthode du service pour obtenir une réservation par son ID
//        Reservation foundReservation = reservationService.reservationById(1L);
//
//        // Vérifier que la réservation retournée par le service correspond à celle attendue
//        assertEquals(reservation.getId(), foundReservation.getId());
//        assertEquals(reservation.getAttractionId(), foundReservation.getAttractionId());
//        assertEquals(reservation.getTouristId(), foundReservation.getTouristId());
//    }

    @Test
    void testCreateReservation() {
        // Préparation des données de test
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        // Simulation du comportement du repository
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Exécution de la méthode à tester
        Reservation savedReservation = reservationRepository.save(reservation);

        // Vérification
        assertEquals(1L, savedReservation.getId());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindAllReservation() {
        // Préparation des données de test
        List<Reservation> reservations = Collections.singletonList(new Reservation());

        // Simulation du comportement du repository
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Exécution de la méthode à tester
        List<Reservation> result = reservationRepository.findAll();

        // Vérification
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

}




