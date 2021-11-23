package com.example.agilesprinters;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class OtherUserScreen extends AppCompatActivity {
    private String UID;
    private User currentUser;
    private User otherUser;
    private String nameStr;
    private String followingCount;
    private int followersCount;
    private ArrayList<Habit> habitArrayList;
    private ArrayAdapter<Habit> habitAdapter;
    private Button follow;
    private static final String TAG = "Habit";
    private static final String UPDATE_TAG = "users";
    private FirebaseFirestore db;
    private Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_screen);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference habitCollectionReference = db.collection("Habit");
        ListView habitList = findViewById(R.id.habit_list);

        follow = findViewById(R.id.followButton);
        habitArrayList = new ArrayList<>();
        habitAdapter = new habitListAdapter(this, habitArrayList);
        habitList.setAdapter(habitAdapter);

        if(currentUser == null){
            currentUser = (User) getIntent().getSerializableExtra("currentUser");
        }

        if (otherUser == null) {
            otherUser = (User) getIntent().getSerializableExtra("otherUser");
            UID = otherUser.getUser();
            nameStr = otherUser.getFirstName()+ " " + otherUser.getLastName();

            followersCount = otherUser.getFollowersList().size();
            followingCount = String.valueOf(otherUser.getFollowingList().size());
        }

        setTextFields(followingCount, String.valueOf(followersCount), nameStr);

        setFollowButton();

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (follow.getText().toString().matches(getString(R.string.FOLLOW_BUTTON_TEXT))){
                    otherUser.getFollowRequestList().add(currentUser.getUserID());
                    updateUserDoc(otherUser);
                }
                else if (follow.getText().toString().matches(getString(R.string.UNFOLLOW_BUTTON_TEXT))){
                    otherUser.getFollowersList().remove(currentUser.getUserID());
                    currentUser.getFollowingList().remove(otherUser.getUserID());
                    followersCount--;
                    setTextFields(followingCount, String.valueOf(followersCount), nameStr);
                    updateUserDoc(otherUser);
                    updateUserDoc(currentUser);
                }

                setFollowButton();
            }
        });

        habitCollectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            // Clear the old list
            habitArrayList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                if (UID.matches((String) doc.getData().get("UID"))) {
                    String title = (String) doc.getData().get("Title");
                    String reason = (String) doc.getData().get("Reason");
                    String dateToStart = (String) doc.getData().get("Date to Start");
                    HashMap<String, Boolean> weekdays = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                    String privacySetting = (String) doc.getData().get("PrivacySetting");
                    int progress = Integer.parseInt(doc.get("Progress").toString());
                    int position = Integer.parseInt((doc.get("List Position").toString()));

                    if (position > habitArrayList.size()-1){
                        habitArrayList.add(new Habit(doc.getId(), UID, title, reason,
                                dateToStart, weekdays, privacySetting, progress, position));
                    } else{
                        habitArrayList.add(position, new Habit(doc.getId(), UID, title, reason,
                                dateToStart, weekdays, privacySetting, progress, position));
                    }
                }
            }
            habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            // from the cloud
        });

    }

    private void setFollowButton(){
        ArrayList<String> followers = otherUser.getFollowersList();
        ArrayList<String> followRequests = otherUser.getFollowRequestList();

        if (followers.contains(currentUser.getUserID())) {
            follow.setText(getString(R.string.UNFOLLOW_BUTTON_TEXT));
            follow.setEnabled(true);
        }
        if (!followers.contains(currentUser.getUserID()) && followRequests.contains(currentUser.getUserID())){
            follow.setEnabled(false);
            follow.setText(getString(R.string.FOLLOW_REQUEST_PENDING_TEXT));
            follow.setTextColor(Color.parseColor(getString(R.string.FOLLOW_REQUEST_PENDING_COLOR)));
            follow.setBackgroundColor(Color.GRAY);
        }
        if (!followers.contains(currentUser.getUserID()) && !followRequests.contains(currentUser.getUserID())){
            follow.setText(getString(R.string.FOLLOW_BUTTON_TEXT));
            follow.setEnabled(true);
        }

    }

    private void setTextFields(String followingCount, String followersCount, String firstNameStr) {
        TextView firstNameTextView = findViewById(R.id.userIdTextView);
        TextView followingCountTextView = findViewById(R.id.followingCount);
        TextView followerCountTextView = findViewById(R.id.followerCount);

        followerCountTextView.setText(followersCount);
        followingCountTextView.setText(followingCount);
        firstNameTextView.setText(firstNameStr);
    }

    public void updateUserDoc(User user) {
        db = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        String collectionPath = "users";

        data.put("UID", user.getUserID());
        data.put(getString(R.string.EMAIL_ID_STR), user.getEmailId());
        data.put(getString(R.string.FIRST_NAME_STR), user.getFirstName());
        data.put(getString(R.string.LAST_NAME_STR), user.getLastName());
        data.put("followers", user.getFollowersList());
        data.put("following", user.getFollowingList());
        data.put("follow request list", user.getFollowRequestList());
        // Makes a call to the database which handles it
        database.updateData(collectionPath, user.getUserID(), data, UPDATE_TAG);
    }
}