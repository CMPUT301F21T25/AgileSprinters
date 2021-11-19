package com.example.agilesprinters;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    private ArrayList<String> notificationList;
    private ListView notificationListView;
    private ArrayAdapter<String> notificationAdapter;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        notificationListView= findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();

        notificationAdapter = new ArrayAdapter<String>(this, R.layout.notifications_content,notificationList);
        notificationListView.setAdapter(notificationAdapter);


    }
}