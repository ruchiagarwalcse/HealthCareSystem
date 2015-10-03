package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.Patient;
import com.sjsu.healthcare.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.UUID;

import com.sjsu.healthcare.Model.Medication;


@RestController
public class MedicationService {

    @Autowired
    private PatientRepository patientRepository;

    //Save new medication for a patient
    @RequestMapping(value = "api/patient/{id}/medication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity medicationPost(@PathVariable("id") String patientId, @RequestBody Medication medication) {
        Patient patient = patientRepository.findById(patientId);
        medication.setId();
        patient.addMedication(medication);
        patientRepository.save(patient);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get all medication for a patient
    @RequestMapping(value = "api/patient/{pid}/medications", method = RequestMethod.GET)
    public ArrayList<Medication> medicationsGetByPatientId(@PathVariable("pid") String patientId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient != null){
            return patient.getMedication();
        }
        return null;
    }

    //Delete medication for a patient by id
    @RequestMapping(value="api/patient/{pid}/medication/{mid}", method = RequestMethod.DELETE)
    public ResponseEntity medicationDeleteById(@PathVariable("pid") String patientId, @PathVariable("mid") UUID medicationId) {
        Patient patient = patientRepository.findById(patientId);
        patient.removeMedicationById(medicationId);
        patientRepository.save(patient);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
