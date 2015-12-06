package com.sjsu.healthcare.Service;

import com.relayrides.pushy.apns.ApnsEnvironment;
import com.relayrides.pushy.apns.PushManager;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.PushManagerConfiguration;


@RestController
public class ApnTestService {
    @RequestMapping(value = "api/send", method = RequestMethod.GET)
    public String sentNotification() {
        PushManager<SimpleApnsPushNotification> pushManager = null;

        try {
            pushManager =
                    new PushManager<SimpleApnsPushNotification>(
                            ApnsEnvironment.getSandboxEnvironment(),
                            SSLContextUtil.createDefaultSSLContext("/Users/piyushmittal/Desktop/healthcare.p12", "password"),
                            null, // Optional: custom event loop group
                            null, // Optional: custom ExecutorService for calling listeners
                            null, // Optional: custom BlockingQueue implementation
                            new PushManagerConfiguration(),
                            "ExamplePushManager");

            pushManager.start();
            final byte[] token = TokenUtil.tokenStringToByteArray(
                    "<f59066ad b944764e deda8777 8ad04f2d 9386a87b ee02d7ca 2c519ecd e0fceac9>");

            final ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();

            payloadBuilder.setAlertBody("Ring ring, Neo.");
            payloadBuilder.setSoundFileName("ring-ring.aiff");

            final String payload = payloadBuilder.buildWithDefaultMaximumLength();

            pushManager.getQueue().put(new SimpleApnsPushNotification(token, payload));
            return "Success";
        }
        catch(Exception e) {
            e.printStackTrace();
            return "Error";
        }    finally {
            try {
                //pushManager.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }
        //return "";
    }
}
