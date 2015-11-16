package com.sjsu.healthcare.DBHandler;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sjsu.healthcare.Model.ActivityData;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sindhu Kashyap on 11/4/2015.
 */
public class ActivityDataHandler {

    DBCollection coll;
    public List<ActivityData> getActivityListFromRange(String patientId, Date date)
    {
        List<ActivityData> activityDataList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try
        {
            coll = MongoFactory.getConnection().getCollection("activityData");
            BasicDBObject query = new BasicDBObject("patientId",patientId);
            query.append("date", new BasicDBObject("$gte", sdf.parse(sdf.format(date))));
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
}
