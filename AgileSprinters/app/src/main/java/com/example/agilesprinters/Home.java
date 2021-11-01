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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener,
        viewEditHabitFragment.OnFragmentInteractionListener, BottomNavigationView.OnNavigationItemSelectedListener{
    ArrayList<Habit> habitArrayList;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;

    BottomNavigationView bottomNavigationView;

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
        ArrayList<String> list = new ArrayList<>();

        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) adapterView.getItemAtPosition(i);
                viewEditHabitFragment values =
                        new viewEditHabitFragment().newInstance(i, habit);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        final FloatingActionButton addMedicineButton = findViewById(R.id.add_habit_button);
        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addHabitFragment().show(getSupportFragmentManager(), "ADD");
            }
        });

    }

    @Override
    public void onAddPressed(Habit habit) {
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
                Intent intent = new Intent(this, Test.class);
                //add bundle to send data if need
                startActivity(intent);
                break;

            case R.id.forumn:
                break;

        }
        return false;
    }




}