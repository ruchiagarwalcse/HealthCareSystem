package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.Patient;
import com.sjsu.healthcare.Repository.MedicineRepository;
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
    @Autowired
    private MedicineRepository medicineRepository;

    //Save new medication for a patient
    @RequestMapping(value = "api/patient/{id}/medication", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity medicationPost(@PathVariable("id") String patientId, @RequestBody Medication medication) {
        Patient patient = patientRepository.findById(patientId);
        if(patient == null)
        {
            new ResponseEntity( "Requested Patient not found",HttpStatus.NOT_FOUND);
        }

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
    public ResponseEntity medicationDeleteById(@PathVariable("pid") String patientId, @PathVariable("mid") String medicationId) {
        Patient patient = patientRepository.findById(patientId);
        patient.removeMedicationById(medicationId);
        patientRepository.save(patient);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //get medication by medication id and patient Id
    @RequestMapping(value = "api/patient/{pid}/medicationbyid/{mid}", method = RequestMethod.GET)
    public ResponseEntity medicationsGetById(@PathVariable("pid") String patientId, @PathVariable("mid") String medId) {
        // Medication med = medicineRepository.findByPatientAndMedicationID(patientId, medId);
       // Medication med = medicineRepository.findById(medId);
        //Medication med = medicineRepository.findByMedicineScheduleId(medId);
        Patient patient = patientRepository.findById(patientId);
        Medication m = patient.getMedication(medId);
        if(m == null)
        {
            return new ResponseEntity( "Requested Medication not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(m,HttpStatus.OK);
    }

        //update medication for a patient by patientid, medicationID
    @RequestMapping(value="api/patient/{pid}/medicationupdate/{mid}", method = RequestMethod.PUT)
    public ResponseEntity medicationUpdateByIds(@PathVariable("pid") String patientId, @PathVariable("mid") String medicationId,
                                                @RequestBody Medication medication)
    {
        Patient patient;
        Medication medicationObj;
        try
        {
            patient = patientRepository.findById(patientId);
        }
        catch (Exception e)
        {
            return new ResponseEntity("Patient Id requested not found!", HttpStatus.NOT_FOUND);
        }
        medicationObj =  patient.getMedication(medicationId);
            // medicationObj = medicineRepository.findByPatientAndMedicationID(patientId,medicationId);
        if(medicationObj == null)
        {
            new ResponseEntity( "Requested Medication not found",HttpStatus.NOT_FOUND);
        }
        if(medication.getName() != null && !medication.getName().isEmpty())
        {
            medicationObj.setName(medication.getName());
        }

        if(medication.getTime() != null)
        {
            medicationObj.setTime(medication.getTime());
        }
        patientRepository.save(patient);
        return new ResponseEntity(medicationObj, HttpStatus.OK);
    }

}
