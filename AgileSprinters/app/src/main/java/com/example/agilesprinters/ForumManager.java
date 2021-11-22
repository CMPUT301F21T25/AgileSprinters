package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForumManager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private String UID;
    private User user;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;


    ListView forumList;
    ArrayAdapter<Forum> forumAdapter;
    final private ArrayList<Forum> forumDataList = new ArrayList<>();;
    ArrayList<String> userTempList;
    ArrayList<HashMap> habitList = new ArrayList<>();
    ArrayList<HashMap> userList = new ArrayList<>();

    private TextView titleTextView;
    private String firstName, lastName, eventDate, duration, optComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_manager);

        if (UID == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
            userTempList = user.getFollowingList();
            userTempList.add(UID);
        }

        System.out.println("User list is " + userTempList);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.forumn);

        titleTextView = findViewById(R.id.forumTitleTextView);
        ListView forumList = findViewById(R.id.forumListView);

        //forumDataList = new ArrayList<>();

        titleTextView.setText("Forum");

        db = FirebaseFirestore.getInstance();

        screenSetup();

        System.out.println("Array list3 is " + forumDataList);
        forumAdapter = new forumPostList(this, forumDataList);
        forumList.setAdapter(forumAdapter);
    }

    public void screenSetup() {
        /**CollectionReference collectionReference = db.collection("ForumPosts");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    System.out.println("IDS are " + (String) doc.getData().get("UID"));
                    if(userTempList.contains((String) doc.getData().get("UID"))){
                        firstName = (String) doc.getData().get("First Name");
                        lastName = (String) doc.getData().get("Last Name");
                        duration = (String) doc.getData().get("duration");
                        eventDate = (String) doc.getData().get("Event Date");
                        forumDataList.add(new Forum(firstName, lastName, eventDate, duration));
                        System.out.println("Array list1 is " + forumDataList);
                    }
                }
                forumAdapter.notifyDataSetChanged();
            }
        });**/

        db.collection("ForumPosts").addSnapshotListener((value, error) -> {
            forumDataList.clear();
            for (QueryDocumentSnapshot doc : value) {
                System.out.println("IDS are " + (String) doc.getData().get("UID"));
                if(userTempList.contains((String) doc.getData().get("UID"))){
                    firstName = (String) doc.getData().get("First Name");
                    lastName = (String) doc.getData().get("Last Name");
                    duration = (String) doc.getData().get("duration");
                    eventDate = (String) doc.getData().get("Event Date");
                    optComment = (String) doc.getData().get("Opt Cmt");
                    forumDataList.add(new Forum(firstName, lastName, eventDate, duration, optComment));
                    System.out.println("Array list1 is " + forumDataList);
                }
            }

            forumAdapter.notifyDataSetChanged();
        });

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