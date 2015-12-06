package com.sjsu.healthcare.DBHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sjsu.healthcare.Model.ActivityData;
import com.sjsu.healthcare.Model.SleepData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Sindhu Kashyap on 11/3/2015.
 */
public class SleepDataHandler
{
    DBCollection coll;
    public ArrayList<SleepData> getSleepEfficiency(String patientId, int days)
    {
        ArrayList<SleepData> sleepDataList = new ArrayList<>();
        //get today's date in UTC timezone
        DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        //get (today - days)'s date
        DateTime oldDate = today.minusDays(days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try
        {
            coll = MongoFactory.getConnection().getCollection("sleepData");
            BasicDBObject query = new BasicDBObject("patientId",patientId);
            query.append("startTime", new BasicDBObject("$gte", sdf.parse(sdf.format(oldDate.toDate()))));
            DBCursor cur = coll.find(query);
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                SleepData sleep = new SleepData();
                sleep.setEfficiency(Integer.parseInt(obj.get("efficiency").toString()));
                String startTime = obj.get("startTime").toString();
                System.out.println("Start time :"+startTime);
                sleep.setStartTime(new Date(startTime));
                sleepDataList.add(sleep);
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
        return sleepDataList;
    }

    public List<SleepData> getSleepDataForLastDayAllPatients()
    {
        //Since Mongodb stores date and time in UTC, convert time to UTC to query
        ArrayList<SleepData> sleepDataList = new ArrayList<SleepData>();
        //get today's date in UTC timezone
        //DateTimeZone timeZone = DateTimeZone.forID("UTC");
        //DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        Date today1 = cal.getTime();
        DateTime today = new DateTime(today1).withTimeAtStartOfDay();
        DateTime lastDay = today.minusDays(1);
        SleepData sleepData = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        try
        {
            coll = MongoFactory.getConnection().getCollection("sleepData");
            BasicDBObject query = new BasicDBObject("startTime", new BasicDBObject("$lt", today.toDate()).append("$gte", lastDay.toDate()));
            DBCursor cur = coll.find(query);
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                sleepData = new SleepData();
                sleepData.setId(obj.get("_id").toString());
                sleepData.setPatientId(obj.get("patientId").toString());
                sleepData.setStartTime(new Date(obj.get("startTime").toString()));
                sleepData.setAwakeningsCount((Integer) obj.get("awakeningsCount"));
                sleepData.setMinutesAsleep((Integer) obj.get("minutesAsleep"));
                sleepData.setMinutesAwake((Integer) obj.get("minutesAwake"));
                sleepData.setTimeInBed((Integer) obj.get("timeInBed"));
                sleepData.setEfficiency((Integer)obj.get("efficiency"));
                sleepDataList.add(sleepData);
            }

        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return sleepDataList;
    }

    public SleepData getSleepDataForLastDay()
    {
        //Since Mongodb stores date and time in UTC, convert time to UTC to query
        ArrayList<SleepData> sleepDataList = new ArrayList<SleepData>();
        //get today's date in UTC timezone
        //DateTimeZone timeZone = DateTimeZone.forID("UTC");
        //DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        Date today1 = cal.getTime();
        DateTime today = new DateTime(today1).withTimeAtStartOfDay();
        DateTime lastDay = today.minusDays(1);
        SleepData sleepData = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        try
        {
            coll = MongoFactory.getConnection().getCollection("sleepData");
            BasicDBObject query = new BasicDBObject("startTime", new BasicDBObject("$lt", today.toDate()).append("$gte", lastDay.toDate()));
            DBCursor cur = coll.find(query);
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                sleepData = new SleepData();
                sleepData.setId(obj.get("_id").toString());
                sleepData.setPatientId(obj.get("patientId").toString());
                sleepData.setStartTime(new Date(obj.get("startTime").toString()));
                sleepData.setAwakeningsCount((Integer) obj.get("awakeningsCount"));
                sleepData.setMinutesAsleep((Integer) obj.get("minutesAsleep"));
                sleepData.setMinutesAwake((Integer) obj.get("minutesAwake"));
                sleepData.setTimeInBed((Integer) obj.get("timeInBed"));
                sleepData.setEfficiency((Integer)obj.get("efficiency"));
                sleepDataList.add(sleepData);
            }

        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        sleepData = sleepDataList.get(0);
        return sleepData;
    }
}
