package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.ArrayList;
import java.util.Calendar;


public class addHabitFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private EditText habitTitle;
    private EditText habitReason;
    private EditText date_editText;
    private Button sunday;
    private Button monday;
    private Button tuesday;
    private Button wednesday;
    private Button thursday;
    private Button friday;
    private Button saturday;
    private String date = "";
    private Spinner privacy;
    private ArrayList<String> weekdays;
    private addHabitFragment.OnFragmentInteractionListener listener;

    public void addWeekday(String day){
        if (!weekdays.isEmpty() && weekdays.contains(day)){
            weekdays.remove(day);
        }
        else{
            weekdays.add(day);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if(month+1 < 10) date+= "0";
        date += String.valueOf(month + 1) + "/";
        if (dayOfMonth < 10 ) date += "0";
        date += String.valueOf(dayOfMonth + "/");
        date += String.valueOf(year);
        date_editText.setText(date);
    }

    public interface OnFragmentInteractionListener {
        void onAddPressed(Habit habit);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment, null);

        weekdays = new ArrayList<String>();
        habitTitle = view.findViewById(R.id.habit_title_editText);
        habitReason = view.findViewById(R.id.habit_reason_editText);
        date_editText = view.findViewById(R.id.Date);
        privacy = view.findViewById(R.id.privacy_spinner);

        //set weekday buttons
        sunday = view.findViewById(R.id.button_sunday);
        monday = view.findViewById(R.id.button_monday);
        tuesday = view.findViewById(R.id.button_Tuesday);
        wednesday = view.findViewById(R.id.button_wednesday);
        thursday = view.findViewById(R.id.button_thursday);
        friday = view.findViewById(R.id.button_friday);
        saturday = view.findViewById(R.id.button_saturday);

        //set on click listeners for all weekday buttons and the editText for the date started
        date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datePickerFragment();
                datePicker.show(getChildFragmentManager(), "date picker");
            }
        });

        final int[] setSunday = {0};
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("SUNDAY");
                if (setSunday[0] == 0) {
                    sunday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setSunday[0] = 1;
                } else {
                    sunday.setBackgroundColor(Color.parseColor("#808080"));
                    setSunday[0] = 0;
                }
            }
        });

        final int[] setMonday = {0};
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("MONDAY");
                if (setMonday[0] == 0){
                    monday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setMonday[0] = 1;
                } else {
                    monday.setBackgroundColor(Color.parseColor("#808080"));
                    setMonday[0] = 0;
                }
            }
        });

        final int[] setTuesday = {0};
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("TUESDAY");
                if (setTuesday[0] == 0){
                    tuesday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setTuesday[0] = 1;
                } else {
                    tuesday.setBackgroundColor(Color.parseColor("#808080"));
                    setTuesday[0] = 0;
                }
            }
        });

        final int[] setWednesday = {0};
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("WEDNESDAY");
                if (setWednesday[0] == 0){
                    wednesday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setWednesday[0] = 1;
                } else {
                    wednesday.setBackgroundColor(Color.parseColor("#808080"));
                    setWednesday[0] = 0;
                }
            }
        });

        final int[] setThurs = {0};
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("THURSDAY");
                if (setThurs[0] == 0){
                    thursday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setThurs[0] = 1;
                } else {
                    thursday.setBackgroundColor(Color.parseColor("#808080"));
                    setThurs[0] = 0;
                }
            }
        });

        final int[] setFriday = {0};
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("FRIDAY");
                if (setFriday[0] == 0){
                    friday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setFriday[0] = 1;
                } else {
                    friday.setBackgroundColor(Color.parseColor("#808080"));
                    setFriday[0] = 0;
                }
            }
        });

        final int[] setSat = {0};
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("SATURDAY");
                if (setSat[0] == 0){
                    saturday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setSat[0] = 1;
                } else {
                    saturday.setBackgroundColor(Color.parseColor("#808080"));
                    setSat[0] = 0;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String habit_title = habitTitle.getText().toString();
                        String habit_reason = habitReason.getText().toString();
                        String privacySetting = privacy.getSelectedItem().toString();

                        listener.onAddPressed(new Habit(habit_title,habit_reason,date, weekdays, privacySetting));
                    }
                }).create();
    }

}
