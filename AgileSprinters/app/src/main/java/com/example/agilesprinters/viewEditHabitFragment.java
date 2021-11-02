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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class viewEditHabitFragment extends DialogFragment{
    private int position;
    private String date;
    private EditText habitTitle;
    private EditText habitReason;
    private TextView date_textView;
    private TextView buttonError;
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
        void onEditViewSaveChangesPressed(Habit habit, int position);
        void onEditViewCancelPressed(Habit habit, int position);
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

        habitTitle = view.findViewById(R.id.view_edit_habit_title_editText);
        habitReason = view.findViewById(R.id.view_edit_habit_reason_editText);
        date_textView = view.findViewById(R.id.view_edit_habit_date);
        privacy = view.findViewById(R.id.view_edit_privacy_spinner);
        buttonError = view.findViewById(R.id.view_edit_habit_button_error);

        //get weekday buttons
        sunday = view.findViewById(R.id.view_edit_button_sunday);
        monday = view.findViewById(R.id.view_edit_button_monday);
        tuesday = view.findViewById(R.id.view_edit_button_Tuesday);
        wednesday = view.findViewById(R.id.view_edit_button_wednesday);
        thursday = view.findViewById(R.id.view_edit_button_thursday);
        friday = view.findViewById(R.id.view_edit_button_friday);
        saturday = view.findViewById(R.id.view_edit_button_saturday);


        Habit habit = (Habit) getArguments().getSerializable("habit");
        position = getArguments().getInt("position");

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        date_textView.setText("Date Started: " + habit.getDateToStart());
        weekdays = habit.getWeekdays();
        date = habit.getDateToStart();


        //make sure spinner for privacy settings is set to the correct option
        if(habit.getPrivacySetting().equals("Private")){
            privacy.setSelection(1);
        }

        //set weekday buttons to proper colors and initialize the trackers for buttons pressed
        final int[] setSunday = {0};
        final int[] setMonday = {0};
        final int[] setTuesday = {0};
        final int[] setWednesday = {0};
        final int[] setThursday = {0};
        final int[] setFriday = {0};
        final int[] setSaturday = {0};
        if(habit.getWeekdays().contains("SUNDAY")) {
            sunday.setBackgroundColor(Color.parseColor("#e27c65"));
            setSunday[0] = 1;
        }
        if(habit.getWeekdays().contains("MONDAY")) {
            monday.setBackgroundColor(Color.parseColor("#e27c65"));
            setMonday[0] = 1;
        }
        if(habit.getWeekdays().contains("TUESDAY")) {
            tuesday.setBackgroundColor(Color.parseColor("#e27c65"));
            setTuesday[0] = 1;
        }
        if(habit.getWeekdays().contains("WEDNESDAY")) {
            wednesday.setBackgroundColor(Color.parseColor("#e27c65"));
            setWednesday[0] = 1;
        }
        if(habit.getWeekdays().contains("THURSDAY")) {
            thursday.setBackgroundColor(Color.parseColor("#e27c65"));
            setThursday[0] = 1;
        }
        if(habit.getWeekdays().contains("FRIDAY")) {
            friday.setBackgroundColor(Color.parseColor("#e27c65"));
            setFriday[0] = 1;
        }
        if(habit.getWeekdays().contains("SATURDAY")) {
            saturday.setBackgroundColor(Color.parseColor("#e27c65"));
            setSaturday[0] = 1;
        }

        //set on click listeners for all weekday buttons
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

        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("THURSDAY");
                if (setThursday[0] == 0){
                    thursday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setThursday[0] = 1;
                } else {
                    thursday.setBackgroundColor(Color.parseColor("#808080"));
                    setThursday[0] = 0;
                }
            }
        });

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

        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWeekday("SATURDAY");
                if (setSaturday[0] == 0){
                    saturday.setBackgroundColor(Color.parseColor("#e27c65"));
                    setSaturday[0] = 1;
                } else {
                    saturday.setBackgroundColor(Color.parseColor("#808080"));
                    setSaturday[0] = 0;
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View/Edit Habit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onEditViewCancelPressed(habit, position);
                    }
                })
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();

    }

    @Override
    public void onResume(){
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null){
            Button positive = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean readyToClose = true;
                    String habit_title = habitTitle.getText().toString();
                    String habit_reason = habitReason.getText().toString();
                    String privacySetting = privacy.getSelectedItem().toString();

                    if (habit_title.matches("")) {
                        readyToClose = false;
                        habitTitle.setError("This field cannot be blank");
                    }
                    if (habit_reason.matches("")) {
                        readyToClose = false;
                        habitReason.setError("This field cannot be blank");
                    }
                    if (privacySetting.matches("")) {
                        readyToClose = false;
                    }
                    if (weekdays.isEmpty()) {
                        readyToClose = false;
                        buttonError.setText("Please choose which days you would like this event to occur.");
                    }

                    if(readyToClose) positive.setEnabled(true);
                    if(readyToClose){
                        listener.onEditViewSaveChangesPressed(new Habit(habit_title,habit_reason,date, weekdays, privacySetting), position);
                        dialog.dismiss();
                    }
                }
            });
        }
    }

}

