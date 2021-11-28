package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * The forum manager class is an activity which displays the habit events completed by the user in
 * the forum page. From here a user may click on the search bar to search for other users in the app.
 * If interested, the user can go to that users profile to checkout their profile. They can also
 * follow/unfollow another user from here. There is a navigation bar on the bottom that the user
 * may click to go to either home, user calendar, or notifications.
 *
 * @author Sai Rasazna Ajerla and Haricharan Bheesetti
 */
public class ForumManager extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private String UID;
    private User user;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db;

    // Variables for search bar
    private TextInputLayout user_drop_down;
    private AutoCompleteTextView users_list;

    // Variables for other user profiles
    private ArrayList<String> arrayList_users = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_users;
    private ArrayList<User> array_user_objects = new ArrayList<>();

    // Variables for forum page
    ArrayAdapter<Forum> forumAdapter;
    final private ArrayList<Forum> forumDataList = new ArrayList<>();;
    ArrayList<String> userTempList;

    // Variables used to store the content of a forum event
    private String firstName, lastName, eventDate, duration, optComment, imageId, location;

    private String emailId;
    private ArrayList<String> followersList = new ArrayList<>();
    private ArrayList<String> followingList = new ArrayList<>();
    private ArrayList<String> followRequestList = new ArrayList<>();

    /**
     * This function creates the UI on the screen and listens for user input
     * @param savedInstanceState the instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forum_manager);

        // Gets the user information of the logged in user
        if (UID == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUserID();
            userTempList = user.getFollowingList();
            userTempList.add(UID);
        }

        // connecting to the bottom navigation bar xml file
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.forumn);

        // connecting to the search bar xml files
        user_drop_down = findViewById(R.id.user_search_drop_down);
        users_list = findViewById(R.id.users_list);

        ListView forumList = findViewById(R.id.forumListView);
        db = FirebaseFirestore.getInstance();

        screenSetup();
        buildUsersList();

        // Sets the forum events list to the UI
        forumAdapter = new forumPostList(this, forumDataList);
        forumList.setAdapter(forumAdapter);

        // Sets all the users to the search bar
        arrayAdapter_users = new ArrayAdapter<>(getApplicationContext(),
                R.layout.drop_down_menu, arrayList_users);
        users_list.setAdapter(arrayAdapter_users);
        users_list.setThreshold(2);

        // When a item in the user search bar is clicked, it opens up the profile of
        // that specific user
        users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println((String) adapterView.getItemAtPosition(i));

                for (User userToSend : array_user_objects) {
                    if (userToSend.getEmailId().matches((String) adapterView.getItemAtPosition(i))) {
                        Intent intent = new Intent(ForumManager.this, OtherUserScreen.class);
                        intent.putExtra("currentUser", user);
                        intent.putExtra("otherUser", userToSend);
                        startActivity(intent);

                    }
                }
            }
        });
    }

    /**
     * This function gets all the users in the database i.e., using the app
     * and displays it in the search bar
     */
    private void buildUsersList() {
        db.collection("users").addSnapshotListener((value, error) -> {
            arrayAdapter_users.clear();
            for (QueryDocumentSnapshot doc : value) {
                if(!doc.getId().matches(user.getUserID())){
                    emailId = (String) doc.getData().get("Email ID");
                    firstName = (String)  doc.getData().get("First Name");
                    lastName = (String) doc.getData().get("Last Name");
                    followersList = (ArrayList<String>) doc.getData().get("followers");
                    followingList = (ArrayList<String>) doc.getData().get("following");
                    followRequestList = (ArrayList<String>) doc.getData().get("follow request list");

                    arrayList_users.add(emailId);
                    array_user_objects.add(
                            new User((String) doc.getData().get("UID"), firstName, lastName,
                                    emailId, followersList, followingList, followRequestList));

                }
            }
            arrayAdapter_users.notifyDataSetChanged();
        });
    }

    /**
     * This function sets the forum events on the UI
     * according to the completed habit events of the user and its following users
     */
    public void screenSetup() {
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
                    imageId = (String) doc.getData().get("IID");
                    location = (String) doc.getData().get("Opt_Loc");
                    System.out.println("Printing while adding "+location);
                    forumDataList.add(new Forum(firstName, lastName, eventDate, duration, optComment, imageId, location));
                    System.out.println("Array list1 is " + forumDataList);
                }
            }
            forumAdapter.notifyDataSetChanged();
            if(userTempList.size() > 0){
                userTempList.remove(userTempList.size()-1);
            }

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
                overridePendingTransition(0,0);
                break;

            case R.id.calendar:
                Intent calendarIntent = new Intent(this, UserCalendar.class);
                calendarIntent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(calendarIntent);
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.notification:
                Intent intentNotification = new Intent(this, Notifications.class);
                intentNotification.putExtra("user", user);
                //add bundle to send data if need

                startActivity(intentNotification);
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.forumn:
                if (this instanceof ForumManager) {
                    return true;
                } else {
                    Intent forumIntent = new Intent(this, ForumManager.class);
                    //add bundle to send data if need
                    startActivity(forumIntent);
                    finish();
                    overridePendingTransition(0,0);
                    break;
                }
        }
        return false;
    }
}