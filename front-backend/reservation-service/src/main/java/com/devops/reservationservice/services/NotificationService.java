package com.devops.reservationservice.services;

import com.devops.reservationservice.models.Notification;

import java.util.List;

public interface NotificationService {

    Notification sendNotification(String msg,Long id,Long idattr);
    List<Notification> getAllNotifications();
}
