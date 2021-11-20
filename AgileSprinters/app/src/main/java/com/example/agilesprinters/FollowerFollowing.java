package com.example.agilesprinters;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
            if (title == "Following") {
                userTempList = user.getFollowingList();
            }
        }

        titleTextView = findViewById(R.id.titleTextView);
        userList = findViewById(R.id.userListView);

        for(int i = 0; i < userTempList.size(); i++) {
            getUser(userTempList.get(i));
        }

        userDataList = new ArrayList<>();
        userAdapter = new CustomUserList(this, userDataList);


        userList.setAdapter(userAdapter);

        titleTextView.setText(title);
    }

    private void getUser(String uniqueId) {
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("users");

        currentUser.setUser(uniqueId);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (uniqueId.matches((String) doc.getData().get("UID"))) {
                        emailId = (String) doc.getData().get("Email ID");
                        firstName = (String) doc.getData().get("First Name");
                        lastName = (String) doc.getData().get("Last Name");
                        followersList = (ArrayList<String>) doc.getData().get("followers");
                        followingList = (ArrayList<String>) doc.getData().get("following");
                        followRequestList = (ArrayList<String>) doc.getData().get("follow request list");
                        setUser(currentUser, emailId, firstName, lastName, followersList, followingList, followRequestList);
                    }
                }
            }
        });
    }

    private void setUser(User user, String emailId, String firstName, String lastName,
                         ArrayList<String> followersList, ArrayList<String> followingList,
                         ArrayList<String> followRequestList){
        user.setEmailId(emailId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setFollowersList(followersList);
        user.setFollowingList(followingList);
        user.setFollowRequestList(followRequestList);
        userDataList.add(user);
    }
}