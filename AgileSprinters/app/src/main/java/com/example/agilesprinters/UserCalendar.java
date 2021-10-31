package com.example.agilesprinters;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class UserCalendar extends AppCompatActivity {
    private ListView toDoEventsList;
    private ListView completedEventsList;

    private final ArrayList<String> toDoEvents = new ArrayList<>();
    private final ArrayList<String> completedEvents = new ArrayList<>();

    private ArrayAdapter<String> toDoEventAdapter;
    private ArrayAdapter<String> completedEventAdapter;

    private final ArrayList<HabitInstance> habitEvents_list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        FloatingActionButton addButton = findViewById(R.id.addEventButton);
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
                new editHabitEventFragment().show(getSupportFragmentManager(), "EDIT");
            }
        });


        /**
        // Display the calendar

        // When a date is clicked, display the date
        List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");


        // get habits here from the database
        ArrayList<String> days = new ArrayList<>();
        days.add("MONDAY");
        days.add("WEDNESDAY");
        days.add("FRIDAY");
        Habit habit1 = new Habit("Running", "To run a 5k", "2021-10-27",
                days,"Private");
        days.remove("Friday");
        Habit habit2 = new Habit("Walking", "To stay healthy", "2021-11-05",
                days,"Private");

        ArrayList<Habit> habits = new ArrayList<>();
        habits.add(habit1);
        //habits.add(habit2);

        // Display it on the calendar using the start date and weekdays
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {


                String curr_habit_date = habits.get(0).getDateToStart();
                String[] parts = curr_habit_date.split("-");
                //String dates = "2011-12-31";
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
                //LocalDate chosenDate = LocalDate.parse(dates, formatter);
                LocalDate chosenDate = LocalDate.of(year, month, day);

                if(chosenDate.compareTo(date)>0){
                    if (habits.get(0).getWeekdays().contains(chosenDate.getDayOfWeek().toString())) {
                        textView.setText(months.get(month) + " " + String.valueOf(day) + ", "
                                + String.valueOf(year) + "Todays schedule" + chosenDate.getDayOfWeek().toString());
                    } else {
                        textView.setText(months.get(month) + " " + String.valueOf(day) + ", "
                                + String.valueOf(year));
                    }
                }else if(chosenDate.compareTo(date)<0){
                    textView.setText(months.get(month) + " " + String.valueOf(day) + ", "
                            + String.valueOf(year));
                }else if(chosenDate.compareTo(date)==0){
                    textView.setText(months.get(month) + " " + String.valueOf(day) + ", "
                            + String.valueOf(year));
                }else{
                    textView.setText("How to get here?");
                }

            }
        });
         **/
    }

    public ArrayList<Habit> createHabits() {
        ArrayList<String> days = new ArrayList<>();
        days.add("MONDAY");
        days.add("WEDNESDAY");
        days.add("SUNDAY");

        Habit habit1 = new Habit("Running", "To run a 5k", "2021-10-27", days,"Private");
        Habit habit2 = new Habit("Walking", "To stay healthy", "2021-10-05", days,"Private");

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
}