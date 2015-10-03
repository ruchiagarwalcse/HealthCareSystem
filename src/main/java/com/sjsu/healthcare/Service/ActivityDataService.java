package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.ActivityData;
import com.sjsu.healthcare.Repository.ActivityDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ActivityDataService {

    @Autowired
    private ActivityDataRepository activityDataRepository;

    //Save new data
    @RequestMapping(value = "api/activity", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity activityDataPost(@RequestBody ActivityData activityData) {
        activityDataRepository.save(activityData);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get all data for a patient
    @RequestMapping(value = "api/patient/{id}/activity", method = RequestMethod.GET)
    public ResponseEntity activityDataGet(@PathVariable("id") String patientId) {
        ArrayList<ActivityData> activityData = activityDataRepository.findByPatientId(patientId);
        return new ResponseEntity(activityData, HttpStatus.OK);
    }
}
