package com.example.pyuashin.se6schaapapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.StringTokenizer;

public class DeviceOverviewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int uniqueID = 1738;
    private String coordinates;
    private MapView mMapView;
    private MarkerOptions testMarker;
    private Marker sheepLocation;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    public DeviceOverviewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_overview);

        final TextView tvDevice_ID = findViewById(R.id.tvDevice_ID);
        final TextView tvUser_name = findViewById(R.id.tvUser_name);
        final TextView tvCoordinates = findViewById(R.id.tvCoordinates);
        final TextView tvOn_Feet_Status = findViewById(R.id.tvOn_Feet_Status);

        mMapView = findViewById(R.id.device_list_map);

        boolean on_feet_status = getIntent().getExtras().getBoolean("on_feet_status");
        int device_id = getIntent().getExtras().getInt("device_id");
        int device_user_id = getIntent().getExtras().getInt("device_user_id");
        coordinates = getIntent().getExtras().getString("coordinates");
        String user_name = getIntent().getExtras().getString("user_name");

        tvDevice_ID.setText("Current device ID: " + device_id);
        tvUser_name.setText("This device is owned by: " + user_name + " and their ID is: " + device_user_id);
        tvCoordinates.setText("The current coordinates of the device are: " + coordinates);

        if(on_feet_status){
            tvOn_Feet_Status.setText("Het schaap staat op zijn poten.");
        }else{
            tvOn_Feet_Status.setText("Het schaap ligt op zijn rug");
        }

        //google maps part start
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        //google maps part end
    }

    //Fetches data from the database and calls the update method
    private void getData(Button testButton){
        testButton.setOnClickListener(new View.OnClickListener() {
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
                                coordinates = jsonResponse.getString("location");
                                String user_name = jsonResponse.getString("user_name");

                                onUpdate(device_id, user_name, coordinates, on_feet_status, device_user_id);
                            } else {
                                //Shows the user a message telling them their log in attempt failed and allows them to retry
                                AlertDialog.Builder builder = new AlertDialog.Builder(DeviceOverviewActivity.this);
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
                RequestQueue queue = Volley.newRequestQueue(DeviceOverviewActivity.this);
                queue.add(deviceDataRequest);
            }
        });
    }

    //Updates the labels to their corresponding values and sends a notification if required
    //(This was supposed updated automatically based on database changes but couldn't figure out how to make it work)
    private void onUpdate(int deviceID, String user_name, String coordinates, boolean on_feet_status, int device_user_id){
        TextView tvDevice_ID = findViewById(R.id.tvDevice_ID);
        TextView tvUser_name = findViewById(R.id.tvUser_name);
        TextView tvCoordinates = findViewById(R.id.tvCoordinates);
        TextView tvOn_Feet_Status = findViewById(R.id.tvOn_Feet_Status);

        tvDevice_ID.setText("Current device ID: " + deviceID);
        tvUser_name.setText("This device is owned by: " + user_name + " and their ID is: " + device_user_id);
        tvCoordinates.setText("The current coordinates of the device are: " + coordinates);

        if(on_feet_status){
            tvOn_Feet_Status.setText("Het schaap staat op zijn poten.");
        }else{
            tvOn_Feet_Status.setText("Het schaap ligt op zijn rug");
            createNotificationChannel();
            sendNotification();
        }
    }

    //Creates a channel for the notifications to work on API26+
    private void createNotificationChannel(){
        final String NOTIFICATION_CHANNEL_ID = getString(R.string.default_notification_channel_id);
        final String NOTIFICATION_CHANNEL_NAME= "Sheep";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel notificationChannelTest = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
        notificationChannelTest.enableLights(true);
        notificationChannelTest.setLightColor(Color.RED);
        notificationChannelTest.enableVibration(true);
        notificationChannelTest.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannelTest);
    }

    //builds and issues the notification
    public void sendNotification(){
        Intent intent = new Intent(this, UserAreaActivity.class);//declare what activity the user will be send to when clicking on the notification
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT); //Give the phone access to the intent

        //Set notification sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Set the notification's content
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Sheep has fallen (ticket)") //doesn't seem to work
                .setContentTitle("EEN SCHAAP LIGT OP ZIJN RUN!")
                .setContentText("Een schaap heeft Uw hulp nodig.")
                .setSound(sound)
                .setContentIntent(pendingIntent);

        //The notification manager that'll build and sent the
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notificationBuilder.build());
    }

    //updates the current position of a marker
    public void updateMarker(){
        StringTokenizer tokens = new StringTokenizer(coordinates, ",");
        double first = Double.parseDouble(tokens.nextToken());
        double second = Double.parseDouble(tokens.nextToken());
        LatLng ll = new LatLng(first, second);

        sheepLocation.setPosition(new LatLng(first, second));

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
        mMap.moveCamera(update);
    }

    //removes a marker from the map
    public void removeMarkers(){
        if(sheepLocation != null){
            sheepLocation.remove();
            Log.d("myTag", "marker was removed");
            String message = "marker was removed";
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    //button to use as updater for now
    public void updateData(View view) {
        Button testButton = findViewById(R.id.btNotification);
        getData(testButton);
    }

    //button to use as updater for now
    public void locationUpdater(View view) {
        updateMarker();
    }

    //Google maps things
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    //Gets called when map is ready
    public void onMapReady(GoogleMap map) {
        this.mMap = map;

        StringTokenizer tokens = new StringTokenizer(coordinates, ",");
        double first = Double.parseDouble(tokens.nextToken());
        double second = Double.parseDouble(tokens.nextToken());
        LatLng ll = new LatLng(first, second);

        testMarker = new MarkerOptions().position(new LatLng(first, second));
        sheepLocation = map.addMarker(testMarker);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
        mMap.moveCamera(update);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
