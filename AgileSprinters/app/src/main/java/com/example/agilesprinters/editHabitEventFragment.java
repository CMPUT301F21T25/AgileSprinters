package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class editHabitEventFragment extends DialogFragment{
    private CheckBox completed;
    private CheckBox not_completed;
    private EditText optional_comment;
    private String date;
    private Integer duration;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        // Display the calendar
        completed = view.findViewById(R.id.checkBox_completed);
        not_completed = view.findViewById(R.id.checkBox_notCompleted);
        optional_comment = view.findViewById(R.id.editText_comment);
        date = view.findViewById(R.id.editText_date);
        duration = view.findViewById(R.id.editText_duration);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view).create();
    }

}
