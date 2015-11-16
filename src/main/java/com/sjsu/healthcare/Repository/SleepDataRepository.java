package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.SleepData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface SleepDataRepository extends MongoRepository<SleepData, String> {
    ArrayList<SleepData> findByPatientId(String patientId);
}
