package com.devops.reservationservice.services.implementation;



import com.devops.reservationservice.clientConfig.AttractionRestClient;
import com.devops.reservationservice.clientConfig.TouristRestClient;
import com.devops.reservationservice.models.Notification;
import com.devops.reservationservice.repository.NotificationDAO;
import com.devops.reservationservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
   @Autowired
   private NotificationDAO notificationDAO;

   @Autowired
   private AttractionRestClient attractionRestClient;
@Autowired
private TouristRestClient touristRestClient;
/*    @Override
    public Notification sendNotification(String msg,Long id) {
        Notification notification = new Notification();
        notification.setMessage(msg);
        notification.setTouristId(id);
        return notificationDAO.save(notification);

    }*/


    @Override
    public Notification sendNotification(String msg, Long touristId,Long idattr) {
        // Créer la notification avec le message et l'ID du touriste
        Notification notification = new Notification(msg);
        notification.setTouristId(touristId);
        notification.setAttractionId(idattr);

        // Enregistrer la notification en base de données
        notificationDAO.save(notification);

        // Envoyer la notification via WebSocket
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return notification; // Retourner la notification enregistrée
    }


    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notifications= notificationDAO.findAll();
          for( Notification n :notifications){
              n.setTourist(touristRestClient.getoneById(n.getTouristId()));
              n.setAttraction(attractionRestClient.findAttractionById(n.getAttractionId()));
          }
          return notifications;
    }








    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotificationn(String message) {
        Notification notification = new Notification(message);
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}