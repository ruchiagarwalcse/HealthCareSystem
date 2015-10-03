package com.sjsu.healthcare.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class SleepData {
    private String id;
    private String patientId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date startTime;

    private int timeInBed;
    private int minutesAsleep;
    private int awakeningsCount;
    private int minutesAwake;
    private int efficiency;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getTimeInBed() {
        return timeInBed;
    }

    public void setTimeInBed(int timeInBed) {
        this.timeInBed = timeInBed;
    }

    public int getMinutesAsleep() {
        return minutesAsleep;
    }

    public void setMinutesAsleep(int minutesAsleep) {
        this.minutesAsleep = minutesAsleep;
    }

    public int getAwakeningsCount() {
        return awakeningsCount;
    }

    public void setAwakeningsCount(int awakeningsCount) {
        this.awakeningsCount = awakeningsCount;
    }

    public int getMinutesAwake() {
        return minutesAwake;
    }

    public void setMinutesAwake(int minutesAwake) {
        this.minutesAwake = minutesAwake;
    }

    public int getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(int efficiency) {
        this.efficiency = efficiency;
    }
}
