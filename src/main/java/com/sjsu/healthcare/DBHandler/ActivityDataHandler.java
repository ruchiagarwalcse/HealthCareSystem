package com.sjsu.healthcare.DBHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sjsu.healthcare.Model.ActivityData;
import com.sjsu.healthcare.Repository.ActivityDataRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sindhu Kashyap on 11/4/2015.
 */
public class ActivityDataHandler {

    DBCollection coll;
    public List<ActivityData> getActivityListFromRange(String patientId, int days)
    {
        //get today's date in UTC timezone
        DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        //get (today - days)'s date
        DateTime oldDate = today.minusDays(days);
        List<ActivityData> activityDataList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try
        {
            coll = MongoFactory.getConnection().getCollection("activityData");
            BasicDBObject query = new BasicDBObject("patientId",patientId);
            query.append("date", new BasicDBObject("$gte", sdf.parse(sdf.format(oldDate.toDate()))));
            DBCursor cur = coll.find(query);
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                int stepCnt = Integer.parseInt(obj.get("stepCount").toString());
                String startTime = obj.get("date").toString();
                String pId = obj.get("patientId").toString();
                String id = obj.get("_id").toString();
                System.out.println("Start time :"+startTime);
                ActivityData activityData = new ActivityData();
                activityData.setPatientId(pId);
                activityData.setStepCount(stepCnt);
                activityData.setId(id);
                activityData.setDate(new Date(startTime));
                activityDataList.add(activityData);
            }
        }
        catch (java.text.ParseException ex)
        {
            ex.printStackTrace();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
       
        return activityDataList;


    }

    public ActivityData getActivityForTheDay(String patientId, Date date)
    {
        //Since Mongodb stores date and time in UTC, convert time to UTC to query
        DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        ActivityData activityData = null;
        try
        {
            coll = MongoFactory.getConnection().getCollection("activityData");
            BasicDBObject query = new BasicDBObject("patientId",patientId);
            query.append("date", new BasicDBObject("$gte", today.toDate()));
            DBObject obj = coll.findOne(query);
            if(obj != null)
            {
                int stepCnt = Integer.parseInt(obj.get("stepCount").toString());
                String startTime = obj.get("date").toString();
                String pId = obj.get("patientId").toString();
                String id = obj.get("_id").toString();
                System.out.println("Start time :"+startTime);
                activityData = new ActivityData();
                activityData.setPatientId(pId);
                activityData.setStepCount(stepCnt);
                activityData.setId(id);
                activityData.setDate(new Date(startTime));
            }
            // activityData = activityDataRepository.findByPatientIdAndDate(patientId, sdf.parse(sdf.format(date)));

        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return activityData;
    }

    public ActivityData getActivityForLastTheDay(String patientId, Date date)
    {
        //Since Mongodb stores date and time in UTC, convert time to UTC to query
        DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        DateTime lastDay = today.minusDays(1);
        ActivityData activityData = null;
        try
        {
            coll = MongoFactory.getConnection().getCollection("activityData");
            BasicDBObject query = new BasicDBObject("patientId",patientId);
            query.append("date", new BasicDBObject("$lt", today.toDate()).append("$gte", lastDay.toDate()));
            DBObject obj = coll.findOne(query);
            if(obj != null)
            {
                int stepCnt = Integer.parseInt(obj.get("stepCount").toString());
                String startTime = obj.get("date").toString();
                String pId = obj.get("patientId").toString();
                String id = obj.get("_id").toString();
                System.out.println("Start time :"+startTime);
                activityData = new ActivityData();
                activityData.setPatientId(pId);
                activityData.setStepCount(stepCnt);
                activityData.setId(id);
                activityData.setDate(new Date(startTime));
            }
            // activityData = activityDataRepository.findByPatientIdAndDate(patientId, sdf.parse(sdf.format(date)));

        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return activityData;
    }

    public List<ActivityData> getActivityForLastDayAllPatients()
    {
        //Since Mongodb stores date and time in UTC, convert time to UTC to query
        DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        DateTime lastDay = today.minusDays(1);
        ActivityData activityData = null;
        List<ActivityData> activityDataList = new ArrayList<ActivityData>();
        try
        {
            coll = MongoFactory.getConnection().getCollection("activityData");
            BasicDBObject query = new BasicDBObject("date", new BasicDBObject("$lt", today.toDate()).append("$gte", lastDay.toDate()));
            //query.append("date", new BasicDBObject("$gte", lastDay.toDate()));
            //DBObject obj = coll.findOne(query);
            DBCursor cur = coll.find(query);
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                activityData = new ActivityData();
                activityData.setPatientId(obj.get("patientId").toString());
                activityData.setId(obj.get("_id").toString());
                activityData.setStepCount((Integer)obj.get("stepCount"));
                activityData.setDate(new Date(obj.get("date").toString()));
                activityDataList.add(activityData);
            }

        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return activityDataList;
    }
}
