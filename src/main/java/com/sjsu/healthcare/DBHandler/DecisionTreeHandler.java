package com.sjsu.healthcare.DBHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sjsu.healthcare.Messaging.SmsSender;
import com.sjsu.healthcare.Model.*;
import com.sjsu.healthcare.Repository.ActivityDataRepository;
import com.sjsu.healthcare.Repository.NotificationRepository;
import com.sjsu.healthcare.Repository.PulseRateDataRepository;
import org.bson.types.ObjectId;
import org.joda.time.*;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sindhu Kashyap on 11/16/2015.
 */
public class DecisionTreeHandler {

    public HeartDiseaseData getPatientDataForDecisionTree(Patient patient)
    {
        //Get patient's data from the patient Object
        HeartDiseaseData heartDiseaseData = new HeartDiseaseData();
        heartDiseaseData.setBmi(patient.getBmi());
        heartDiseaseData.setCholestrol(patient.getCholestrol());
        heartDiseaseData.setPatientID(patient.getId());
        //calculate age from DOB using JODA
        Date patientDOB = patient.getDateOfBirth();
        Calendar cal = Calendar.getInstance();
        cal.setTime(patientDOB);
        LocalDate dob = new LocalDate(cal.get(Calendar.YEAR),  cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH));
        LocalDate date = new LocalDate();
        Period period = new Period(dob, date, PeriodType.yearMonthDay());
        int age = period.getYears();
        heartDiseaseData.setAge(age);
        //get step count for the day for the patient
        int stepCnt = new ActivityDataHandler().getActivityForTheDay(patient.getId(), new Date()).getStepCount();
        heartDiseaseData.setStepCount(stepCnt);//sets the step count
        //get maxpulserate and resting pulserate for that day from the database
        int []pulseData = getPulseRateData( patient.getId());
        //pulseData[0] is maxpulserate; pulseData[1] is mod of pulse rate;
        heartDiseaseData.setMaxPulseRate(pulseData[0]);
        heartDiseaseData.setRestingPulseRate(pulseData[1]);
        return heartDiseaseData;
    }

    //gets pulse rates for the day starting from 00:01 midnight till current time,
    //since the service runs on current day at around 11:45 PM, it collects for that day
    //calculates the max pulse rate for the day,
    // calculates and the max occuring pulserate for the day, which is the resting pulse rate
    public int[] getPulseRateData(String patientId)
    {
        DBCollection coll = null;
        int[] pulseRateValues = new int[2];
        int maxPulserate = 0;
        int maxOccuringKey =0;
        int maxOccuringValue = 0;
        //hashmap of pulserate and count of pulserate
        Map<Integer, Integer> mapPulseRate = new HashMap<>();
        try
        {
            DateTimeZone timeZone = DateTimeZone.forID("UTC");
            DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
            coll = MongoFactory.getConnection().getCollection("pulseRateData");
            BasicDBObject query = new BasicDBObject("patientId",patientId);
            query.append("date", new BasicDBObject("$gte", today.toDate()));
            DBCursor cur = coll.find(query);
            int i =0;
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                String p = obj.get("pulseRate").toString();
                int pulseRate = Integer.parseInt(p);
                if(pulseRate > maxPulserate)
                    maxPulserate = pulseRate;
                //initialise maxOccuring key and value to the first element
                if(i == 0)
                {
                    maxOccuringKey = pulseRate;
                    maxOccuringValue = 1;
                    i++;
                }
                if(mapPulseRate.containsKey(pulseRate))
                {
                    int count = mapPulseRate.get(pulseRate)+1;
                    mapPulseRate.put(pulseRate,count);
                    if(count > maxOccuringValue)
                    {
                        maxOccuringValue = count;
                        maxOccuringKey = pulseRate;
                    }
                }
                else
                {
                    mapPulseRate.put(pulseRate, 1);
                }
            }
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        pulseRateValues[0] = maxPulserate;
        pulseRateValues[1] = maxOccuringKey;
        return pulseRateValues;
    }

    public boolean sendHeartDiseaseDecisionNotification
    (HeartDiseaseData heartDiseaseData, Patient patient,  NotificationRepository notificationRepository)
    {
        List<CircleOfCareContact> circleOfCareContactList = patient.getCircleOfCare();
        Notification notification = new Notification();
        notification.setPatientId(patient.getId());
        notification.setCircleOfCare(circleOfCareContactList);
        //for(CircleOfCareContact c : circleOfCareContactList)
//        for(int i =0;i<circleOfCareContactList.size();i++)
//        {
//            notification.addCircleOfCarePerson(circleOfCareContactList.get(i));
//            //notification.addCircleOfCarePerson(c);
//        }
        notification.setCreatedAt();
        notification.setId(new ObjectId().toString());
        notification.setMessage(patient.getName() + " might be prone to heart disease ");
        notification.setNotificationType("HEARTDISEASE");
        boolean s = false;
        for(CircleOfCareContact c : circleOfCareContactList)
        {
            SmsSender smsSender = new SmsSender();
            s = smsSender.sendMessage(c.getPhoneNumber(), notification.getMessage(),
                    c.getName());
        }
        if(s)
            notification.setNotificationSent(true);
        notificationRepository.save(notification);
        return s;
    }
}
