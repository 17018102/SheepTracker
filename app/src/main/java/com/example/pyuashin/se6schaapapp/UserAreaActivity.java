package com.example.pyuashin.se6schaapapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvWelcomeMessage = (TextView) findViewById(R.id.tvWelcomeMessage);
        final TextView tvUserInfo = (TextView) findViewById(R.id.tvUserInfo);
        final Button btDeviceOverview = (Button) findViewById(R.id.bt_DeviceOverview);

        final Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String userInformation = "User panel";

        String message = name + " , welcome to your user area";
        tvWelcomeMessage.setText(message);
        tvName.setText(name);
        tvUserInfo.setText(userInformation);

        btDeviceOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            //Forward the user to the device overview page with the retrieved device data
                            //Check if data was successfully retrieved
                            if (success) {
                                boolean on_feet_status = jsonResponse.getBoolean("on_feet_status");

                                int device_id = Integer.parseInt(jsonResponse.getString("device_id"));
                                int device_user_id = Integer.parseInt(jsonResponse.getString("device_user_id"));

                                String coordinates = jsonResponse.getString("location");
                                String user_name = jsonResponse.getString("user_name");

                                Intent NewAcvitity = new Intent(UserAreaActivity.this, DeviceOverviewActivity.class);
                                NewAcvitity.putExtra("on_feet_status", on_feet_status);
                                NewAcvitity.putExtra("device_id", device_id);
                                NewAcvitity.putExtra("device_user_id", user_name);
                                NewAcvitity.putExtra("coordinates", coordinates);
                                NewAcvitity.putExtra("device_user_id", device_user_id);
                                NewAcvitity.putExtra("user_name", user_name);

                                UserAreaActivity.this.startActivity(NewAcvitity);
                            } else {
                                //Shows the user a message telling them their log in attempt failed and allows them to retry
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("Device data couldn't be retrieved")
                                        .setNegativeButton("OK", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                DeviceDataRequest deviceDataRequest = new DeviceDataRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                queue.add(deviceDataRequest);
            }
        });
    }
}
