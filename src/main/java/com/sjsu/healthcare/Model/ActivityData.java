package com.sjsu.healthcare.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ActivityData {

    private String id;
    private String patientId;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date date;

    private int stepCount;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        this.date = cal.getTime();
        /*DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        DateTime lastDay = today.minusDays(1);
        this.date = lastDay.toDate();*/
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }
}
