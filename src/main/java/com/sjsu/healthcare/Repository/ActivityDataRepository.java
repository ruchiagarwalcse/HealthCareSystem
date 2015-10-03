package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.ActivityData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface ActivityDataRepository extends MongoRepository<ActivityData, String>{
    public ArrayList<ActivityData> findByPatientId(String patientId);
}
