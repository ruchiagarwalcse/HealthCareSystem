package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.PulseRateData;
import com.sjsu.healthcare.Repository.PulseRateDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class PulseRateDataService {

    @Autowired
    private PulseRateDataRepository pulseRateDataRepository;

    //Save new data
    @RequestMapping(value = "api/pulserate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity pulseRateDataPost(@RequestBody PulseRateData pulseRateData) {
        pulseRateDataRepository.save(pulseRateData);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get all data for a patient
    @RequestMapping(value = "api/patient/{id}/pulserate", method = RequestMethod.GET)
    public ResponseEntity pulseRateDataGet(@PathVariable("id") String patientId) {
        ArrayList<PulseRateData> pulseRateData = pulseRateDataRepository.findByPatientId(patientId);
        return new ResponseEntity(pulseRateData, HttpStatus.OK);
    }
}
