package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.ModelTestData;
import com.sjsu.healthcare.Model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelTestDataRepository extends MongoRepository<ModelTestData, String> {
    public ModelTestData findById(String Id);
}
