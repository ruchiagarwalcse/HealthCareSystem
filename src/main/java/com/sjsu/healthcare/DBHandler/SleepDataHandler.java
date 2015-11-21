package com.sjsu.healthcare.DBHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sjsu.healthcare.Model.SleepData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

}
