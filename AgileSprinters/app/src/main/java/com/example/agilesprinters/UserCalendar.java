package com.example.agilesprinters;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class UserCalendar extends AppCompatActivity implements addHabitEventFragment.OnFragmentInteractionListener {
    private ListView toDoEventsList;
    private ListView completedEventsList;

    private final ArrayList<String> toDoEvents = new ArrayList<>();
    private final ArrayList<String> completedEvents = new ArrayList<>();

    private ArrayAdapter<String> toDoEventAdapter;
    private ArrayAdapter<String> completedEventAdapter;

    private final ArrayList<HabitInstance> habitEvents_list = new ArrayList<>();

    String selectedCompletedEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        TextView title1 = findViewById(R.id.title1);

        // Getting present date and day of the week
        LocalDate todayDate = LocalDate.now();
        String todayDay = todayDate.getDayOfWeek().toString();

        // Setting the current date to the first text view
        String formattedDate = todayDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        title1.setText(formattedDate + ")");

        // Create a list of habit events and initialize an adapter
        toDoEventsList = findViewById(R.id.toDoEventsList);
        completedEventsList = findViewById(R.id.completedEventsList);

        toDoEventAdapter = new ArrayAdapter<>(
                this, R.layout.calendar_content, toDoEvents);
        completedEventAdapter = new ArrayAdapter<>(
                this, R.layout.calendar_content, completedEvents);

        toDoEventsList.setAdapter(toDoEventAdapter);
        completedEventsList.setAdapter(completedEventAdapter);

        // Creating habits
        ArrayList<Habit> habits = createHabits();

        initialScreen(habits, todayDate, todayDay);

        toDoEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addHabitEventFragment dialog = new addHabitEventFragment();
                //dialog.setValue("Running");
                dialog.show(getSupportFragmentManager(), "EDIT");
            }
        });

        completedEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCompletedEvent = completedEvents.get(i);
                int pos = Integer.parseInt(selectedCompletedEvent.split("[)]")[0]);

            }
        });
    }

    public ArrayList<Habit> createHabits() {
        ArrayList<String> days = new ArrayList<>();
        days.add("MONDAY");
        days.add("WEDNESDAY");
        days.add("SUNDAY");

        Habit habit1 = new Habit("Running", "To run a 5k", "2021-10-27", days, "Private");
        Habit habit2 = new Habit("Walking", "To stay healthy", "2021-10-05", days, "Private");

        ArrayList<Habit> habits = new ArrayList<>();
        habits.add(habit1);
        habits.add(habit2);

        return habits;
    }

    public void initialScreen(ArrayList<Habit> habits, LocalDate todayDate, String todayDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < habits.size(); i++) {
            Habit currentHabit = habits.get(i);

            // Checking if there are any habits assigned for today
            LocalDate startDate = LocalDate.parse(currentHabit.getDateToStart(), formatter);

            if (startDate.isBefore(todayDate) && currentHabit.getWeekdays().contains(todayDay)) {
                toDoEvents.add(currentHabit.getTitle());
            }
        }

        toDoEventAdapter.notifyDataSetChanged();
    }

    public void displayCompletedEvents() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < habitEvents_list.size(); i++) {
            HabitInstance currentHabit = habitEvents_list.get(i);
            currentHabit.setUniqueId(i+1);

            LocalDate doneDate = LocalDate.parse(currentHabit.getDate(), formatter);
            String formattedDate = doneDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            String toDisplay = currentHabit.getUniqueId() + ") Running" + " @ " + formattedDate
                    + " for " + String.valueOf(currentHabit.getDuration()) + " mins";
            completedEvents.add(toDisplay);
        }

        completedEventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSavePressed(HabitInstance habitInstance) {
        habitEvents_list.add(habitInstance);

        displayCompletedEvents();
    }

}