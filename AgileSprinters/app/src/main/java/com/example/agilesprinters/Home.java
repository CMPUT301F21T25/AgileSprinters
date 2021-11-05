package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener,

        viewEditHabitFragment.OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener,
        deleteHabitFragment.OnFragmentInteractionListener {
    ArrayList<Habit> habitArrayList;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;

    private static final String TAG = "Habit";
    private String UID;
    private User user;

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
        final CollectionReference collectionReference = db.collection("Habit");

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
        }
        else{
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        System.out.println("User" + UID);

       /** findViewById(R.id.signoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.signoutBtn) {
                    FirebaseAuth.getInstance()
                            .signOut()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    // user is now signed out
                                    startActivity(new Intent(Home.this, Login.class));
                                    finish();
                                }
                            });
                }
            }});
        **/

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                habitArrayList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (/*auth.getCurrentUser().getUid() USE ONCE SET UP JUST COMPARE WITH TEMP ID FOR NOW*/
                            UID.matches((String) doc.getData().get("UID"))) {
                        String title = (String) doc.getData().get("Title");
                        String reason = (String) doc.getData().get("Reason");
                        String dateToStart = (String) doc.getData().get("Data to Start");
                        HashMap<String, Boolean> weekdays = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                        String privacySetting = (String) doc.getData().get("PrivacySetting");

                        habitArrayList.add(new Habit(doc.getId(), UID, title, reason, dateToStart, weekdays, privacySetting));
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                // from the cloud
            }
        });

        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) adapterView.getItemAtPosition(i);
                viewEditHabitFragment values = new viewEditHabitFragment().newInstance(habit);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        final FloatingActionButton addButton = findViewById(R.id.add_habit_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addHabitFragment().show(getSupportFragmentManager(), "ADD");
                System.out.println(getIntent());
            }
        });

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

    @Override
    public void onStart() {
        super.onStart();
    }
    /**
     * This function adds a habit to the list once the user clicks add on the addHabitFragment
     * dialog fragment
     *
     * @param habit The habit object created by the addHabitFragment
     */
    @Override
    public void onAddPressed(Habit habit) {
        addHabitDatabase(habit);
        habitAdapter.add(habit);
        habitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditViewSaveChangesPressed(Habit habit) {
        updateHabitDatabase(habit);
    }

    @Override
    public void onEditViewCancelPressed(Habit habit) {
        //original.setWeekdays(habit.getWeekdays());
        //habitAdapter.notifyDataSetChanged();
    }


    //onclick for follow and followers
    public void follow(View view) {
    }

    //click logic for navigation bar
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
                }
                break;

            case R.id.calendar:
                Intent intent = new Intent(this, UserCalendar.class);
                intent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intent);
                break;

            case R.id.forumn:
                break;

        }
        return false;
    }

    /**
     * This function adds
     *
     * @param habit The habit that needs to be added to the database.
     */
    public void addHabitDatabase(Habit habit) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Habit");
        // Creating a unique Id for the Habit that is being added
        DocumentReference newHabitRef = db.collection("Habit").document();
        String Uid = getIntent().getStringExtra("userId");
        String HabitId = newHabitRef.getId();
        HashMap<String, Object> data = new HashMap<>();

        if (HabitId != null) {
            data.put("UID", UID);
            data.put("Title", habit.getTitle());
            data.put("Reason", habit.getReason());
            data.put("PrivacySetting", habit.getPrivacySetting());
            data.put("Data to Start", habit.getDateToStart());
            data.put("Weekdays", habit.getWeekdays());

            collectionReference
                    .document(HabitId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be added!" + e.toString());
                        }
                    });
        }
    }

    public void updateHabitDatabase(Habit habit) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Habit");
        HashMap<String, Object> data = new HashMap<>();

        data.put("UID", UID);
        data.put("Title", habit.getTitle());
        data.put("Reason", habit.getReason());
        data.put("PrivacySetting", habit.getPrivacySetting());
        data.put("Data to Start", habit.getDateToStart());
        data.put("Weekdays", habit.getWeekdays());

        collectionReference
                .document(habit.getHID())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }

    public void deleteHabitDatabase(Habit habit) {
        deleteHabitInstances(habit);
        db.collection("Habit")
                .document(habit.getHID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /**
     * This function deletes the object selected by the user in the list after a user clicks "Yes"
     * in the deleteHabitFragment dialog fragment.
     *
     * @param position The position of the object clicked in the list.
     */
    @Override
    public void onDeleteHabitYesPressed(int position) {
        Habit habit = habitAdapter.getItem(position);
        deleteHabitDatabase(habit);
        habitAdapter.notifyDataSetChanged();
    }

    public void deleteHabitInstances(Habit habit) {
        CollectionReference collectionReference = db.collection("HabitEvents");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (habit.getHID().matches((String) doc.getData().get("HID"))) {
                        if(doc.getId() == null){
                            return;
                        } else {
                            db.collection("HabitEvents")
                                    .document(doc.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                        }
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                // from the cloud
            }
        });
        return;
    }
}