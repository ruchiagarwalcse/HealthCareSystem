package com.sjsu.healthcare.DBHandler;

import com.sjsu.healthcare.Model.Medication;
import com.sjsu.healthcare.Repository.PatientRepository;
import com.sjsu.healthcare.Service.ApnTestService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Minutes;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sindhu Kashyap on 12/5/2015.
 */
public class MedicationHandler
{

    public void getPushObject(PatientRepository repository, ArrayList<Medication> medList)
    {
        for(Medication medication : medList)
        {
//            String []timeOfMed = medication.getTime().split(":");
//            System.out.println("time:hr"+timeOfMed[0] + "min:"+timeOfMed[1]);
            DateTimeZone timeZone = DateTimeZone.forID("UTC");

            //DateTime now = new DateTime(timeZone).withTimeAtStartOfDay();
            Date medicineTime = medication.getTime();
            DateTime medTime = new DateTime(medicineTime).plusHours(8);
            DateTime curTime = new DateTime();
            System.out.println("medtime:"+medTime);
            System.out.println("curTime:"+curTime);
            //int min = Minutes.minutesBetween(medTime,curTime).getMinutes();
            int min = (medTime.getMillisOfDay()-curTime.getMillisOfDay())/(1000*60);
            System.out.println("min:"+min);
            if(min >=0 && min<=30)
            {
                String msg = "Your medicine "+medication.getName()+ " is due soon at "+medication.getTime();
                new ApnTestService().sentNotification(msg);
            }
        }
    }
}
