package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.DecisionTree.BadDecisionException;
import com.sjsu.healthcare.DecisionTree.DecisionTree;
import com.sjsu.healthcare.DecisionTree.UnknownDecisionException;
import com.sjsu.healthcare.Model.ModelData;
import com.sjsu.healthcare.Model.ModelTestData;
import com.sjsu.healthcare.Repository.ModelDataRepository;
import com.sjsu.healthcare.Repository.ModelTestDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static org.junit.Assert.*;
import java.util.*;

@RestController
public class DecisionTreeService {

    @Autowired
    private ModelDataRepository modelDataRepository;

    @Autowired
    private ModelTestDataRepository modelTestDataRepository;

    private DecisionTree createTree() {
        try {
            DecisionTree decisionTree = new DecisionTree();
            decisionTree.setAttributes(new String[]{"age", "cholestrol", "maxPulseRate", "restingPulseRate, bmi, stepCount"});
            List<ModelData> data = modelDataRepository.findAll();
            for(ModelData d: data) {
                decisionTree.addExample(new String[]{String.valueOf(d.getAge()), String.valueOf(d.getCholestrol()), String.valueOf(d.getMaxPulseRate()), String.valueOf(d.getRestingPulseRate()), String.valueOf(d.getBmi()), String.valueOf(d.getStepCount())}, d.isResult());
            }
            return decisionTree;
        } catch (UnknownDecisionException e) {
            fail();
            return new DecisionTree();
        }
    }

    public String testTree() throws BadDecisionException{
        DecisionTree tree = createTree();
        List<ModelTestData> data = modelTestDataRepository.findAll();
        int right = 0, wrong = 0;
        for(ModelTestData d: data) {
            Map<String, String> testCase = new HashMap<String, String>();
            testCase.put("age", String.valueOf(d.getAge()));
            testCase.put("cholestrol", String.valueOf(d.getCholestrol()));
            testCase.put("maxPulseRate", String.valueOf(d.getMaxPulseRate()));
            testCase.put("restingPulseRate", String.valueOf(d.getRestingPulseRate()));
            testCase.put("bmi", String.valueOf(d.getBmi()));
            testCase.put("stepCount", String.valueOf(d.getStepCount()));
            Boolean result = tree.apply(testCase);
            if (result == d.isResult()){
                  right++;
            }   else {
                wrong++;

                //Feedback
                ModelData md = modelDataRepository.findById(d.getId());
                if (md == null) {
                    md = new ModelData();
                    md.setId(d.getId());
                    md.setAge(d.getAge());
                    md.setBmi(d.getBmi());
                    md.setCholestrol(d.getCholestrol());
                    md.setMaxPulseRate(d.getMaxPulseRate());
                    md.setRestingPulseRate(d.getRestingPulseRate());
                    md.setStepCount(d.getStepCount());
                    md.setResult(d.getResult());
                    modelDataRepository.save(md);
                }
            }
        }

        String str =  "Right Decision Count: " + right;
        str = str + " & Wrong Decision Count: " + wrong;
        return str;
    }

    public Boolean getDecision(int age, int cholestrol, int maxPulseRate, int restingPulseRate, int bmi, int stepCount) throws BadDecisionException{
        Boolean decision;
        DecisionTree tree = createTree();
        Map<String, String> testCase = new HashMap<String, String>();
        testCase.put("age", String.valueOf(age));
        testCase.put("cholestrol", String.valueOf(cholestrol));
        testCase.put("maxPulseRate", String.valueOf(maxPulseRate));
        testCase.put("restingPulseRate", String.valueOf(restingPulseRate));
        testCase.put("bmi", String.valueOf(bmi));
        testCase.put("stepCount", String.valueOf(stepCount));
        decision = tree.apply(testCase);
        return decision;
    }

    @RequestMapping(value = "api/createTree", method = RequestMethod.GET)
    public DecisionTree create() {
        try {
            DecisionTree decisionTree = createTree();
            return decisionTree;
        }catch (Exception e) {
            return new DecisionTree();
        }
    }

    @RequestMapping(value = "api/testTree", method = RequestMethod.GET)
    public String test() {
        try {
            return testTree();
        }catch (Exception e) {
            e.printStackTrace();
           return "Error";
        }
    }

}