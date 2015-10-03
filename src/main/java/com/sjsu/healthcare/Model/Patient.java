package com.sjsu.healthcare.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Patient {

    private String id;
    private String name;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private Date dateOfBirth;

    private float height;
    private int weight;
    private int bmi;
    private long phoneNumber;
    private String email;
    private String username;
    private String password;
    private String fitbitUsername;
    private ArrayList<CircleOfCareContact> circleOfCare = new ArrayList<CircleOfCareContact>();
    private ArrayList<Medication> medicineSchedule = new ArrayList<Medication>();

    public Patient(){

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {this.id = id;}

    public int getWeight() {

        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFitbitUsername() {
        return fitbitUsername;
    }

    public void setFitbitUsername(String fitbitUsername) {
        this.fitbitUsername = fitbitUsername;
    }

    public ArrayList<CircleOfCareContact> getCircleOfCare() {
        return circleOfCare;
    }

    public void setCircleOfCare(ArrayList<CircleOfCareContact> circleOfCare) {
        this.circleOfCare = circleOfCare;
    }

    public ArrayList<Medication> getMedication() {
        return medicineSchedule;
    }

    public void setMedication(ArrayList<Medication> medication) {
        this.medicineSchedule = medication;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void addCircleOfCarePerson(CircleOfCareContact circleOfCareContact) {this.circleOfCare.add(circleOfCareContact) ;
    }

    //Get circle of care contact created by id
    public CircleOfCareContact getCircleOfCareContactById(UUID id) {
        CircleOfCareContact circleOfCareContact = null;
        for (CircleOfCareContact c : this.circleOfCare)
        {
            if (c.getId().equals(id)) {
                circleOfCareContact = c;
                break;
            }
        }
        return circleOfCareContact;
    }

    //Get circle of care contact created by priority level
    public CircleOfCareContact getCircleOfCareContactByPriorityLevel(String priority) {
        CircleOfCareContact circleOfCareContact = null;
        for (CircleOfCareContact c : this.circleOfCare)
        {
            if (c.getPriority().equals(priority)) {
                circleOfCareContact = c;
                break;
            }
        }
        return circleOfCareContact;
    }

    //Remove circle of care contact using id
    public void removeCircleOfCareContactById(UUID id) {
        CircleOfCareContact circleOfCareContact = getCircleOfCareContactById(id);
        this.circleOfCare.remove(circleOfCareContact);
    }

    //Remove circle of care contact using priority level
    public void removeCircleOfCareContactByPriorityLevel(String priority) {
        CircleOfCareContact circleOfCareContact = getCircleOfCareContactByPriorityLevel(priority);
        this.circleOfCare.remove(circleOfCareContact);
    }

    public void addMedication(Medication medication) {this.medicineSchedule.add(medication) ;}

    //Remove medication by id
    public void removeMedicationById(UUID id) {
        for (Medication m : this.medicineSchedule)
        {
            if (m.getId().equals(id)) {
                medicineSchedule.remove(m);
                break;
            }
        }
    }
}
