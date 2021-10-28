package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserCalendar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        // Display the calendar
        CalendarView calendarView = findViewById(R.id.calendar);
        TextView textView = findViewById(R.id.selected_date);

        // When a date is clicked, display the date
        List<String> months = Arrays.asList("January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");


        // get habits here from the database
        ArrayList<String> days = new ArrayList<>();
        days.add("Monday");
        days.add("Wednesday");
        days.add("Friday");
        Habit habit1 = new Habit("Running", "To run a 5k", "2021-11-01",
                days,"Private");

        
        // Display it on the calendar using the start date and weekdays
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                textView.setText(months.get(month) + " " + String.valueOf(day) + ", "
                        + String.valueOf(year));
            }
        });


    }
}