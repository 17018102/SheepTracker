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
}
