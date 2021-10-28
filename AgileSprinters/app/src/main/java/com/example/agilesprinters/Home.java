package com.example.agilesprinters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener,
        viewEditHabitFragment.OnFragmentInteractionListener{
    ArrayList<Habit> habitArrayList;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
    public void onEditViewOkPressed(Habit habit, int position) {
        Habit original = habitAdapter.getItem(position);
        original.setTitle(habit.getTitle());
        original.setReason(habit.getReason());
        original.setDateToStart(habit.getDateToStart());
        original.setWeekdays(habit.getWeekdays());
        original.setPrivacySetting(habit.getPrivacySetting());
        habitAdapter.notifyDataSetChanged();

    }

}