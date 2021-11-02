package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class addHabitEventFragment extends DialogFragment{
    private int position;
    private String UID;
    private String HID;

    private EditText optional_comment;
    private EditText input_date;
    private EditText input_duration;

    private addHabitEventFragment.OnFragmentInteractionListener listener;

    public static addHabitEventFragment newInstance(int position, String UID, String HID) {
        addHabitEventFragment fragment = new addHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("UID", UID);
        args.putString("HID", HID);
        fragment.setArguments(args);

        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onSavePressed(HabitInstance habitInstance);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        // Display the calendar
        optional_comment = view.findViewById(R.id.editText_comment);
        input_date = view.findViewById(R.id.editText_date);
        input_duration = view.findViewById(R.id.editText_duration);

        position = getArguments().getInt("position");
        UID = getArguments().getString("UID");
        HID = getArguments().getString("HID");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String comment = optional_comment.getText().toString();
                        String date = input_date.getText().toString();
                        int duration = Integer.parseInt(input_duration.getText().toString());

                        listener.onSavePressed(new HabitInstance(UID, HID, comment, date, duration));
                    }
                }).create();

    }

}
