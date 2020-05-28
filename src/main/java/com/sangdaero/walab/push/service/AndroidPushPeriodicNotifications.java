package com.sangdaero.walab.push.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AndroidPushPeriodicNotifications {

    public static String PeriodicNotificationJson() throws JSONException {
        LocalDate localDate = LocalDate.now();

        String sampleData[] = {"eVk6Q63g65g:APA91bFwKYybtCRrJI5QdSGlGOitNL42zermURHjdWAWCg6VAIsrggMlaJAoVXPD_vu0xhOU5efVToJzFFDzuG8qag2PdWmWFvTgU1Qp4m-kIyhR9Ss4Q7aQ9MPH80nzKZ-M3WT6lxlt"};

        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<sampleData.length; i++){
            tokenlist.add(sampleData[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title","success!");
        notification.put("body","You have been registered for the activity you requested.");
//        notification.put("body","Today is "+localDate.getDayOfWeek().name()+"!");

        body.put("notification", notification);

        System.out.println(body.toString());

        return body.toString();
    }
}