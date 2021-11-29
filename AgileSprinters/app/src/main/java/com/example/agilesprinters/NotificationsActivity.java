package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is an activity which displays any follow requests the current user has. It displays them as
 * a list with the name of the user requesting to follow them and a text saying "has requested to
 * follow you." Once clicked the user can decide to accept or decline and this class will add the
 * user who requested to follow to the appropriate lists saved in the current user User object as
 * well as add the current user to the user who requested to follow's appropriate list.
 *
 * @author Hannah Desmarais
 */
public class NotificationsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        AcceptDeclineFollowRequestFragment.OnFragmentInteractionListener {
    private ArrayList<User> notificationList;
    private ListView notificationListView;
    private ArrayAdapter<User> notificationAdapter;
    BottomNavigationView bottomNavigationView;
    private String UID;
    private User user;
    private String collectionPath = "users";
    private static final String TAG = "User";
    private FirebaseFirestore db;
    private Database database = new Database();

    /**
     * This function creates the UI on the screen and listens for user input.
     *
     * @param savedInstanceState The instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notifications);
        db = FirebaseFirestore.getInstance();

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            user.getFollowRequestList().clear();
            user.getFollowersList().clear();
            user.getFollowingList().clear();
            UID = user.getUserID();
        }

        getUserLists();

        DocumentReference userCollectionReference = db.collection("users").document(user.getUserID());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        notificationListView = findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationsListAdapter(this, notificationList);
        notificationListView.setAdapter(notificationAdapter);

        /**
         * This is a database collection listener. Each time the Notifications activity is created,
         * it will read the database and find all the users who have requested to follow the current
         * user and build the notification list fom them.
         */
        userCollectionReference.addSnapshotListener((value, error) -> {
            // Clear the old list
            notificationList.clear();
            Log.d(TAG, String.valueOf(value.getData().get("UID")));
            if (UID.matches((String) value.getData().get("UID"))) {
                ArrayList<String> followRequests = (ArrayList<String>) value.get("follow request list");

                /*
                 * For each UID that is in the follow request list, find the corresponding user file
                 * in the database and create a user object with their stored data. Add each created
                 * user to the list.
                 */
                for (int i = 0; i < followRequests.size(); i++) {
                    DocumentReference otherUsersDoc = db.collection(collectionPath).document(followRequests.get(i));
                    otherUsersDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Log.d(TAG, "onComplete: " + documentSnapshot.getData());
                                    String otherUID = (String) documentSnapshot.get("UID");
                                    String firstName = (String) documentSnapshot.get("First Name");
                                    String lastName = (String) documentSnapshot.get("Last Name");
                                    String emailId = (String) documentSnapshot.get("Email ID");
                                    ArrayList<String> followRequest = (ArrayList<String>) documentSnapshot.get("follow request list");
                                    ArrayList<String> followers = (ArrayList<String>) documentSnapshot.get("followers");
                                    ArrayList<String> following = (ArrayList<String>) documentSnapshot.get("following");

                                    User requestingUser = new User(otherUID, firstName, lastName, emailId, followers, following, followRequest);
                                    notificationList.add(requestingUser);
                                    notificationAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
            notificationAdapter.notifyDataSetChanged();
        });

        /**
         * The following listener will listen for when an item in the notification list is clicked
         * and begin the AcceptDeclineFollowRequestFragment.
         */
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User requestingUser = (User) adapterView.getItemAtPosition(i);
                AcceptDeclineFollowRequestFragment values = new AcceptDeclineFollowRequestFragment().newInstance(requestingUser);
                values.show(getSupportFragmentManager(), "ACCEPT/DECLINE FRAGMENT");
            }
        });


    }

    /**
     * This method is for the navigation bar. It will begin the corresponding activity of the item
     * on the navigation bar that was clicked.
     *
     * @param item The menu item clicked by the user.
     * @return Returns true if the item clicked is the button for the current screen the user is already on.
     * Returns false if the item clicked does not match any of the four appropriate activities.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("user", user);
                //add bundle to send data if need

                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.calendar:
                Intent intentCalendar = new Intent(this, UserCalendarActivity.class);
                intentCalendar.putExtra("user", user);
                startActivity(intentCalendar);
                finish();
                overridePendingTransition(0, 0);
                break;

            case R.id.notification:
                if (this instanceof NotificationsActivity) {
                    return true;
                } else {
                    Intent intent2 = new Intent(this, NotificationsActivity.class);
                    //add bundle to send data if need

                    startActivity(intent2);
                    finish();
                    overridePendingTransition(0, 0);
                    break;
                }

            case R.id.forum:
                Intent forumIntent = new Intent(this, ForumActivity.class);
                forumIntent.putExtra("user", user);
                startActivity(forumIntent);
                finish();
                overridePendingTransition(0, 0);
                break;

        }
        return false;
    }

    /**
     * This method is called from the AcceptDeclineFollowRequestFragment and will add the requesting
     * user to the current user's follower list, add the current user to the requesting user's
     * following list and delete the request from the follow request list. It will update the
     * database for each user by calling updateUserDoc().
     *
     * @param requestingUser The user who sent the follow request to the current user.
     */
    @Override
    public void onAcceptPressed(User requestingUser) {
        user.getFollowersList().add(requestingUser.getUserID());
        user.getFollowRequestList().remove(requestingUser.getUserID());
        requestingUser.getFollowingList().add(user.getUserID());

        updateUserDoc(user);
        updateUserDoc(requestingUser);

        notificationAdapter.notifyDataSetChanged();
    }

    /**
     * This method is called from the AcceptDeclineFollowRequestFragment and will simply get rid
     * of the request and update the current user doc in the database to match by calling
     * updateUserDoc().
     *
     * @param requestingUser The user who requested to follow the current user.
     */
    @Override
    public void onDeclinePressed(User requestingUser) {
        user.getFollowRequestList().remove(requestingUser.getUserID());
        updateUserDoc(user);

        notificationAdapter.notifyDataSetChanged();
    }

    /**
     * This method will get all parameters from a User object and update the corresponding document
     * in the database.
     *
     * @param user The User object being updated.
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
        database.updateData(collectionPath, user.getUserID(), data, TAG);
    }

    public void getUserLists(){
        db.collection("users").addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot doc : value) {
                if (user.getUserID().matches(doc.getId())) {
                    ArrayList<String> userTempList = (ArrayList<String>) doc.getData().get("following");
                    user.setFollowingList(userTempList);

                    userTempList = (ArrayList<String>) doc.getData().get("followers");
                    user.setFollowersList(userTempList);

                    userTempList = (ArrayList<String>) doc.getData().get("follow request list");
                    user.setFollowRequestList(userTempList);
                }
            }
        });
    }
}