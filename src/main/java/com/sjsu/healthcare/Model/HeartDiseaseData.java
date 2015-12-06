package com.sjsu.healthcare.Model;

/**
 * Created by Sindhu Kashyap on 11/16/2015.
 */
public class HeartDiseaseData

{
    int cholestrol;
    int maxPulseRate;
    int restingPulseRate;
    int stepCount;
    int bmi;
    boolean decision;
    String patientID;
    int age;
    boolean circleOfCareNotified;

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCholestrol() {
        return cholestrol;
    }

    public void setCholestrol(int cholestrol) {
        this.cholestrol = cholestrol;
    }

    public int getMaxPulseRate() {
        return maxPulseRate;
    }

    public void setMaxPulseRate(int maxPulseRate) {
        this.maxPulseRate = maxPulseRate;
    }

    public int getRestingPulseRate() {
        return restingPulseRate;
    }

    public void setRestingPulseRate(int restingPulseRate) {
        this.restingPulseRate = restingPulseRate;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }

    public boolean getDecision() {
        return this.decision;
    }

    public boolean getCircleOfCareNotified() {
        return circleOfCareNotified;
    }

    public void setCircleOfCareNotified(boolean circleOfCareNotified) {
        this.circleOfCareNotified = circleOfCareNotified;
    }

}
