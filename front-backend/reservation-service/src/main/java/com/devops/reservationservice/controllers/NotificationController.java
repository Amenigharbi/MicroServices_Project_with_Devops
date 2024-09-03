package com.devops.reservationservice.controllers;

import com.devops.reservationservice.models.MessageResponse;
import com.devops.reservationservice.models.Notification;
import com.devops.reservationservice.repository.NotificationDAO;
import com.devops.reservationservice.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationDAO notificationDAO;
    @MessageMapping("/notify")
    @SendTo("/topic/notifications")
    public Notification send(Notification notification) {
        return notification;
    }



    @GetMapping("/allnot")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }


    @GetMapping("/confirm/{id}")
    public ResponseEntity<?> upnotif(@PathVariable Long id) {
        try {
            Notification notification = notificationDAO.findById(id).orElse(null);
            if (notification != null) {
                notification.setReadnotif(true);
                notificationDAO.save(notification);
                return ResponseEntity.ok(new MessageResponse("Notification updated successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error updating notification"));
        }
    }
}
