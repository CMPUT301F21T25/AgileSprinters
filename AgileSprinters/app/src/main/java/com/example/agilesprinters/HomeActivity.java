package com.example.agilesprinters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * The home class is an activity which displays the habits of a user upon login. From here a user
 * may click the floating button to add a habit, tap on a habit to edit or view a habit, or long
 * click on a habit to delete it. There is a navigation bar on the bottom that the user may click
 * to go to either calendar, forum, or notifications.
 *
 * @author Hannah Desmarais, Hari Bheesetti, and Gurick Kooner
 */
public class HomeActivity extends AppCompatActivity implements AddHabitFragment.OnFragmentInteractionListener,
        ViewEditHabitFragment.OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener,
        DeleteHabitFragment.OnFragmentInteractionListener {
    private ArrayList<Habit> habitArrayList;
    private ListView habitList;
    private ArrayAdapter<Habit> habitAdapter;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    private TextView followingTextView;
    private TextView followersTextView;
    private TextView followingCountTextView;
    private TextView followerCountTextView;
    private FirebaseAuth auth;
    private MapHelperClass mapHelperClass = new MapHelperClass();

    private static final String TAG = "Habit";
    private String UID;
    private User user;
    private String collectionPath;
    private String nameStr;
    private String followingCount;
    private String followersCount;
    private Database database = new Database();

    /**
     * This function creates the UI on the screen and listens for user input
     *
     * @param savedInstanceState the instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        mapHelperClass.getLocationPermission(HomeActivity.this, HomeActivity.this);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        db = FirebaseFirestore.getInstance();

        habitList = findViewById(R.id.habit_list);
        followingTextView = findViewById(R.id.following);
        followersTextView = findViewById(R.id.followers);
        followingCountTextView = findViewById(R.id.followingCount);
        followerCountTextView = findViewById(R.id.followerCount);

        habitArrayList = new ArrayList<>();
        habitAdapter = new HabitListAdapter(this, habitArrayList);
        habitList.setAdapter(habitAdapter);

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUserID();
            nameStr = user.getFirstName() + " " + user.getLastName();

            getFollowersFollowingCount();

        }


        Button homeUserButton = findViewById(R.id.homeUserButton);
        homeUserButton.setText(nameStr.substring(0, 1));
        getFollowersFollowingCount();


        CollectionReference habitCollectionReference = db.collection("Habit");
        /**
         * This is a database listener. Each time the Home page is created, it will read the contents
         * of the database and put it in our listview.
         */
        habitCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
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
                        int progress = Integer.parseInt((doc.get("Progress").toString()));
                        int position = Integer.parseInt((doc.get("List Position").toString()));

                        habitArrayList.add(new Habit(doc.getId(), UID, title, reason,
                                dateToStart, weekdays, privacySetting, progress, position));
                        habitArrayList.sort(Comparator.comparing(Habit::getListPosition));
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                // from the cloud
            }
        });

        /**
         * This is an on item click listener which listens for when a user taps on an item in the
         * habit list. Once clicked it will open the viewEditHabitFragment
         */
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) adapterView.getItemAtPosition(i);
                ViewEditHabitFragment values = new ViewEditHabitFragment().newInstance(habit, i, habitArrayList.size());
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        /**
         * This is a floating action button which listens for when a user taps it. If tapped it will
         * begin the addHabitFragment.
         */
        final FloatingActionButton addButton = findViewById(R.id.add_habit_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddHabitFragment values = new AddHabitFragment().newInstance(UID, habitArrayList.size());
                values.show(getSupportFragmentManager(), "ADD");
                System.out.println(getIntent());
            }
        });

        /**
         * This is a textview for the following column which listens for when the user clicks it.
         * Upon being clicked, it will begin the FollowerFollowing activity.
         */
        followingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FollowerFollowingActivity.class);
                intent.putExtra("Title", "Following");
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        /**
         * This is a textview which displays the following count for the current user and listens
         * for when the user clicks it. Upon being clicked, it will start the FollowerFollowing
         * activity.
         */
        followingCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FollowerFollowingActivity.class);
                intent.putExtra("Title", "Following");
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        /**
         * This is a textview for the follower column which listens for when the user clicks it.
         * Upon being clicked, it will begin the FollowerFollowing activity.
         */
        followersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FollowerFollowingActivity.class);
                intent.putExtra("Title", "Followers");
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        /**
         * This is a textview which displays the follower count for the current user and listens
         * for when the user clicks it. Upon being clicked, it will start the FollowerFollowing
         * activity.
         */
        followerCountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FollowerFollowingActivity.class);
                intent.putExtra("Title", "Followers");
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        /**
         * This is a button which displays the first letter of the first name of the current user.
         * Upon being clicked, it will begin the EditUserActivity.
         */
        homeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, EditUserActivity.class);
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);

                overridePendingTransition(0, 0);
            }
        });

        /**
         * This is a long item click listener which overrides the regular item click listener.
         * When an item is long clicked, it will begin the deleteHabitFragment
         */
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("pos" + i);
                DeleteHabitFragment delete = new DeleteHabitFragment().newInstance(i);
                delete.show(getSupportFragmentManager(), "DELETE");

                // return true so that it overrides a regular item click and the view/edit fragment does not pop up
                return true;
            }
        });

    }

    /**
     * This function gets the followers and following count
     * of the current user from the database
     */
    private void getFollowersFollowingCount() {
        db.collection("users").addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot doc : value) {
                if (user.getUserID().matches(doc.getId())) {
                    followersCount = String.valueOf(((ArrayList<String>)doc.getData().get("followers")).size());
                    followingCount = String.valueOf(((ArrayList<String>)doc.getData().get("following")).size());
                    followerCountTextView.setText(followersCount);
                    followingCountTextView.setText(followingCount);
                }
            }
        });
    }

    /**
     * This function passes a habit to be added to the list once the user clicks add on the
     * addHabitFragment dialog fragment
     *
     * @param habit The habit object created by the addHabitFragment
     */
    @Override
    public void onAddPressed(Habit habit) {
        addHabitDatabase(habit);
        habitAdapter.notifyDataSetChanged();
    }

    /**
     * This function passes a habit to be updated once a user clicks Save Changes in the
     * viewEditHabitFragment dialog fragment.
     *
     * @param habit The habit object changed in the viewEditHabitFragment
     */
    @Override
    public void onEditViewSaveChangesPressed(Habit habit, int oldPosition) {

        if (habit.getListPosition() > oldPosition) {
            for (int i = habit.getListPosition(); i > oldPosition; i--) {
                habitArrayList.get(i).setListPosition(i - 1);
                updateHabitDatabase(habitArrayList.get(i));
            }
        }
        if (habit.getListPosition() < oldPosition) {
            for (int i = habit.getListPosition(); i < oldPosition; i++) {
                habitArrayList.get(i).setListPosition(i + 1);
                updateHabitDatabase(habitArrayList.get(i));
            }
        }

        updateHabitDatabase(habit);
    }

    /**
     * This function adds a habit to the database.
     *
     * @param habit The habit that needs to be added to the database.
     */
    public void addHabitDatabase(Habit habit) {
        // Creating a unique Id for the Habit that is being added
        DocumentReference newHabitRef = db.collection("Habit").document();
        String HabitId = newHabitRef.getId();
        HashMap<String, Object> data = new HashMap<>();
        collectionPath = "Habit";

        if (HabitId != null) {
            data.put("UID", UID);
            data.put("Title", habit.getTitle());
            data.put("Reason", habit.getReason());
            data.put("PrivacySetting", habit.getPrivacySetting());
            data.put("Date to Start", habit.getDateToStart());
            data.put("Weekdays", habit.getWeekdays());
            data.put("Progress", habit.getOverallProgress());
            data.put("List Position", habit.getListPosition());

            // Makes a call to the database which handles it.
            database.addData(collectionPath, HabitId, data, TAG);
        }
    }

    /**
     * This is a method that updates a habit selected by the user in the database based with the
     * fields entered in the viewEditHabitFragment
     *
     * @param habit is the habit object selected by the user to be updated
     */
    public void updateHabitDatabase(Habit habit) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Habit");
        HashMap<String, Object> data = new HashMap<>();

        data.put("UID", UID);
        data.put("Title", habit.getTitle());
        data.put("Reason", habit.getReason());
        data.put("PrivacySetting", habit.getPrivacySetting());
        data.put("Date to Start", habit.getDateToStart());
        data.put("Weekdays", habit.getWeekdays());
        data.put("Progress", habit.getOverallProgress());
        data.put("List Position", habit.getListPosition());
        collectionPath = "Habit";
        // Makes a call to the database which handles it
        database.updateData(collectionPath, habit.getHID(), data, TAG);
    }

    /**
     * This method deletes a habit selected by the user from the database
     *
     * @param HID is the habit object's ID selected by the user to be deleted
     */
    public void deleteHabitDb(String HID) {
        collectionPath = "Habit";
        // Makes a call to the database which handles it
        database.deleteData(collectionPath, HID, TAG);
    }

    /**
     * This method deletes a habit selected by the user from the database
     *
     * @param EID is the habit event object's ID selected by the user to be deleted
     */
    public void deleteHabitEventsDb(String EID) {
        collectionPath = "HabitEvents";
        // Makes a call to the database which handles it
        database.deleteData(collectionPath, EID, TAG);
    }

    /**
     * This function deletes the habit selected by the user in the list after a user clicks "Yes"
     * in the deleteHabitFragment dialog fragment.
     *
     * @param position The position of the object clicked in the list.
     */
    @Override
    public void onDeleteHabitYesPressed(int position) {
        Habit habit = habitAdapter.getItem(position);

        //Set all the positions of habits higher in the list down by one
        while (position < habitArrayList.size() - 1) {
            habitArrayList.get(position + 1).setListPosition(position);
            updateHabitDatabase(habitArrayList.get(position + 1));
            position++;
        }
        deleteHabitInstances(habit.getHID());
        deleteHabitDb(habit.getHID());
        habitAdapter.notifyDataSetChanged();
    }

    /**
     * This function deletes the forum events in the list after a user clicks deletes
     * its associated habit events or habit.
     *
     * @param FID The forum ID of the object chosen to delete.
     */
    public void deleteForumEventsDb(String FID) {
        collectionPath = "ForumPosts";
        // Makes a call to the database which handles it
        database.deleteData(collectionPath, FID, TAG);
    }

    /**
     * This method deletes habit events from the database based on the habit object passed to it.
     *
     * @param HID this is the habit object the user wishes to be deleted
     */
    public void deleteHabitInstances(String HID) {
        CollectionReference collectionReference = db.collection("HabitEvents");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (HID.matches((String) doc.getData().get("HID"))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            deleteHabitEventsDb(doc.getId());
                            deleteForumEventsDb((String) doc.getData().get("FID"));
                        }
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                // from the cloud
            }
        });
        return;
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
        //Context context = getApplicationContext();
        switch (item.getItemId()) {
            case R.id.home:
                if (this instanceof HomeActivity) {
                    return true;
                } else {
                    Intent intent = new Intent(this, HomeActivity.class);
                    //add bundle to send data if need
                    finish();
                    startActivity(intent);

                    overridePendingTransition(0, 0);
                }
                break;

            case R.id.calendar:
                Intent intent = new Intent(this, UserCalendarActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("mapHelper", mapHelperClass);
                //add bundle to send data if need
                finish();
                startActivity(intent);

                overridePendingTransition(0, 0);
                break;

            case R.id.notification:
                Intent intentNotification = new Intent(this, NotificationsActivity.class);
                intentNotification.putExtra("user", user);
                //add bundle to send data if need
                finish();
                startActivity(intentNotification);

                overridePendingTransition(0, 0);
                break;

            case R.id.forum:
                Intent forumIntent = new Intent(this, ForumActivity.class);
                forumIntent.putExtra("user", user);
                finish();
                startActivity(forumIntent);

                overridePendingTransition(0, 0);
                break;

        }
        return false;
    }
}