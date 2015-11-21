package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.Patient;
import com.sjsu.healthcare.Model.PulseRateData;
import com.sjsu.healthcare.Repository.NotificationRepository;
import com.sjsu.healthcare.Repository.PatientRepository;
import com.sjsu.healthcare.Repository.PulseRateDataRepository;
import com.sjsu.healthcare.DBHandler.PulseRateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PulseRateDataService {

    @Autowired
    private PulseRateDataRepository pulseRateDataRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    NotificationRepository notificationRepository;
    private PulseRateHandler pulseRateHandler = new PulseRateHandler();
    //Save new pulse rate data
    //takes a list of pulserate data and saves it in the database
    //also checks for abnormal pulserate in the list and creates a notification object if it finds any abnormal data
    @RequestMapping(value = "api/pulserate", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity pulseRateDataPost(@RequestBody List<PulseRateData> pulseRateDataList) {

//        List<PulseRateData> pulseRateObjectList = new ArrayList<PulseRateData>();
//        List<Integer> pulseRateValueList = new ArrayList<Integer>();
        int minPulseRate = 30;
        int maxPulseRate = 190;
        PulseRateData minPulse = null;
        PulseRateData maxPulse = null;
        Patient patient = null;
        for(PulseRateData pulseRateData : pulseRateDataList )
        {
            patient = patientRepository.findById(pulseRateData.getPatientId());
            if(patient == null)
            {
                return new ResponseEntity("Cannot find pulserate data for this patient", HttpStatus.NOT_FOUND);
            }
            pulseRateDataRepository.save(pulseRateData);
            int pulserate = pulseRateData.getPulseRate();
            //check for the min and max pulse rates outside the threshold value and save the two correspoding PulseRateData objects
            if( pulserate <= 30 )
            {
                if(pulserate < minPulseRate)
                {
                    minPulseRate = pulserate;
                    minPulse = pulseRateData;
                }

//                pulseRateObjectList.add(pulseRateData);
//                pulseRateValueList.add(pulserate);
            }
            if( pulserate >= 190)
            {
                if(pulserate > maxPulseRate)
                {
                    maxPulseRate = pulserate;
                    maxPulse = pulseRateData;
                }
            }
        }
        if(minPulse != null || maxPulse != null)
        {

            int noOfNotifications = pulseRateHandler.notifyAbnormalPulseRate(minPulse,maxPulse, patient, notificationRepository);
            System.out.println("noOfNotifications:"+noOfNotifications);
             return new ResponseEntity(noOfNotifications + " abnormal pulserate found with the patient." +
                     " CircleOfCare has been appropriately notified.", HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get all data for a patient
    @RequestMapping(value = "api/patient/{id}/pulserate", method = RequestMethod.GET)
    public ResponseEntity pulseRateDataGet(@PathVariable("id") String patientId) {
        ArrayList<PulseRateData> pulseRateData = pulseRateDataRepository.findByPatientId(patientId);
        return new ResponseEntity(pulseRateData, HttpStatus.OK);
    }

    //Get data for last x days
    @RequestMapping(value = "api/pulserate/{id}/days/{days}", method = RequestMethod.GET)
    public ResponseEntity getPulseRateRange(@PathVariable("id") String patientID,
                                            @PathVariable("days") int days)
    {
        ArrayList<PulseRateData> pList = pulseRateDataRepository.findByPatientId(patientID);
        if(pList ==  null)
        {
            return new ResponseEntity("No pulse data found for this user ", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(pulseRateHandler.getPulseRateBetween(patientID, days),HttpStatus.OK );
    }
}
