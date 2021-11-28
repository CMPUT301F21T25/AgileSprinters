package com.example.agilesprinters;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
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
import java.util.Comparator;
import java.util.HashMap;

/**
 * This is an activity which will show the information of a user that is not the user currently
 * signed in to the app on the device. The current user may also choose to follow or unfollow the
 * user from this activity.
 *
 * @author Hari Bheesetti and Hannah Desmarais
 */
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

    /**
     * This function creates the UI on the screen and listens for user input.
     * @param savedInstanceState The instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_other_user_screen);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        //Set the follow button to the appropriate state.
        setFollowButton();

        /**
         * This is a button which will display "Follow","Unfollow", or "Follow Request Pending"
         * based on whether the current user is already following the other user. It will listen for
         * when the current user touches the button but will only be clickable when the other user
         * doesn't already have a follow request from the current user. Based on which state the
         * button is currently in, the button will perform actions to follow or unfollow the other
         * user.
         */
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

        /**
         * This is a database listener. Each time the other user page is created, it will read the
         * contents of the database to find the public habits for that user and put it in the
         * listview.
         */
        habitCollectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            // Clear the old list
            habitArrayList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                if (UID.matches((String) doc.getData().get("UID")) &&
                        ((String) doc.getData().get("PrivacySetting")).matches("Public") ) {
                    String title = (String) doc.getData().get("Title");
                    String reason = (String) doc.getData().get("Reason");
                    String dateToStart = (String) doc.getData().get("Date to Start");
                    HashMap<String, Boolean> weekdays = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                    String privacySetting = (String) doc.getData().get("PrivacySetting");
                    int progress = Integer.parseInt(doc.get("Progress").toString());
                    int position = Integer.parseInt((doc.get("List Position").toString()));

                    if (privacySetting.matches("Public")) {
                        habitArrayList.add(new Habit(doc.getId(), UID, title, reason,
                                dateToStart, weekdays, privacySetting, progress, position));
                        habitArrayList.sort(Comparator.comparing(Habit::getListPosition));
                    }
                }
            }
            habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            // from the cloud
        });

    }

    /**
     * This method will set the follow button to display either "Follow", "Unfollow" or "Follow
     * Request Pending" based on whether the current user is already following the other user or has
     * requested to follow them. If the other user already has a follow request that they have not
     * chosen to accept or decline yet from the current user, the button will not be clickable.
     */
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

    /**
     * This method sets the text fields for following count, follower count, and the text in the
     * otherUserButton to be the first letter of the other user's first name.
     * @param followingCount The number of people the other user is following as a string.
     * @param followersCount The number of people following the other user as a string.
     * @param firstNameStr The first name of the other user.
     */
    private void setTextFields(String followingCount, String followersCount, String firstNameStr) {
        TextView followingCountTextView = findViewById(R.id.followingCount);
        TextView followerCountTextView = findViewById(R.id.followerCount);
        Button otherUserButton = findViewById(R.id.otherUserButton);

        followerCountTextView.setText(followersCount);
        followingCountTextView.setText(followingCount);
        otherUserButton.setText(firstNameStr.substring(0,1));
    }

    /**
     * This method will update the document of a user in the database with the current values of the
     * user objects parameters upon being called.
     * @param user The user that is having their document updated as a User object.
     */
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

    /**
     * This method will finish the activity when the back button is pressed and override the
     * screen transition.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        overridePendingTransition(0,0);
    }
}