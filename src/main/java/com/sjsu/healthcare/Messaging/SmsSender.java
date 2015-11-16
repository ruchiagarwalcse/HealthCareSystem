package com.sjsu.healthcare.Messaging;

import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
/**
 * Created by Sindhu Kashyap on 11/15/2015.
 */
public class SmsSender {

    public static final String ACCOUNT_SID = "ACc49b78007b91792bd0e6a3b4a0981648";
    public static final String AUTH_TOKEN = "6f5e2ce35ca051fd92085378ed7d7517";
    public static final String fromNumber = "+15627324560";
    public static final String toNumber = "+14157128116";

    public boolean sendMessage(long phoneNumber, String message, String toName)
    {
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
        Account account = client.getAccount();
        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
       // params.add(new BasicNameValuePair("To", "+1"+phoneNumber)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("To", toNumber));
        params.add(new BasicNameValuePair("From", fromNumber)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("Body", message));
        try {
            Message sms = messageFactory.create(params);
        } catch (TwilioRestException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
