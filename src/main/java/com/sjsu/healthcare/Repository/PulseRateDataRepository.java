package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.PulseRateData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

public interface PulseRateDataRepository extends MongoRepository<PulseRateData, String> {
    public ArrayList<PulseRateData> findByPatientId(String patientId);
}
