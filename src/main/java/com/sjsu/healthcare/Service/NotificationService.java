package com.sjsu.healthcare.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.sjsu.healthcare.Model.Notification;
import com.sjsu.healthcare.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import javax.xml.ws.Response;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@RestController
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    //Save new notification
    @RequestMapping(value = "api/notification", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity notificationPost(@RequestBody Notification notification)
    {
        notification.setCreatedAt();
        //:TODO check if patientID exists in the patient table and then insert
        //:TODO check if CoCCID exists in the patient table for that patient and then insert
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
/*
    @RequestMapping(value = "api/notification/{id}/notificationsbetween ", method = RequestMethod.GET)
    public ResponseEntity getNotificationsBetween(@PathVariable("id") String patientID,
                                                  @RequestParam(value="fromTime",required=true) String fromTime,
                                                  @RequestParam(value="toTime",required=true) String toTime)
    {
        if(notificationRepository.findByPatientId(patientID) == null)
        {
            return new ResponseEntity("No Notifications found for this patient", HttpStatus.NOT_FOUND);
        }
        Date fromDate = null;
        Date toDate = null;
        System.out.println(fromTime);
        System.out.println(toTime);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try
        {
             fromDate=sdf.parse(fromTime);
             toDate=sdf.parse(toTime);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        BasicDBObject query = new BasicDBObject();
        query.put("createdAt", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());
//        DBCollection collection = null;
//        collection.find(query);
        System.out.println("toDate:"+toDate);
        ArrayList<Notification> nList = null;
        System.out.println("nList:"+nList);
        nList = notificationRepository.findByPatientIdAndCreatedAtBetween(patientID, fromDate, toDate);
        System.out.println("nList after:"+nList);
        if(nList == null)
        {
            return new ResponseEntity("No notifications found between the time period", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(nList, HttpStatus.OK);
    }
    */
}
