package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

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

    private static final String TAG = "Habit";
    private String UID;
    private User currentUser;

    /**
     * This function creates the UI on the screen and listens for user input
     *
     * @param savedInstanceState instance state {@link Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        habitList = findViewById(R.id.habit_list);
        habitArrayList = new ArrayList<>();
        habitAdapter = new habitListAdapter(this, habitArrayList);
        habitList.setAdapter(habitAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection(getString(R.string.habit));

        if (currentUser == null) {
            currentUser = (User) getIntent().getSerializableExtra(getString(R.string.currentUser));
            UID = currentUser.getUser();
        }

        /*
          This is a database listener. Each time the Home page is created, it will read the contents
          of the database and put it in our listview.
         */
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                habitArrayList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {

                        //get the information of each habit to be displayed
                        String habitTitle = (String) doc.getData().get(getString(R.string.habitTitle));
                        String habitReason = (String) doc.getData().get(getString(R.string.habitReason));
                        String habitStartDate = (String) doc.getData().get(getString(R.string.habitStartDate));
                        HashMap<String, Boolean> habitWeekdays = (HashMap<String, Boolean>) doc.getData().get(getString(R.string.habitWeekdays));
                        String habitPrivacySetting = (String) doc.getData().get(getString(R.string.habitPrivacySetting));

                        //add habit's info to be displayed
                        habitArrayList.add(new Habit(doc.getId(), UID, habitTitle, habitReason, habitStartDate, habitWeekdays, habitPrivacySetting));
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                // from the cloud
            }
        });

        /**
         This is an on item click listener which listens for when a user taps on an item in the
         habit list. Once clicked it will open the viewEditHabitFragment
         */
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) adapterView.getItemAtPosition(i);
                viewEditHabitFragment values = new viewEditHabitFragment().newInstance(habit);
                values.show(getSupportFragmentManager(), getString(R.string.viewEditFrag));
            }
        });

        /**
         This is a floating action button which listens for when a user taps it. If tapped it will
         begin the addHabitFragment.
         */
        final FloatingActionButton addButton = findViewById(R.id.add_habit_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHabitFragment values = new addHabitFragment().newInstance(UID);
                values.show(getSupportFragmentManager(), getString(R.string.addFrag));
                System.out.println(getIntent());
            }
        });

        /**
         This is a long item click listener which overrides the regular item click listener.
         When an item is long clicked, it will begin the deleteHabitFragment
         */
        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteHabitFragment deleteFrag = new deleteHabitFragment().newInstance(i);
                deleteFrag.show(getSupportFragmentManager(), getString(R.string.delFrag));

                // return true so that it overrides a regular item click and the view/edit fragment does not pop up
                return true;
            }
        });

    }

    /**
     * This function passes a habit to be added to the list once the user clicks add on the
     * addHabitFragment dialog fragment
     *
     * @param habit The habit object created by the addHabitFragment {@link Habit}
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
     * @param habit The habit object changed in the viewEditHabitFragment {@link Habit}
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
     *
     * @param item This is the item selected by the user {@link MenuItem}
     * @return Returns a boolean based on which activity the user is currently in and which item was
     * clicked
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Switch to different activities depending on item clicked on in nav bar
        switch (item.getItemId()) {
            case R.id.home:
                if (this instanceof Home) {
                    return true;
                } else {
                    Intent intent = new Intent(this, Home.class);
                    //add bundle to send data if need
                    startActivity(intent);
                }
                break;
            case R.id.calendar:
                Intent intent = new Intent(this, UserCalendar.class);
                intent.putExtra(getString(R.string.currentUser), currentUser);
                //add bundle to send data if needED
                startActivity(intent);
                break;
            case R.id.forumn:
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * This function adds a habit to the database.
     *
     * @param habit The habit that needs to be added to the database {@link Habit}
     */
    public void addHabitDatabase(Habit habit) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection(getString(R.string.habit));
        // Creating a unique Id for the Habit that is being added
        DocumentReference newHabitRef = db.collection(getString(R.string.habit)).document();
        String HabitId = newHabitRef.getId();


        if (HabitId != null) {
            HashMap<String, Object> data = putHabitDatababse(habit);
            collectionReference
                    .document(HabitId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, getString(R.string.dataAddSuccess));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, getString(R.string.dataAddErr) + e.toString());
                        }
                    });
        }
    }

    /**
     * This is a method that updates a habit selected by the user in the database based with the
     * fields entered in the viewEditHabitFragment
     *
     * @param habit Give habit to be updated {@link Habit}
     */
    public void updateHabitDatabase(Habit habit) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection(getString(R.string.habit));
        HashMap<String, Object> data = putHabitDatababse(habit);

        collectionReference
                .document(habit.getHID())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, getString(R.string.dataAddSuccess));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, getString(R.string.dataAddErr) + e.toString());
                    }
                });
    }

    /**
     * This method puts the given habit's information (UID, title, reason, privacy setting, start date, weekdays) <br>
     * into a hashmap that is passed to the database
     *
     * @param habit Give the habit whose info will be put into the hashmap {@link Habit}
     * @return hashmap to be passed to database {@link HashMap}
     */
    private HashMap<String, Object> putHabitDatababse(Habit habit) {
        HashMap<String, Object> habitData = new HashMap<>();
        habitData.put(getString(R.string.UID), UID);
        habitData.put(getString(R.string.habitTitle), habit.getTitle());
        habitData.put(getString(R.string.habitReason), habit.getReason());
        habitData.put(getString(R.string.habitPrivacySetting), habit.getPrivacySetting());
        habitData.put(getString(R.string.habitStartDate), habit.getDateToStart());
        habitData.put(getString(R.string.habitWeekdays), habit.getWeekdays());
        return habitData;
    }

    /**
     * This method deletes a habit selected by the user from the database
     *
     * @param habit this is the habit object selected by the user to be deleted
     */
    public void deleteHabitDatabase(Habit habit) {
        deleteHabitInstances(habit);
        db.collection(getString(R.string.habit))
                .document(habit.getHID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, getString(R.string.delDocSuccess));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, getString(R.string.delDocErr), e);
                    }
                });
    }

    /**
     * This function deletes the habit selected by the user in the list after a user clicks "Yes"
     * in the deleteHabitFragment dialog fragment.
     *
     * @param position The position of the object clicked in the list.
     */
    @Override
    public void onDeleteHabitYesPressed(int position) {
        Habit habitToDel = habitAdapter.getItem(position);
        deleteHabitDatabase(habitToDel);
        habitAdapter.notifyDataSetChanged();
    }

    /**
     * This method deletes habit events from the database based on the habit object passed to it.
     *
     * @param habit this is the habit object the user wishes to be deleted {@link Habit}
     */
    public void deleteHabitInstances(Habit habit) {
        CollectionReference collectionReference = db.collection(getString(R.string.HabitEvents));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (habit.getHID().matches((String) doc.getData().get(getString(R.string.HID)))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            db.collection(getString(R.string.HabitEvents))
                                    .document(doc.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, getString(R.string.delDocSuccess));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, getString(R.string.delDocErr), e);
                                        }
                                    });
                        }
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                // from the cloud
            }
        });
    }
}