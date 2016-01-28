package com.horizon.cantor.util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class JSONUtils {

    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSONObject.toJSONString(obj);
    }

    public static JSONObject toJSON(Object obj) {
        if (obj == null) {
            return null;
        }
        return (JSONObject) JSONObject.toJSON(obj);
    }

    public static JSONArray parseArray(String obj) {
        if (obj == null) {
            return null;
        }
        return JSON.parseArray(obj);
    }

    public static JSONObject parseObject(String obj) {
        if (obj == null) {
            return null;
        }
        return JSON.parseObject(obj);
    }

}