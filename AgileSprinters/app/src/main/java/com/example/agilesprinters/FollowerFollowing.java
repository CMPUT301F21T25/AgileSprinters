package com.example.agilesprinters;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowerFollowing extends AppCompatActivity {
    private User user;
    private String UID;
    private String title;
    private TextView titleTextView;
    ListView userList;
    ArrayAdapter<User> userAdapter;
    ArrayList<User> userDataList;
    ArrayList<String> userTempList;



    private User currentUser = new User();
    private FirebaseFirestore db;
    private Database database = new Database();
    private String firstName, lastName, emailId, uniqueId;
    private ArrayList<String> followersList = new ArrayList<>();
    private ArrayList<String> followingList = new ArrayList<>();
    private ArrayList<String> followRequestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following);

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
            title = (String) getIntent().getStringExtra("Title");

            if (title.matches("Following")){
                userTempList = user.getFollowingList();
            } else if (title.matches( "Followers")) {
                userTempList = user.getFollowersList();
            }
        }

        titleTextView = findViewById(R.id.titleTextView);
        userList = findViewById(R.id.userListView);

        userDataList = new ArrayList<>();
        userAdapter = new CustomUserList(this, userDataList);

        userList.setAdapter(userAdapter);

        titleTextView.setText(title);

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (userTempList.contains((String) doc.getData().get("UID"))) {
                        emailId = (String) doc.getData().get("Email ID");
                        firstName = (String)  doc.getData().get("First Name");
                        lastName = (String) doc.getData().get("Last Name");
                        followersList = (ArrayList<String>) doc.getData().get("followers");
                        followingList = (ArrayList<String>) doc.getData().get("following");
                        followRequestList = (ArrayList<String>) doc.getData().get("follow request list");
                        userDataList.add(new User((String) doc.getData().get("UID"), firstName, lastName, emailId, followersList, followingList, followRequestList));
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(FollowerFollowing.this, OtherUserScreen.class);
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);
                finish();
            }
        });
    }
}