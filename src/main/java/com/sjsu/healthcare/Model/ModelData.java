package com.sjsu.healthcare.Model;

public class ModelData {
    private String id;
    private int age;
    private int cholestrol;
    private int maxPulseRate;
    private int restingPulseRate;
    private int bmi;
    private int stepCount;
    private int result;

    public ModelData()
    {

    }

    public ModelData(String id, int age, int cholestrol, int maxPulseRate, int restingPulseRate, int bmi,
                     int stepCount, int result)
    {
        this.id = id;
        this.age = age;
        this.cholestrol = cholestrol;
        this.maxPulseRate = maxPulseRate;
        this.restingPulseRate
                 = restingPulseRate;
        this.bmi = bmi;
        this.stepCount = stepCount;
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public Boolean isResult() {
        if (result > 0) {
            return true;
        }
        return false;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

}
