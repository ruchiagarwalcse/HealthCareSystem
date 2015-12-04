package com.sjsu.healthcare.Model;

import org.bson.types.ObjectId;

public class CircleOfCareContact {
    /*High: Doctor
      Medium: Emergency Contact
      Low: Family Member*/

    public CircleOfCareContact() {
        this.id = new ObjectId().toString();
    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = new ObjectId().toString();
    }

    private String id;
    private String name;
    private long phoneNumber;
    private String email;
    private String relation;
    private String priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}
