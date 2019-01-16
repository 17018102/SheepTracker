package com.example.pyuashin.se6schaapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DeviceOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_overview);

        final TextView tvDevice_ID = (TextView) findViewById(R.id.tvDevice_ID);
        final TextView tvUser_name = (TextView) findViewById(R.id.tvUser_name);
        final TextView tvCoordinates = (TextView) findViewById(R.id.tvCoordinates);
        final TextView tvOn_Feet_Status = (TextView) findViewById(R.id.tvOn_Feet_Status);

        boolean on_feet_status = getIntent().getExtras().getBoolean("on_feet_status");

        int device_id = getIntent().getExtras().getInt("device_id");
        int device_user_id = getIntent().getExtras().getInt("device_user_id");

        String coordinates = getIntent().getExtras().getString("coordinates");
        String user_name = getIntent().getExtras().getString("user_name");


        tvDevice_ID.setText("Current device ID: " + device_id);
        tvUser_name.setText("This device is owned by: " + device_user_id);
        tvCoordinates.setText("The current coordinates of the device are: " + coordinates);

        if(on_feet_status){
            tvOn_Feet_Status.setText("Het schaap staat op zijn poten.");
        }else{
            tvOn_Feet_Status.setText("Het schaap ligt op zijn rug");
        }
    }
}
