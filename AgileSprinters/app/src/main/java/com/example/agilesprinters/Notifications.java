package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private ArrayList<String> notificationList;
    private ListView notificationListView;
    private ArrayAdapter<String> notificationAdapter;
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "Notifications";
    private String UID;
    private User user;
    private String collectionPath;
    private Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        notificationListView= findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();

        notificationAdapter = new ArrayAdapter<String>(this, R.layout.notifications_content,notificationList);
        notificationListView.setAdapter(notificationAdapter);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, Home.class);
                intent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intent);
                break;

            case R.id.calendar:
                Intent intentCalendar = new Intent(this, UserCalendar.class);
                intentCalendar.putExtra("user", user);

                startActivity(intentCalendar);
                break;
            case R.id.forumn:
                break;
            case R.id.notification:
                if(this instanceof Notifications){
                    return true;
                } else {
                    Intent intent2 = new Intent(this, Notifications.class);
                    //add bundle to send data if need
                    startActivity(intent2);
                    break;
                }
        }
        return false;
    }
}