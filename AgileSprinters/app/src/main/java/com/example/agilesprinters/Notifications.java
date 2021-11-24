package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
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

public class Notifications extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        AcceptDeclineFollowRequestFragment.OnFragmentInteractionListener{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_notifications);
        db = FirebaseFirestore.getInstance();

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
        }

        DocumentReference userCollectionReference = db.collection("users").document(user.getUser());
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        notificationListView= findViewById(R.id.notification_list);
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationsListAdapter(this, notificationList);
        notificationListView.setAdapter(notificationAdapter);



        userCollectionReference.addSnapshotListener((value, error) -> {
            // Clear the old list
            notificationList.clear();
            Log.d(TAG, String.valueOf(value.getData().get("UID")));
            if (UID.matches((String) value.getData().get("UID"))) {
                ArrayList<String> followRequests = (ArrayList<String>) value.get("follow request list");

                for (int i = 0 ; i < followRequests.size(); i++) {
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



        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User requestingUser = (User) adapterView.getItemAtPosition(i);
                AcceptDeclineFollowRequestFragment values = new AcceptDeclineFollowRequestFragment().newInstance(requestingUser);
                values.show(getSupportFragmentManager(), "ACCEPT/DECLINE FRAGMENT");
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, Home.class);
                intent.putExtra("user", user);
                //add bundle to send data if need

                startActivity(intent);
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.calendar:
                Intent intentCalendar = new Intent(this, UserCalendar.class);
                intentCalendar.putExtra("user", user);
                startActivity(intentCalendar);
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.notification:
                if(this instanceof Notifications){
                    return true;
                } else {
                    Intent intent2 = new Intent(this, Notifications.class);
                    //add bundle to send data if need

                    startActivity(intent2);
                    finish();
                    overridePendingTransition(0,0);
                    break;
                }

            case R.id.forumn:
                Intent forumIntent = new Intent(this, ForumManager.class);
                forumIntent.putExtra("user", user);
                startActivity(forumIntent);
                finish();
                overridePendingTransition(0,0);
                break;

        }
        return false;
    }

    @Override
    public void onAcceptPressed(User requestingUser) {
        user.getFollowersList().add(requestingUser.getUserID());
        user.getFollowRequestList().remove(requestingUser.getUserID());
        requestingUser.getFollowingList().add(user.getUserID());

        updateUserDoc(user);
        updateUserDoc(requestingUser);

        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeclinePressed(User requestingUser) {
        user.getFollowRequestList().remove(requestingUser.getUserID());
        updateUserDoc(user);

        notificationAdapter.notifyDataSetChanged();
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
        database.updateData(collectionPath, user.getUserID(), data, TAG);
    }
}