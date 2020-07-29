package com.sangdaero.walab.common.push;

import org.json.JSONObject;

public class MakeJSON {

    public String makePush(String token, String title, String body) {
        JSONObject obj = new JSONObject();

        obj.put("to", "ExponentPushToken[" + token + "]");
        obj.put("sound", "default");
        obj.put("title", title);
        obj.put("body", body);

        JSONObject data = new JSONObject();
        data.put("data", "goes here");
        obj.put("data", data);

        return obj.toString();
    }
}
