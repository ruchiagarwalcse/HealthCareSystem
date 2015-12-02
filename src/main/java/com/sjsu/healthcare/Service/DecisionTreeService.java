package com.sjsu.healthcare.Service;

import com.sjsu.healthcare.DBHandler.DecisionTreeHandler;
import com.sjsu.healthcare.DecisionTree.BadDecisionException;
import com.sjsu.healthcare.DecisionTree.DecisionTree;
import com.sjsu.healthcare.DecisionTree.UnknownDecisionException;
import com.sjsu.healthcare.Model.*;
import com.sjsu.healthcare.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import static org.junit.Assert.*;
import java.util.*;

@RestController
public class DecisionTreeService {

    @Autowired
    private ModelDataRepository modelDataRepository;


    @Autowired
    private ModelTestDataRepository modelTestDataRepository;

    @Autowired private PatientRepository patientRepository;
    @Autowired private NotificationRepository notificationRepository;
    private DecisionTree tree;

    @PostConstruct
    private void createTree()
    {
        try
        {
            DecisionTree decisionTree = new DecisionTree();
            decisionTree.setAttributes(new String[]{"age", "cholestrol", "maxPulseRate", "restingPulseRate, bmi, stepCount"});
            List<ModelData> data = modelDataRepository.findAll();
            for(ModelData d: data) {
                decisionTree.addExample(new String[]{String.valueOf(d.getAge()), String.valueOf(d.getCholestrol()), String.valueOf(d.getMaxPulseRate()), String.valueOf(d.getRestingPulseRate()), String.valueOf(d.getBmi()), String.valueOf(d.getStepCount())}, d.isResult());
            }
            tree = decisionTree;
        } catch (UnknownDecisionException e) {
            fail();
            tree = new DecisionTree();
        }
    }

    public String testTree() throws BadDecisionException{
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


    //@RequestMapping(value = "api/testDecision", method = RequestMethod.GET)
    @Scheduled(cron="0 45 23 * * ?")
    public void decisionService(){
        List<Patient> patients = patientRepository.findAll();
        for(Patient p: patients){
            if(!p.getHasHeartDisease()){
               try {
                        decide(p);
                    }   catch (Exception e) {
                        System.out.println("Error occurred but continue for other patients...");
                    }
            }
        }
    }

    /*@RequestMapping(value = "api/testDecision", method = RequestMethod.GET)
    public List<HeartDiseaseData> decisionService(){
        List<Patient> patients = patientRepository.findAll();
        List<HeartDiseaseData> h  = new ArrayList<HeartDiseaseData>();
            for(Patient p: patients){
                if(!p.getHasHeartDisease()){
                    try {
                        h.add(decide(p));
                    }   catch (Exception e) {
                        System.out.println("Error occurred but continue for other patients...");
                    }
                }
            }
        return h;
    }*/

    public Boolean getDecision(HeartDiseaseData heartDiseaseData  )
            throws BadDecisionException{
        Boolean decision;
        Map<String, String> testCase = new HashMap<String, String>();
        testCase.put("age", String.valueOf(heartDiseaseData.getAge()));
        testCase.put("cholestrol", String.valueOf(heartDiseaseData.getCholestrol()));
        testCase.put("maxPulseRate", String.valueOf(heartDiseaseData.getMaxPulseRate()));
        testCase.put("restingPulseRate", String.valueOf(heartDiseaseData.getRestingPulseRate()));
        testCase.put("bmi", String.valueOf(heartDiseaseData.getBmi()));
        testCase.put("stepCount", String.valueOf(heartDiseaseData.getStepCount()));
        decision = tree.apply(testCase);
        return decision;
    }

    /*@RequestMapping(value = "api/createTree", method = RequestMethod.GET)
    public DecisionTree create() {
        try {
            DecisionTree decisionTree = createTree();
            return decisionTree;
        }catch (Exception e) {
            return new DecisionTree();
        }
    }*/

    @RequestMapping(value = "api/testTree", method = RequestMethod.GET)
    public String test() {
        try {
            return testTree();
        }catch (Exception e) {
            e.printStackTrace();
           return "Error";
        }
    }

    //needs age, cholestrol, bmi, maxpulserate, restingpulserate, bmi, stepcount
    @RequestMapping(value = "api/heartcondition/decision/{pid}", method = RequestMethod.GET)
    public ResponseEntity getDecisionForPatient(@PathVariable("pid") String patientId)
    {
        Patient patient = patientRepository.findById(patientId);
        if(patient == null)
        {
            return new ResponseEntity("No Patient found for the given Patient ID", HttpStatus.NOT_FOUND);
        }
        DecisionTreeHandler handler = new DecisionTreeHandler();
        //get all patient data required for determining heart disease
        HeartDiseaseData heartDiseaseData = handler.
                getPatientDataForDecisionTree(patient);
        //call getDecision which gets the decision for the patient
        boolean decision = false;
        try {
            decision = getDecision(heartDiseaseData);
            heartDiseaseData.setDecision(decision);
        } catch (BadDecisionException e) {
            e.printStackTrace();
        }
        if(decision)
        {
            System.out.println("Patient has heart disease , going to send notification...");
            boolean sent =
                    handler.sendHeartDiseaseDecisionNotification(heartDiseaseData, patient, notificationRepository);
            if(sent)
                heartDiseaseData.setCircleOfCareNotified(true);
        }

        return new ResponseEntity(heartDiseaseData, HttpStatus.OK);
    }
    /*@Scheduled(cron="0 58 23 * * ?")
    public void testScheduler(){
        System.out.println("Hello:" + new Date());
    }*/

    public HeartDiseaseData decide(Patient patient) {
        DecisionTreeHandler handler = new DecisionTreeHandler();
        //get all patient data required for determining heart disease
        HeartDiseaseData heartDiseaseData = handler.
                getPatientDataForDecisionTree(patient);
        //call getDecision which gets the decision for the patient
        boolean decision = false;
        try {
            decision = getDecision(heartDiseaseData);
            heartDiseaseData.setDecision(decision);
            patient.setHasHeartDisease(decision);
            patientRepository.save(patient);
        } catch (BadDecisionException e) {
            e.printStackTrace();
        }
        if(decision)
        {
            System.out.println("Patient has heart disease , going to send notification...");
            boolean sent =
                    handler.sendHeartDiseaseDecisionNotification(heartDiseaseData, patient, notificationRepository);
            if(sent)
                heartDiseaseData.setCircleOfCareNotified(true);
            for (CircleOfCareContact circleOfCareContact: patient.getCircleOfCare()) {
                String emailMessage = createMessage(heartDiseaseData, patient, false);
                emailMessage = emailMessage.replace("[circleOfCare]", circleOfCareContact.getName());
                EmailService.sendEmail(circleOfCareContact.getEmail(), emailMessage);
            }
            String emailMessage = createMessage(heartDiseaseData, patient, true);
            EmailService.sendEmail(patient.getEmail(), emailMessage);
        }
        return  heartDiseaseData;
    }

    //
    @RequestMapping(value = "api/heartcondition/{pid}/feedbackloop", method = RequestMethod.GET)
    public ResponseEntity getFeedBackLoopDecision(@PathVariable("pid") String patientId)
    {
        Patient patient = patientRepository.findById(patientId);
        if(patient == null)
        {
            return new ResponseEntity("No Patient found for the given Patient ID", HttpStatus.NOT_FOUND);
        }
        patient.setHasHeartDisease(false);
        DecisionTreeHandler handler = new DecisionTreeHandler();
        //get all patient data required for determining heart disease
        HeartDiseaseData heartDiseaseData = handler.
                getPatientDataForDecisionTree(patient);
        //insert the heart disease data into Model Data
        ModelData data =insertPatientDataIntoModelData(heartDiseaseData);
        if(data == null)
        {
            new ResponseEntity("Error inserting patient heart data into Model Data", HttpStatus.EXPECTATION_FAILED);
        }
        //rebuild the tree with the new insertion in model data
        createTree();

        //call getDecision which gets the decision for the patient
        boolean decision = false;
        try {
            decision = getDecision(heartDiseaseData);
            heartDiseaseData.setDecision(decision);
        } catch (BadDecisionException e) {
            e.printStackTrace();
        }
        if(decision)
        {
            System.out.println("Patient has heart disease , going to send notification...");
            return new ResponseEntity("Oops! Looks like you still have a heart problem!", HttpStatus.OK);
        }
        return new ResponseEntity("Sorry, looks like our previous decision was wrong. Hurray, you do" +
                " not have any heart disease!", HttpStatus.OK);
    }

    private ModelData insertPatientDataIntoModelData(HeartDiseaseData heartDiseaseData) {

        int result = 0;
        ModelData md = new ModelData(new ObjectId().toString(), heartDiseaseData.getAge(), heartDiseaseData.getCholestrol(),
                heartDiseaseData.getMaxPulseRate(), heartDiseaseData.getRestingPulseRate(), heartDiseaseData.getBmi(),
                heartDiseaseData.getStepCount(), result);

        return modelDataRepository.save(md);
    }
    private String createMessage(HeartDiseaseData heartDiseaseData, Patient patient, boolean forPatient) {
        StringBuilder str = new StringBuilder();
        if (forPatient) {
            str.append("Dear [patient],") ;
            str.append("\n\n") ;
            str.append("We have predicted from our system that you might be prone to heart problems based on the following health parameters.");
        } else {
            str.append("Dear [circleOfCare],") ;
            str.append("\n\n") ;
            str.append("We have predicted from our system that [patient]'s might be prone to heart problems based on the following health parameters.");
        }
        str.append("\n\n") ;
        str.append("Cholestrol : [cholestrol]");
        str.append("\n") ;
        str.append("Age : [age]");
        str.append("\n") ;
        str.append("BMI : [bmi]");
        str.append("\n") ;
        str.append("Maximum pulse rate for today : [maxPulseRate]");
        str.append("\n") ;
        str.append("Resting pulse rate for today : [restingPulseRate]");
        str.append("\n") ;
        str.append("Step count for today : [stepCount]");
        str.append("\n\n") ;
        if (forPatient) {
            str.append("You might want to make sure if everything alright.");
        } else {
            str.append("You might want to make sure if he/she is doing alright.");
        }

        str.append("\n\n") ;
        str.append("Thanks,") ;
        str.append("\n") ;
        str.append("Healthcare App Team") ;

        String emailMessage = str.toString();
        emailMessage = emailMessage.replace("[patient]", patient.getName());
        emailMessage = emailMessage.replace("[cholestrol]", String.valueOf((heartDiseaseData.getCholestrol())));
        emailMessage = emailMessage.replace("[age]", String.valueOf((heartDiseaseData.getAge())));
        emailMessage = emailMessage.replace("[bmi]", String.valueOf((heartDiseaseData.getBmi())));
        emailMessage = emailMessage.replace("[maxPulseRate]", String.valueOf((heartDiseaseData.getMaxPulseRate())));
        emailMessage = emailMessage.replace("[restingPulseRate]", String.valueOf((heartDiseaseData.getRestingPulseRate())));
        emailMessage = emailMessage.replace("[stepCount]", String.valueOf((heartDiseaseData.getStepCount())));
        System.out.println(emailMessage);
        return emailMessage;
    }
}