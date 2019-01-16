package com.example.pyuashin.se6schaapapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeviceDataRequest extends StringRequest {
    private static final String DEVICE_DATA_REQUEST_URL = "https://crinkly-shows.000webhostapp.com/DeviceDataRequest.php";
    //private Map<String, String> params;

    public DeviceDataRequest(Response.Listener<String> listener){
        //Fetch data from the database
        super(Method.POST, DEVICE_DATA_REQUEST_URL, listener, null);
    }

    /*public DeviceDataRequest(String device_id, String location, String on_feet_status, String device_user_id, String user_name, Response.Listener<String> listener){
        super(Request.Method.POST, DEVICE_DATA_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("device_id", device_id);
        params.put("location", location);
        params.put("on_feet_status", on_feet_status);
        params.put("device_user_id", device_user_id);
        params.put("user_name", user_name);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }*/
}
