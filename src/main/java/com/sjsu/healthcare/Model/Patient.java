package com.sjsu.healthcare.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Email;
import org.mindrot.jbcrypt.BCrypt;
//import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;

import java.util.*;

public class Patient {

    private String id;
    private String name;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private Date dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date createdAt;
    private float height;
    private int weight;
    private int bmi;
    private long phoneNumber;
    private int cholestrol;

    // @Email	//Email format validation
    private String email;
    private String username;
    private String password;
    private String fitbitUsername;
    private ArrayList<CircleOfCareContact> circleOfCare = new ArrayList<CircleOfCareContact>();
    private ArrayList<Medication> medicineSchedule = new ArrayList<Medication>();
    private boolean hasHeartDisease;

    public boolean isHasHeartDisease() {
        return hasHeartDisease;
    }

    public void setHasHeartDisease(boolean hasHeartDisease) {
        this.hasHeartDisease = hasHeartDisease;
    }

    public boolean getHasHeartDisease() {
        return hasHeartDisease;
    }

    public Patient(){

    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        this.createdAt = cal.getTime();
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

    @Email(message = "Email Address is not a valid format")
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

    public int getCholestrol() {
        return this.cholestrol;
    }

    public void setCholestrol(int cholestrol) {this.cholestrol = cholestrol;}


    public void addCircleOfCarePerson(CircleOfCareContact circleOfCareContact) {this.circleOfCare.add(circleOfCareContact) ;
    }

    //Get circle of care contact created by id
    public CircleOfCareContact getCircleOfCareContactById(String id) {
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
            if (c.getPriority().equalsIgnoreCase(priority)) {
                circleOfCareContact = c;
                break;
            }
        }
        return circleOfCareContact;
    }

    //Remove circle of care contact using id
    public void removeCircleOfCareContactById(String id) {
        CircleOfCareContact circleOfCareContact = getCircleOfCareContactById(id);
        this.circleOfCare.remove(circleOfCareContact);
    }

    //Remove circle of care contact using priority level
    public void removeCircleOfCareContactByPriorityLevel(String priority) {
        CircleOfCareContact circleOfCareContact = getCircleOfCareContactByPriorityLevel(priority);
        this.circleOfCare.remove(circleOfCareContact);
    }

    public boolean addMedication(Medication medication)
    {
        if(medication.getName().isEmpty() || medication.getName().equals(null)
                || medication.getTime() == null)
        {
            return false;
        }
        return this.medicineSchedule.add(medication);
    }

    //Remove medication by id
    public void removeMedicationById(String id) {
        for (Medication m : this.medicineSchedule)
        {
            if (m.getId().equals(id)) {
                medicineSchedule.remove(m);
                break;
            }
        }
    }

    //get Medication object from patient
    public Medication getMedication(String medId)
    {
        for(Medication m : this.medicineSchedule)
        {
            if(m.getId().equals(medId))
            {
                return m;
            }
        }
        return null;
    }

    public boolean medicationExists(String medId)
    {
        for(Medication m : this.medicineSchedule)
        {
            if(m.getId().equals(medId))
            {
                return true;
            }
        }
        return false;
    }

    public static String hashPassword(String password_plaintext) {
          int workload = 12;
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        return(hashed_password);
    }

    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return(password_verified);
    }


}
