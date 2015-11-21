package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.ActivityData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.Date;

public interface ActivityDataRepository extends MongoRepository<ActivityData, String>{
    ArrayList<ActivityData> findByPatientId(String patientId);
    ActivityData findByPatientIdAndDate(String patientId, Date date);
}
