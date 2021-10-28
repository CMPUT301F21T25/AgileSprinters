package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class viewEditHabitFragment extends DialogFragment{
    private int position;
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
    private Spinner privacy;
    private ArrayList<String> weekdays;
    private viewEditHabitFragment.OnFragmentInteractionListener listener;

    public static viewEditHabitFragment newInstance(int position, Habit habit) {
        viewEditHabitFragment frag = new viewEditHabitFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("habit", habit);
        frag.setArguments(args);

        return frag;
    }

    public interface OnFragmentInteractionListener {
        void onEditViewOkPressed(Habit habit, int position);
    }

    public void addWeekday(String day){
        if (weekdays.contains(day)){
            weekdays.remove(day);
        }
        else{
            weekdays.add(day);
        }
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof addHabitFragment.OnFragmentInteractionListener){
            listener = (viewEditHabitFragment.OnFragmentInteractionListener) context;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_edit_habit_fragment, null);

        habitTitle = view.findViewById(R.id.habit_title_editText);
        habitReason = view.findViewById(R.id.habit_reason_editText);
        date_editText = view.findViewById(R.id.Date);
        privacy = view.findViewById(R.id.privacy_spinner);

        //get weekday buttons
        sunday = view.findViewById(R.id.view_edit_button_sunday);
        monday = view.findViewById(R.id.view_edit_button_monday);
        tuesday = view.findViewById(R.id.view_edit_button_Tuesday);
        wednesday = view.findViewById(R.id.view_edit_button_wednesday);
        thursday = view.findViewById(R.id.view_edit_button_wednesday);
        friday = view.findViewById(R.id.view_edit_button_friday);
        saturday = view.findViewById(R.id.view_edit_button_saturday);


        Habit habit = (Habit) getArguments().getSerializable("habit");
        position = getArguments().getInt("position");

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        date_editText.setText(habit.getDateToStart());

        //make sure spinner for privacy settings is set to the correct option
        if(habit.getPrivacySetting() == "Private"){
            privacy.setSelection(1);
        }

        //set weekday buttons to proper colors

        if(habit.getWeekdays().contains("SUNDAY")) sunday.setBackgroundColor(Color.parseColor("#e27c65)"));
        if(habit.getWeekdays().contains("MONDAY")) monday.setBackgroundColor(Color.parseColor("#e27c65)"));
        if(habit.getWeekdays().contains("TUESDAY")) tuesday.setBackgroundColor(Color.parseColor("#e27c65)"));
        if(habit.getWeekdays().contains("WEDNESDAY")) wednesday.setBackgroundColor(Color.parseColor("#e27c65)"));
        if(habit.getWeekdays().contains("THURSDAY")) thursday.setBackgroundColor(Color.parseColor("#e27c65)"));
        if(habit.getWeekdays().contains("FRIDAY")) friday.setBackgroundColor(Color.parseColor("#e27c65)"));
        if(habit.getWeekdays().contains("SATURDAY")) saturday.setBackgroundColor(Color.parseColor("#e27c65)"));

        //set on click listeners for all weekday buttons
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("SUNDAY");
            }
        });

        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("MONDAY");
            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("TUESDAY");
            }
        });

        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("WEDNESDAY");
            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("THURSDAY");
            }
        });

        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("FRIDAY");
            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("SATURDAY");
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
                        String date = date_editText.getText().toString();
                        String privacySetting = privacy.getSelectedItem().toString();

                        listener.onEditViewOkPressed(new Habit(habit_title,habit_reason,date, weekdays, privacySetting), position);
                    }
                }).create();
    }

}
