package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;
import java.util.Date;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    ArrayList<Notification> findByPatientId(String patientId);
    //ArrayList<Notification> findByPatientIdAndCreatedAtBetween(String patientId, Date d1, Date d2);
//    @Query("{'createdAt' : {$gte : ?1, $lte : ?2}}")
//    ArrayList<Notification> findByPatientIdAndCreatedAtBetween(String patientId,Date d1, Date d2);
    //ArrayList<Notification> findByCreatedAtAfter(Date d);
    //Notification findByCreatedAt(Date createdAt);
    Notification findById(String id);
}
