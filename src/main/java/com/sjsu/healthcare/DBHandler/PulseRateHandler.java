package com.sjsu.healthcare.DBHandler;

import com.mongodb.*;
import com.sjsu.healthcare.Messaging.SmsSender;
import com.sjsu.healthcare.Model.CircleOfCareContact;
import com.sjsu.healthcare.Model.Notification;
import com.sjsu.healthcare.Model.Patient;
import com.sjsu.healthcare.Model.PulseRateData;
import com.sjsu.healthcare.Repository.NotificationRepository;
import com.sjsu.healthcare.Service.EmailService;
import org.bson.types.ObjectId;
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
public class PulseRateHandler
{
    DBCollection coll;
    SmsSender smsSender;

    public ArrayList<Integer> getPulseRateBetween(String patient, int days)
    {
        ArrayList<Integer> pulseRateList = new ArrayList<Integer>();
        //get today's date in UTC timezone
        DateTimeZone timeZone = DateTimeZone.forID("UTC");
        DateTime today = new DateTime(timeZone).withTimeAtStartOfDay();
        //get (today - days)'s date
        DateTime oldDate = today.minusDays(days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        try
        {
            coll = MongoFactory.getConnection().getCollection("pulseRateData");
            BasicDBObject query = new BasicDBObject("patientId",patient);
            System.out.println("in Handler : "+sdf.parse(sdf.format(oldDate.toDate())));
            query.append("date", new BasicDBObject("$gte", sdf.parse(sdf.format(oldDate.toDate()))));
            DBCursor cur = coll.find(query);
            while(cur.hasNext())
            {
                DBObject obj = cur.next();
                String p = obj.get("pulseRate").toString();
                pulseRateList.add(Integer.parseInt(p));
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
        return pulseRateList;
    }

    public int notifyAbnormalPulseRate(PulseRateData minPulseRateObject,PulseRateData maxPulseRateObject,
                                                Patient patient, NotificationRepository notificationRepository ) {
        int numberOfNotifications = 0;
        int minPulseVal = 0;
        int maxPulseVal = 0;
        if(minPulseRateObject != null || maxPulseRateObject != null)
        {
            if (minPulseRateObject != null) {
                minPulseVal = minPulseRateObject.getPulseRate();
                numberOfNotifications++;
            }
            if (maxPulseRateObject != null) {
                maxPulseVal = maxPulseRateObject.getPulseRate();
                numberOfNotifications++;
            }
            if (minPulseVal == maxPulseVal) {
                numberOfNotifications = 1;
            }
        }
        System.out.println("numberOfNotifications : "+numberOfNotifications);
        if(numberOfNotifications == 2) {
            determinePriorityLevelsForAbnormalPulseRate(minPulseRateObject, patient, notificationRepository);
            determinePriorityLevelsForAbnormalPulseRate(maxPulseRateObject, patient, notificationRepository);
        }
        else
        {
            if (minPulseRateObject != null && maxPulseRateObject == null) {
                System.out.println("minPulseRateObject :"+minPulseRateObject);
                System.out.println("maxPulseRateObject :"+maxPulseRateObject);
                determinePriorityLevelsForAbnormalPulseRate(minPulseRateObject, patient, notificationRepository);
            }

            if (maxPulseRateObject != null && minPulseRateObject == null) {
                determinePriorityLevelsForAbnormalPulseRate(maxPulseRateObject, patient, notificationRepository);
            }
        }
        return numberOfNotifications;
    }

    public boolean determinePriorityLevelsForAbnormalPulseRate(PulseRateData pulseRateData, Patient patient, NotificationRepository
            notificationRepository)
    {

            int pulseRate = pulseRateData.getPulseRate();
            String priority = "low";
            String message = "";
            if(pulseRate <= 10)
            {
                message = "Emergency!! " + patient.getName() +"'s pulse rate at time "+ pulseRateData.getDate() + " was " +
                        "too low at "+pulseRate+" RPM. Please attend to them immediately.";
                priority = "high";
            }
            else if(pulseRate > 10 && pulseRate <= 30)
            {
                message = patient.getName() +"'s pulse rate at time "+ pulseRateData.getDate() + " was " +
                        "low at "+pulseRate+" RPM. You might want to check if they are doing alright.";

            }
            else if(pulseRate >= 220 )
            {
                message = "Emergency!!" + patient.getName() +"'s pulse rate at time "+ pulseRateData.getDate() + " was " +
                        "too high at "+pulseRate+" RPM. Please attend to them immediately.";
                priority = "high";
            }
            else if(pulseRate >=190 && pulseRate < 220 )
            {
                message = patient.getName() +"'s pulse rate at time "+ pulseRateData.getDate() + "was " +
                        "high at "+pulseRate+" RPM. You might want to check if they are doing alright.";
            }
           return buildNotification(patient,pulseRateData, priority, message, notificationRepository);
    }

    public boolean buildNotification(Patient patient, PulseRateData pulseRateData, String priority, String message,
                                          NotificationRepository  notificationRepository      )
    {
        CircleOfCareContact circleOfCareContact = patient.getCircleOfCareContactByPriorityLevel(priority);
        if(circleOfCareContact == null)
            return false;
        //build the notification object
        Notification notification = new Notification();
        notification.setCircleOfCareContact(circleOfCareContact);
        notification.setMessage(message);
        notification.setId(new ObjectId().toString());
        notification.setNotificationSent(false);
        notification.notificationType = "HEARTRATE";
        //Notification.NotificationType type = Notification.NotificationType.HEARTRATE;
        notification.setPatientId(patient.getId());
        notification.setCreatedAt();
        System.out.print("Going to save notification :");
        System.out.println(notification.toString());
        Notification n = notificationRepository.save(notification);
       if(n == null)
       {
           return false;
       }

        if(!sendMessage(notification))
        {
            return false;
        }
        //set notification sent to true
        notification.setNotificationSent(true);
        notificationRepository.save(notification);
        String emailMessage = createEmailTemplate();
        emailMessage = emailMessage.replace("[circleOfCare]", circleOfCareContact.getName());
        emailMessage = emailMessage.replace("[Patient]", patient.getName());
        emailMessage = emailMessage.replace("[Time]", pulseRateData.getDate().toString());
        emailMessage = emailMessage.replace("[Priority]", priority);
        emailMessage = emailMessage.replace("[PulseRate]", String.valueOf(pulseRateData.getPulseRate()));
        System.out.println(emailMessage);
        EmailService.sendEmail(circleOfCareContact.getEmail(), emailMessage);
        return true;
    }

    private String createEmailTemplate() {
        StringBuilder str = new StringBuilder();
        str.append("Dear [circleOfCare],") ;
        str.append("\n\n") ;
        str.append("[Patient]'s pulse rate at time [Time] was [Priority] at [PulseRate] RPM. You might want to check if he/she is doing alright.");
        str.append("\n\n") ;
        str.append("Thanks,") ;
        str.append("\n") ;
        str.append("Healthcare App Team") ;
        return str.toString();
    }

    public boolean sendMessage(Notification notification)
    {
        smsSender = new SmsSender();
       return smsSender.sendMessage(notification.getCircleOfCareContact().getPhoneNumber(), notification.getMessage(),
                notification.getCircleOfCareContact().getName());
    }
}
