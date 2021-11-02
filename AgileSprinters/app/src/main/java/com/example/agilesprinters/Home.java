package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener,
        viewEditHabitFragment.OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener,
        deleteHabitFragment.OnFragmentInteractionListener{
    ArrayList<Habit> habitArrayList;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    private static final String TAG = "Habit";
    private String user_id = getIntent().getStringExtra("userId");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        habitList = findViewById(R.id.habit_list);
        habitArrayList = new ArrayList<Habit>();

        habitAdapter = new habitListAdapter(this, R.layout.home_list_content, habitArrayList);
        habitList.setAdapter(habitAdapter);


        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) adapterView.getItemAtPosition(i);
                viewEditHabitFragment values = new viewEditHabitFragment().newInstance(i, habit);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        final FloatingActionButton addMedicineButton = findViewById(R.id.add_habit_button);
        addMedicineButton.setOnClickListener(new View.OnClickListener() {
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

    /**
     * This function adds a habit to the list once the user clicks add on the addHabitFragment
     * dialog fragment
     * @param habit The habit object created by the addHabitFragment
     */
    @Override
    public void onAddPressed(Habit habit) {
        addHabitDatabase(habit);
        habitAdapter.add(habit);

        habitAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditViewSaveChangesPressed(Habit habit, int position) {
        Habit original = habitAdapter.getItem(position);
        original.setTitle(habit.getTitle());
        original.setReason(habit.getReason());
        original.setDateToStart(habit.getDateToStart());
        original.setWeekdays(habit.getWeekdays());
        original.setPrivacySetting(habit.getPrivacySetting());
        habitAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEditViewCancelPressed(Habit habit, int position) {
        Habit original = habitAdapter.getItem(position);
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
                if(this instanceof Home){
                    return true;
                }
                else{
                    Intent intent = new Intent(this, Home.class);
                    //add bundle to send data if need
                    startActivity(intent);
                }
                break;

            case R.id.calendar:
                Intent intent = new Intent(this, UserCalendar.class);
                //add bundle to send data if need
                intent.putExtra("User_ID", user_id);
                startActivity(intent);
                break;

            case R.id.forumn:
                break;

        }
        return false;
    }

    public void addHabitDatabase(Habit habit){
        /**
         * This function takes the given habit and add it to the database
         * Input : Habit
         * Output: none
         */
        db  =  FirebaseFirestore.getInstance();
        final CollectionReference collectionReference  =  db.collection("Habit");
        // Creating a unique Id for the Habit that is being added
        DocumentReference newHabitRef = db.collection("Habit").document();
        String Uid = getIntent().getStringExtra("userId");
        String HabitId = newHabitRef.getId();
        HashMap<String, Object> data = new HashMap<>();

        if (HabitId != null){
            data.put("UID", Uid);
            data.put("Title", habit.getTitle());
            data.put("Reason",habit.getReason());
            data.put("PrivacySetting",habit.getPrivacySetting());
            data.put("Data to Start",habit.getDateToStart());
            data.put("Weekdays", habit.getWeekdays());

            collectionReference
                    .document(HabitId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log. d (TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log. d (TAG, "Data could not be added!" + e.toString());
                        }
                    });
        }
    }

    /**
     * This function deletes the object selected by the user in the list after a user clicks "Yes"
     * in the deleteHabitFragment dialog fragment.
     * @param position The position of the object clicked in the list.
     */
    @Override
    public void onDeleteHabitYesPressed(int position) {
        Habit habit = habitAdapter.getItem(position);
        habitAdapter.remove(habit);
        habitAdapter.notifyDataSetChanged();
    }
}