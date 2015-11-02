package com.sjsu.healthcare.Service;

import java.util.ArrayList;
import java.util.UUID;

import com.sjsu.healthcare.Model.CircleOfCareContact;
import com.sjsu.healthcare.Model.Patient;
import com.sjsu.healthcare.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.common.base.Strings;
@RestController
public class CircleOfCareContactService {

    @Autowired
    private PatientRepository patientRepository;

    //Save new contact for circle of care
    @RequestMapping(value = "api/patient/{id}/circleofcarecontact", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity circleOfCarePost(@PathVariable("id") String patientId, @RequestBody CircleOfCareContact circleOfCareContact) {
        Patient patient = patientRepository.findById(patientId);
        circleOfCareContact.setId();
        patient.addCircleOfCarePerson(circleOfCareContact);
        patientRepository.save(patient);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //Get circle of care contact for a patient by id
    @RequestMapping(value = "api/patient/{pid}/circleofcarecontact/{cid}", method = RequestMethod.GET)
    public CircleOfCareContact circleOfCareContactGetById(@PathVariable("pid") String patientId, @PathVariable("cid") UUID circleOfCareContactId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient != null){
            return patient.getCircleOfCareContactById(circleOfCareContactId);
        }
        return null;
    }

    //Get circle of care contact for a patient by priority level
    @RequestMapping(value = "api/patient/{pid}/circleofcarecontactbypriority/{level}", method = RequestMethod.GET)
    public CircleOfCareContact circleOfCareContactGetByPriorityLevel(@PathVariable("pid") String patientId, @PathVariable("level") String priority) {
        Patient patient = patientRepository.findById(patientId);
        if (patient != null){
            return patient.getCircleOfCareContactByPriorityLevel(priority);
        }
        return null;
    }

    //Get all circle of care contact for a patient
    @RequestMapping(value = "api/patient/{pid}/circleofcarecontacts", method = RequestMethod.GET)
    public ArrayList<CircleOfCareContact> circleOfCareContacts(@PathVariable("pid") String patientId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient != null){
            return patient.getCircleOfCare();
        }
        return null;
    }

    //Delete circle of care contact for a patient by id
    @RequestMapping(value="api/patient/{pid}/circleofcarecontact/{cid}", method = RequestMethod.DELETE)
    public ResponseEntity circleOfCareContactDeleteById(@PathVariable("pid") String patientId, @PathVariable("cid") UUID circleOfCareContactId) {
        Patient patient = patientRepository.findById(patientId);
        patient.removeCircleOfCareContactById(circleOfCareContactId);
        patientRepository.save(patient);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //Delete circle of care contact for a patient by level
    @RequestMapping(value="api/patient/{pid}/circleofcarecontactbypriority/{level}", method = RequestMethod.DELETE)
    public ResponseEntity circleOfCareContactDeleteByPriority(@PathVariable("pid") String patientId, @PathVariable("level") String priority) {
        Patient patient = patientRepository.findById(patientId);
        patient.removeCircleOfCareContactByPriorityLevel(priority);
        patientRepository.save(patient);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    //get CIrcleofCare Contact for a patient by patientid and COCID
    @RequestMapping(value = "api/patient/{pid}/circleofcarebyid/{cid}", method = RequestMethod.GET)
    public ResponseEntity getCircleOfCareContactsById(@PathVariable("pid") String patientId,
                                                      @PathVariable("cid") UUID circleOfCareContactId)
    {
        Patient patient = patientRepository.findById(patientId);
        if(patient == null)
        {
            return new ResponseEntity( "Requested Patient not found",HttpStatus.NOT_FOUND);
        }
        CircleOfCareContact c = patient.getCircleOfCareContactById(circleOfCareContactId);
        if (c == null){
           return new ResponseEntity( "Requested CoC not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(c,HttpStatus.OK);
    }

    //update CIrcleofCare Contact for a patient by patientid and COCID
    @RequestMapping(value = "api/patient/{pid}/updatecircleofcare/{cid}", method = RequestMethod.PUT)
    public ResponseEntity updateCircleOfCareContactById(@PathVariable("pid") String patientId,
                                                        @PathVariable("cid") UUID circleOfCareContactId,
                                                            @RequestBody CircleOfCareContact c)
    {
        Patient patient = patientRepository.findById(patientId);
        CircleOfCareContact circle = null;
        if(patient == null)
        {
            return new ResponseEntity("Patient Id requested not found!", HttpStatus.NOT_FOUND);
        }
        circle = patient.getCircleOfCareContactById(circleOfCareContactId);
        if(circle == null)
        {
            return new ResponseEntity( "Requested CircleofCareContact not found",HttpStatus.NOT_FOUND);
        }

        if(!Strings.isNullOrEmpty(c.getEmail()))
        //if(!c.getEmail().isEmpty() && !c.getEmail().equals(null))
        {
            circle.setEmail(c.getEmail());
        }

        if(!Strings.isNullOrEmpty(c.getName()))
        {
            circle.setName(c.getName());
        }

        if(!Strings.isNullOrEmpty(c.getPriority()))
        {
            circle.setPriority(c.getPriority());
        }

        if(!Strings.isNullOrEmpty(c.getRelation()))
        {
            circle.setRelation(c.getRelation());
        }

        if(c.getPhoneNumber() != 0)
        {
            circle.setPhoneNumber(c.getPhoneNumber());
        }
        patientRepository.save(patient);
        return new ResponseEntity(circle,HttpStatus.OK);
    }

}
