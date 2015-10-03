package com.sjsu.healthcare.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.sjsu.healthcare.Model.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {

    public Patient findById(String Id);

}
