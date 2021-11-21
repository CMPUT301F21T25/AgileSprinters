package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ForumManager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private String UID;
    private User user;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;


    ListView instanceList;
    ArrayAdapter<User> userAdapter;
    ArrayList<User> userDataList;
    ArrayList<String> userTempList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_manager);

        if (UID == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.notification);

    }

    /**
     * This method contains the logic for switching screens by selecting an item from the navigation
     * bar.
     *
     * @param item This is the item selected by the user
     * @return Returns a boolean based on which activity the user is currently in and which item was
     * clicked.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, Home.class);
                intent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intent);
                finish();
                break;

            case R.id.calendar:
                Intent calendarIntent = new Intent(this, UserCalendar.class);
                calendarIntent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(calendarIntent);
                finish();
                break;

            case R.id.notification:
                Intent intentNotification = new Intent(this, Notifications.class);
                intentNotification.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intentNotification);
                finish();
                break;

            case R.id.forumn:
                if (this instanceof ForumManager) {
                    return true;
                } else {
                    Intent forumIntent = new Intent(this, ForumManager.class);
                    //add bundle to send data if need
                    startActivity(forumIntent);
                    finish();
                    break;
                }
        }
        return false;
    }



}