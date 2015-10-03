package com.sjsu.healthcare.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sjsu.healthcare.Repository.PatientRepository;
import com.sjsu.healthcare.Model.Patient;


import java.util.List;

@RestController
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    //Save Patient
    @RequestMapping(value = "api/patients", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity patientPost(@RequestBody Patient p) {
        patientRepository.save(p);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //View Patient
    @RequestMapping(value = "api/patients/{id}", method = RequestMethod.GET)
    public ResponseEntity patientGet(@PathVariable("id") String id) {
        Patient p = patientRepository.findById(id);
        return new ResponseEntity(p, HttpStatus.OK);
    }

    //Get All Patients
    @RequestMapping(value = "api/allPatients", method = RequestMethod.GET)
    public List<Patient> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients;
    }

    //Update Patient
    @RequestMapping(value = "api/patient/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity patientPut(@PathVariable("id") String id, @RequestBody Patient p) {
        Patient patient;
        try {
            patient = patientRepository.findById(id);
        } catch (Exception e){
            return new ResponseEntity("Patient Id requested not found!", HttpStatus.NOT_FOUND);
        }
        if (p.getWeight() != 0) {
            patient.setWeight(p.getWeight());
        }

        if (p.getBmi() != 0) {
            patient.setBmi(p.getBmi());
        }


        if (p.getPhoneNumber() != 0) {
            patient.setPhoneNumber(p.getPhoneNumber());
        }

        if (p.getEmail() != null) {
            patient.setEmail(p.getEmail());
        }

        if (p.getUsername() != null) {
            patient.setUsername(p.getUsername());
        }

        if (p.getPassword() != null) {
            patient.setPassword(p.getPassword());
        }

        if (p.getFitbitUsername() != null) {
            patient.setFitbitUsername(p.getFitbitUsername());
        }

        patientRepository.save(patient);
        return new ResponseEntity(patient, HttpStatus.OK);
    }
}
