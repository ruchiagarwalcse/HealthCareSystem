package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.Model.ModelData;
import com.sjsu.healthcare.Model.ModelTestData;
import com.sjsu.healthcare.Repository.ModelDataRepository;
import com.sjsu.healthcare.Repository.ModelTestDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class DataService {

    @Autowired
    private ModelDataRepository modelDataRepository;

    @Autowired
    private ModelTestDataRepository modelTestDataRepository;

    //Parse data
    @RequestMapping(value = "api/parsedata", method = RequestMethod.GET)
    public void notificationGet() {
        String fileName = "/Users/piyushmittal/Desktop/RawData.txt";
        String line = null;

        try {
            FileReader fileReader =
                    new FileReader(fileName);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);
            line = bufferedReader.readLine();
            String[] records = line.split("name");
            for (String s: records) {
                System.out.println(s);
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
    }


    //Generate Model BMI and step count
    @RequestMapping(value = "api/generateModelData", method = RequestMethod.GET)
    public void generateModelParameters() {
        int bmi = 0, step = 0;
        Random random  = new Random();
        int maxNormal = 25, minNormal = 17;
        int maxOver = 30, minOver = 25;
        int maxObese = 40, minObese = 30;
        int maxExtreme = 42;

        int maxNormalStep = 15000, minNormalStep = 9000;
        int maxOverStep = 8000, minOverStep = 6000;
        int maxObeseStep = 6000, minObeseStep = 3000;
        int maxExtremeStep = 3500;

        List<ModelData> data = modelDataRepository.findAll();
        List<ModelData> normal =  new ArrayList<ModelData>();
        List<ModelData> issues =  new ArrayList<ModelData>();

        for(ModelData d: data) {
            if (d.isResult()) {
                issues.add(d);
            } else {
                normal.add(d);
            }
        }

        for(ModelData d: normal) {
           if (d.getRestingPulseRate() < 75) {
               bmi = random.nextInt(maxNormal - minNormal + 1) + minNormal;
               step = random.nextInt(maxNormalStep - minNormalStep + 1) + minNormalStep;
           } else {
               bmi = random.nextInt(maxObese - minOver + 1) + minOver;
               step = random.nextInt(maxObeseStep - minOverStep + 1) + minOverStep;
           }
            d.setBmi(bmi);
            d.setStepCount(step);
            modelDataRepository.save(d);
        }

        for(ModelData d: issues) {
            if (d.getRestingPulseRate() < 75) {
                bmi = random.nextInt(maxOver - minOver + 1) + minOver;
                step = random.nextInt(maxOverStep - minOverStep + 1) + minOverStep;
            } else {
                bmi = random.nextInt(maxExtreme - minObese + 1) + minObese;
                step = random.nextInt(maxExtremeStep - minObeseStep + 1) + minObeseStep;
            }
            d.setBmi(bmi);
            d.setStepCount(step);
            modelDataRepository.save(d);
        }
    }



    @RequestMapping(value = "api/generateModelTestData", method = RequestMethod.GET)
    public void generateModelTestParameters() {
        int bmi = 0, step = 0;
        Random random  = new Random();
        int maxNormal = 25, minNormal = 17;
        int maxOver = 30, minOver = 25;
        int maxObese = 40, minObese = 30;
        int maxExtreme = 42;

        int maxNormalStep = 15000, minNormalStep = 9000;
        int maxOverStep = 8000, minOverStep = 6000;
        int maxObeseStep = 6000, minObeseStep = 3000;
        int maxExtremeStep = 3500;

        List<ModelTestData> data = modelTestDataRepository.findAll();
        List<ModelTestData> normal =  new ArrayList<ModelTestData>();
        List<ModelTestData> issues =  new ArrayList<ModelTestData>();

        for(ModelTestData d: data) {
            if (d.isResult()) {
                issues.add(d);
            } else {
                normal.add(d);
            }
        }

        for(ModelTestData d: normal) {
            if (d.getRestingPulseRate() < 75) {
                bmi = random.nextInt(maxNormal - minNormal + 1) + minNormal;
                step = random.nextInt(maxNormalStep - minNormalStep + 1) + minNormalStep;
            } else {
                bmi = random.nextInt(maxObese - minOver + 1) + minOver;
                step = random.nextInt(maxObeseStep - minOverStep + 1) + minOverStep;
            }
            d.setBmi(bmi);
            d.setStepCount(step);
            modelTestDataRepository.save(d);
        }

        for(ModelTestData d: issues) {
            if (d.getRestingPulseRate() < 75) {
                bmi = random.nextInt(maxOver - minOver + 1) + minOver;
                step = random.nextInt(maxOverStep - minOverStep + 1) + minOverStep;
            } else {
                bmi = random.nextInt(maxExtreme - minObese + 1) + minObese;
                step = random.nextInt(maxExtremeStep - minObeseStep + 1) + minObeseStep;
            }
            d.setBmi(bmi);
            d.setStepCount(step);
            modelTestDataRepository.save(d);
        }
    }

    @RequestMapping(value = "api/transferData", method = RequestMethod.GET)
    public void transferData() {
        List<ModelData> modelData = modelDataRepository.findAll();
        for(int i = 0; i < 63; i ++) {
            ModelData d = modelData.get(i);
            ModelTestData md = new ModelTestData();
            md.setAge(d.getAge());
            md.setBmi(d.getBmi());
            md.setCholestrol(d.getCholestrol());
            md.setMaxPulseRate(d.getMaxPulseRate());
            md.setRestingPulseRate(d.getRestingPulseRate());
            md.setStepCount(d.getStepCount());
            md.setResult(d.getResult());
            modelTestDataRepository.save(md);
            modelDataRepository.delete(d);
        }
    }
}
