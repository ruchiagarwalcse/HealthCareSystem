package com.sjsu.healthcare.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sjsu.healthcare.Repository.PatientRepository;
import com.sjsu.healthcare.Model.Patient;


import java.util.List;
import com.google.common.base.Strings;

@RestController
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    //Homepage check
    @RequestMapping(value = "api/homepage", method = RequestMethod.GET)
    public String getHomePage() {
        return "Welcome to Home Page";
    }

    //Save Patient
    @RequestMapping(value = "api/patients", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity patientPost(@RequestBody Patient p) {
//
        String hashedPass = Patient.hashPassword(p.getPassword());
        p.setPassword(hashedPass);
        p.setCreatedAt();
        p = patientRepository.save(p);
        //:TODO: Validate the details here, email format should be proper
        return new ResponseEntity(p, HttpStatus.CREATED);
    }

    //Login patient with username and password
    @RequestMapping(value = "api/patient/login", method = RequestMethod.POST)
    public ResponseEntity loginPatient(@RequestBody Patient p)
    {
        Patient patient = null;
        if(!Strings.isNullOrEmpty(p.getUsername()) && !Strings.isNullOrEmpty(p.getPassword()))
        {
            patient = patientRepository.findByUsername(p.getUsername());
        }
        if(patient == null)
        {
            return new ResponseEntity("Invalid username", HttpStatus.NOT_FOUND);
        }
        if(Patient.checkPassword(p.getPassword(),patient.getPassword()))
       // if(patient.getPassword().equals(p.getPassword()))
        {
            return new ResponseEntity(patient.getId(), HttpStatus.OK);
        }
        else
            return new ResponseEntity("Invalid password", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "api/v2/patient/login", method = RequestMethod.POST)
    private ResponseEntity loginPatientApp(@RequestBody Patient p)
    {
        Patient patient = null;
        if(!Strings.isNullOrEmpty(p.getUsername()) && !Strings.isNullOrEmpty(p.getPassword()))
        {
            patient = patientRepository.findByUsername(p.getUsername());
        }
        if(patient == null)
        {
            return new ResponseEntity("Invalid username", HttpStatus.NOT_FOUND);
        }
        if(Patient.checkPassword(p.getPassword(),patient.getPassword()))
        //if(patient.getPassword().equals(p.getPassword()))
        {
            return new ResponseEntity(patient, HttpStatus.OK);
        }
        else
            return new ResponseEntity("Invalid password", HttpStatus.NOT_FOUND);
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

        if(!p.getDateOfBirth().equals("") && !p.getDateOfBirth().equals(null))
        {
            patient.setDateOfBirth(p.getDateOfBirth());
        }

        if (p.getBmi() != 0) {
            patient.setBmi(p.getBmi());
        }

        if(p.getCholestrol() != 0)
        {
            patient.setCholestrol(p.getCholestrol());
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
            String hashedPass = Patient.hashPassword(p.getPassword());
            patient.setPassword(hashedPass);

        }

        if (p.getFitbitUsername() != null) {
            patient.setFitbitUsername(p.getFitbitUsername());
        }
        patientRepository.save(patient);
        return new ResponseEntity(patient, HttpStatus.OK);
    }
}
