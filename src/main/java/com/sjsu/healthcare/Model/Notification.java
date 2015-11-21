package com.sjsu.healthcare.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class Notification {

    private String id;
    private String patientId;
    private String circleOfCareContactId;
    private boolean notificationSent;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdAt;
    private String message;


    //added enum for 3 types of notification
//    public enum NotificationType{
//        HEARTRATE, HEARTDISEASE
//    }
    public String notificationType;
    public CircleOfCareContact circleOfCareContact;
    public List<CircleOfCareContact> circleOfCare;


    public Notification() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCircleOfCareContactId() {
        return circleOfCareContactId;
    }

    public void setCircleOfCareContactId(String circleOfCareContactId) {
        this.circleOfCareContactId = circleOfCareContactId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = java.util.Calendar.getInstance().getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotificationSent(boolean val)
    {
        this.notificationSent = val;
    }

    public boolean getNotificationSent()
    {
        return notificationSent;
    }

    public CircleOfCareContact getCircleOfCare() {
        return circleOfCareContact;
    }

    public void setCircleOfCare(CircleOfCareContact circleOfCare) {
        this.circleOfCareContact = circleOfCare;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public List<CircleOfCareContact> getCircleOfCareContactList() {
        return circleOfCare;
    }

    public void setCircleOfCareContactList(List<CircleOfCareContact> circleOfCareContactList) {
        this.circleOfCare = circleOfCareContactList;
    }

    public void addCircleOfCarePerson(CircleOfCareContact circleOfCareContact)
    {
        this.circleOfCare.add(circleOfCareContact) ;
    }


}
