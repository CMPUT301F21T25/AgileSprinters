package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * The home class is an activity which displays the habits of a user upon login. From here a user
 * may click the floating button to add a habit, tap on a habit to edit or view a habit, or long
 * click on a habit to delete it. There is a navigation bar on the bottom that the user may click
 * to go to either calendar, forum, or notifications.
 *
 * @author Hannah Desmarais, Hari Bheesetti, and Gurick Kooner
 */
public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener,
        viewEditHabitFragment.OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener,
        deleteHabitFragment.OnFragmentInteractionListener {
    private ArrayList<Habit> habitArrayList;
    private ListView habitList;
    private ArrayAdapter<Habit> habitAdapter;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    private TextView followingTextView;
    private TextView followersTextView;

    private static final String TAG = "Habit";
    private String UID;
    private User user;
    private String firstNameStr;
    private String collectionPath;
    private Database database = new Database();

    /**
     * This function creates the UI on the screen and listens for user input
     * @param savedInstanceState the instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        habitList = findViewById(R.id.habit_list);
        followingTextView = findViewById(R.id.following);
        followersTextView = findViewById(R.id.followers);

        habitArrayList = new ArrayList<>();
        habitAdapter = new habitListAdapter(this, habitArrayList);
        habitList.setAdapter(habitAdapter);


        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
            firstNameStr = (String) user.getFirstName();
        }

        TextView firstName = findViewById(R.id.userIdTextView);
        firstName.setText(firstNameStr);


        db = FirebaseFirestore.getInstance();
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

                        habitArrayList.add(new Habit(doc.getId(), UID, title, reason, dateToStart, weekdays, privacySetting, progress));
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
                viewEditHabitFragment values = new viewEditHabitFragment().newInstance(habit);
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
                addHabitFragment values = new addHabitFragment().newInstance(UID);
                values.show(getSupportFragmentManager(), "ADD");
                System.out.println(getIntent());
            }
        });

        followingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FollowerFollowing.class);
                intent.putExtra("Title", "Following");
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);
                finish();
            }
        });

        followersTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, FollowerFollowing.class);
                intent.putExtra("Title", "Followers");
                intent.putExtra(getString(R.string.USER_STR), user);
                startActivity(intent);
                finish();
            }
        });



        /**
         * This is a long item click listener which overrides the regular item click listener.
         * When an item is long clicked, it will begin the deleteHabitFragment
         */
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteHabitFragment delete = new deleteHabitFragment().newInstance(i);
                delete.show(getSupportFragmentManager(), "DELETE");

                // return true so that it overrides a regular item click and the view/edit fragment does not pop up
                return true;
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
     * @param habit The habit object changed in the viewEditHabitFragment
     */
    @Override
    public void onEditViewSaveChangesPressed(Habit habit) {
        updateHabitDatabase(habit);
    }

    /**
     * This method is called when the user presses cancel on the editViewCancel fragment. Will not
     * change the habit.
     */
    @Override
    public void onEditViewCancelPressed() {
    }


    //onclick for follow and followers. Not to be implemented until after the halfway checkpoint
    public void follow(View view) {
    }

    /**
     * This method contains the logic for switching screens by selecting an item from the navigation
     * bar.
     * @param item This is the item selected by the user
     * @return
     * Returns a boolean based on which activity the user is currently in and which item was
     * clicked.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Context context = getApplicationContext();
        switch (item.getItemId()) {
            case R.id.home:
                if (this instanceof Home) {
                    return true;
                } else {
                    Intent intent = new Intent(this, Home.class);
                    //add bundle to send data if need
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.calendar:
                Intent intent = new Intent(this, UserCalendar.class);
                intent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intent);
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
                Intent forumIntent = new Intent(this, ForumManager.class);
                forumIntent.putExtra("user", user);
                startActivity(forumIntent);
                finish();
                break;

        }
        return false;
    }

    /**
     * This function adds a habit to the database.
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

            // Makes a call to the database which handles it.
            database.addData(collectionPath, HabitId, data, TAG);
        }
    }

    /**
     * This is a method that updates a habit selected by the user in the database based with the
     * fields entered in the viewEditHabitFragment
     * @param habit
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
        collectionPath = "Habit";
        // Makes a call to the database which handles it
        database.updateData(collectionPath, habit.getHID(), data, TAG);
    }

    /**
     * This method deletes a habit selected by the user from the database
     * @param  HID is the habit object's ID selected by the user to be deleted
     */
    public void deleteHabitDb(String HID) {
        collectionPath = "Habit";
        // Makes a call to the database which handles it
        database.deleteData(collectionPath, HID, TAG);
    }

    /**
     * This method deletes a habit selected by the user from the database
     * @param  EID is the habit event object's ID selected by the user to be deleted
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
        deleteHabitInstances(habit.getHID());
        deleteHabitDb(habit.getHID());
        habitAdapter.notifyDataSetChanged();
    }

    /**
     * This method deletes habit events from the database based on the habit object passed to it.
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
                        if(doc.getId() == null){
                            return;
                        } else {
                            deleteHabitEventsDb(doc.getId());
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
     * This method deletes all of the user's habit and associated events when the user is deleted.
     */
    private void deleteUserHabits() {
        CollectionReference collectionReference = db.collection(getString(R.string.HABIT));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = "Habit";
                            deleteHabitInstances((String) doc.getId());
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            }
        });
        return;
    }


    /**
     * This method is for the creation of the options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    /**
     * This method lets a user sign out or delete their account on a menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutItem:
                confirmMenuSelection(item.getItemId(), getString(R.string.SIGN_OUT));
                return true;
            case R.id.deleteItem:
                confirmMenuSelection(item.getItemId(), getString(R.string.DEL_USER));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This function handles sign in/ authentication when the user clicks the sign in button,
     * email and password fields must be non-empty
     */
    private void confirmMenuSelection(int itemId, String titleStr) {

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(Home.this);
        confirmDialog.setTitle(titleStr);
        confirmDialog.setMessage(getString(R.string.SURE_STR)+titleStr.toLowerCase()+"?");

        // Calling the function to reset password here
        confirmDialog.setPositiveButton(R.string.CONFIRM_STR, (dialog, which) -> {
            select(itemId);
        });
        confirmDialog.setNegativeButton(R.string.CANCEL_STR, (dialog, which) -> dialog.cancel());
        confirmDialog.show();
    }

    private boolean select(int itemId){
        Intent intent = null;
        switch (itemId) {
            case R.id.signOutItem:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(this, Login.class);
                user = null;
                startActivity(intent);
                finish();
                return true;
            case R.id.deleteItem:
                deleteUser();
                user = null;
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(Home.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            default:
                return true;
        }
    }
    /**
     * This method deletes a user from the database and all its associated data and habits and events.
     */
    private void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.USER_DEL_LOG));
                            deleteUserHabits();
                            deleteUserData();
                        }
                        else {
                            Log.d(TAG, getString(R.string.USER_NOT_DEL_LOG));
                        }
                    }
                });
    }

    /**
     * This method deletes all data (email, password etc) associated with a user.
     */
    private void deleteUserData(){
        CollectionReference collectionReference = db.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {
                        if(doc.getId() == null){
                            return;
                        } else {
                            collectionPath = getString(R.string.USERS);
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
                // from the cloud
            }
        });
    }

}