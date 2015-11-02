package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.Notification;
import com.sjsu.healthcare.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    //Save new notification
    @RequestMapping(value = "api/notification", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity notificationPost(@RequestBody Notification notification)
    {
        notification.setCreatedAt();
        notification.setNotificationSent(false);
        notificationRepository.save(notification);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get all notifications for a patient
    @RequestMapping(value = "api/patient/{id}/notifications", method = RequestMethod.GET)
    public ResponseEntity notificationGet(@PathVariable("id") String patientId) {
        ArrayList<Notification> notifications = notificationRepository.findByPatientId(patientId);
        return new ResponseEntity(notifications, HttpStatus.OK);
    }

    //update notificationFlag to true when notification is sent
    @RequestMapping(value = "api/notification/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity notificationSentUpdate(@PathVariable("id") String notificationId)
    {
        Notification notification;
        try {
             notification = notificationRepository.findById(notificationId);
        } catch (Exception e){
            return new ResponseEntity("Notification Id requested not found!", HttpStatus.NOT_FOUND);
        }
        notification.setNotificationSent(true);
        notificationRepository.save(notification);
        return new ResponseEntity(notification, HttpStatus.OK);

    }


}
