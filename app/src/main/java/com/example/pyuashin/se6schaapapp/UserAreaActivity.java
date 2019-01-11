package com.example.pyuashin.se6schaapapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvWelcomeMessage = (TextView) findViewById(R.id.tvWelcomeMessage);
        final TextView tvUserInfo = (TextView) findViewById(R.id.tvUserInfo);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String username = intent.getStringExtra("username");

        String message = name + " , welcome to your user area";
        tvWelcomeMessage.setText(message);
        etUsername.setText(username);
        tvName.setText(name);
    }
}
