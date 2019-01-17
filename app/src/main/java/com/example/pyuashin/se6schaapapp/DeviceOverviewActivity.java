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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class DeviceOverviewActivity extends AppCompatActivity {

    NotificationCompat.Builder sheepNotification;
    private static final int uniqueID = 1738;

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
        tvUser_name.setText("This device is owned by: " + user_name + " and their ID is: " + device_user_id);
        tvCoordinates.setText("The current coordinates of the device are: " + coordinates);

        if(on_feet_status){
            tvOn_Feet_Status.setText("Het schaap staat op zijn poten.");
        }else{
            tvOn_Feet_Status.setText("Het schaap ligt op zijn rug");
        }
    }

    public void notificationTest(View view) {
        createNotificationChannel();
        sendNotification();
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
                .setTicker("Sheep has fallen (ticket)")
                .setContentTitle("Sheep status(title)")
                .setContentText("This is the body of the sheep notification(Text)")
                .setSound(sound)
                .setContentIntent(pendingIntent);

        //The notification manager that'll build and sent the
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notificationBuilder.build());
    }
}
