package com.wirelesskings.data.mapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alberto on 24/10/2017.
 */

public class WK {
    /*
    *     "action": "login",
    "id": "8fg4656g-qwe415",
    "params": {
        "user": "amarturelo@gmail.com",
        "pass": "4654658q64q565456",
        "user_nauta": "amarturelo@nauta.cu"
    }
}

    * */

    public static String ACTION = "action";
    public static String ACTION_LOGIN = "login";
    public static String ACTION_UPDATE = "update";
    public static String ID = "id";
    public static String PARAMS = "params";
    public static String WK_USER = "user";
    public static String WK_PASS = "pass";
    public static String NAUTA_USER = "user_nauta";



    public static JSONObject update(String nauta_user, String wk_user, String wk_pass) throws JSONException {
        JSONObject node = new JSONObject();
        node.put(WK_USER,wk_user);
        node.put(WK_PASS,wk_pass);
        node.put(NAUTA_USER,nauta_user);
        return node;
    }
}
