package com.sjsu.healthcare.Service;


import com.sjsu.healthcare.Model.ActivityData;
import com.sjsu.healthcare.Model.SleepData;
import com.sjsu.healthcare.Repository.SleepDataRepository;
import com.sjsu.healthcare.DBHandler.SleepDataHandler;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SleepDataService {

    @Autowired
    private SleepDataRepository sleepDataRepository;

    private SleepDataHandler handler = new SleepDataHandler();
    //Save new data
    @RequestMapping(value = "api/sleep", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity sleepDataPost(@RequestBody SleepData sleepData) {
        sleepDataRepository.save(sleepData);
        JSONObject obj = new JSONObject();
        obj.put("patientID", sleepData.getPatientId());
        return new ResponseEntity(obj,HttpStatus.CREATED);
    }

    //Get all data for a patient
    @RequestMapping(value = "api/patient/{id}/sleep", method = RequestMethod.GET)
    public ResponseEntity sleepDataGet(@PathVariable("id") String patientId) {
        ArrayList<SleepData> sleepData = sleepDataRepository.findByPatientId(patientId);
        return new ResponseEntity(sleepData, HttpStatus.OK);
    }

    //Get sleep efficiency and time - last x days
    @RequestMapping(value = "api/patient/{id}/sleepefficiency/{days}", method = RequestMethod.GET)
    public ResponseEntity getSleepEfficiency(@PathVariable("id") String patientId, @PathVariable("days") int days)
    {
        if(sleepDataRepository.findByPatientId(patientId) == null)
        {
            return new ResponseEntity("PatientID not valid, not found", HttpStatus.NOT_FOUND);
        }

         return new ResponseEntity(handler.getSleepEfficiency(patientId, days),HttpStatus.OK );
    }

    //Get all patient's sleep data, for last day
    @RequestMapping(value = "api/sleepdataforpatients", method = RequestMethod.GET)
    public ResponseEntity getSleepDataForAllPatients()
    {
        List<SleepData> sleepDataList = handler.getSleepDataForLastDayAllPatients();
        if(sleepDataList.isEmpty())
        {
            return new ResponseEntity("Cannot find sleep data for any patient", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(sleepDataList,HttpStatus.OK );
    }
}
