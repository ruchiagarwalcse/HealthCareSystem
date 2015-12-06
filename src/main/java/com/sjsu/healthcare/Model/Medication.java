package com.sjsu.healthcare.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.UUID;

public class Medication {

    //private UUID id;
    @Id private String id;
    private String name;
    @JsonFormat(pattern = "HH:mm")
    private Date time;

    public Medication() {
	this.id = new ObjectId().toString();
    }

//    public UUID getId() {
//        return id;
//    }
//
//    public void setId() {
//        this.id = UUID.randomUUID();
//    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = new ObjectId().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
