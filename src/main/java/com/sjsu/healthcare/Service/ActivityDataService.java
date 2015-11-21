package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.ActivityData;
import com.sjsu.healthcare.Repository.ActivityDataRepository;
import com.sjsu.healthcare.DBHandler.ActivityDataHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

@RestController
public class ActivityDataService {

    @Autowired
    private ActivityDataRepository activityDataRepository;

    private ActivityDataHandler activityDataHandler = new ActivityDataHandler();

    //Save new data
    @RequestMapping(value = "api/activity", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity activityDataPost(@RequestBody ActivityData activityData) {

        System.out.println(new Date());
        activityData.setDate();
        activityDataRepository.save(activityData);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get all data for a patient
    @RequestMapping(value = "api/patient/{id}/activity", method = RequestMethod.GET)
    public ResponseEntity activityDataGet(@PathVariable("id") String patientId) {
        ArrayList<ActivityData> activityData = activityDataRepository.findByPatientId(patientId);
        return new ResponseEntity(activityData, HttpStatus.OK);
    }

    //Get a patient's activity, x days before current date
    @RequestMapping(value = "api/patient/{id}/activitydatarange/{days}", method = RequestMethod.GET)
    public ResponseEntity getActivityDataTimeRange(@PathVariable("id") String patientId, @PathVariable("days") int days)
    {
        ArrayList<ActivityData> activityData = activityDataRepository.findByPatientId(patientId);
        if(activityData.isEmpty())
        {
           return new ResponseEntity("Cannot find activity for this patient", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(activityDataHandler.getActivityListFromRange(patientId, days),HttpStatus.OK );
    }

}
