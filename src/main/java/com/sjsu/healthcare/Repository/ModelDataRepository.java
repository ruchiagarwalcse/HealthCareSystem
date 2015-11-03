package com.sjsu.healthcare.Repository;

import com.sjsu.healthcare.Model.ModelData;
import com.sjsu.healthcare.Model.ModelTestData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelDataRepository extends MongoRepository<ModelData, String> {
    public ModelData findById(String Id);
}
