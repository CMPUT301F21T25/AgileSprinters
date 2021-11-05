package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class addHabitEventFragment extends DialogFragment{
    private int position;
    private String EID;
    private String UID;
    private String HID;
    private String date = "";

    private EditText optional_comment;
    private TextView input_date;
    private EditText input_duration;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(getString(R.string.dateFormatStr));

    private addHabitEventFragment.OnFragmentInteractionListener listener;

    public static addHabitEventFragment newInstance(int position, String UID, String HID, String EID) {
        addHabitEventFragment fragment = new addHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("UID", UID);
        args.putString("HID", HID);
        args.putString("EID", EID);
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
                    + getString(R.string.onFragmentImplementErr));
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

        position = getArguments().getInt(getString(R.string.positionKey));
        UID = getArguments().getString(getString(R.string.uidKey));
        HID = getArguments().getString(getString(R.string.hidKey));
        EID = getArguments().getString(getString(R.string.eidKey));

        LocalDate currentDate = LocalDate.now();
        input_date.setText(currentDate.format(formatter));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {

                }).create();

    }

    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positive = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);

            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Boolean tracks when the all the fields have been filled out. Will turn to false
                    // if anything has been left blank.
                    Boolean readyToClose = true;

                    String comment = optional_comment.getText().toString();
                    String date_entry = input_date.getText().toString();
                    String duration = input_duration.getText().toString();

                    if (comment.length() > 20) {
                        readyToClose = false;
                        optional_comment.setError("This field cannot have more than 20 chars");
                    }

                    if (date_entry.matches("")) {
                        readyToClose = false;
                        input_date.setError("This field cannot be blank");
                    }


                    if (duration.matches("")) {
                        readyToClose = false;
                        input_duration.setError("This field cannot be blank");
                    }

                    // If everything has been filled out, call the listener and send the edited
                    // habit back to the Home class and dismiss the dialog.
                    if(readyToClose){
                        listener.onSavePressed(new HabitInstance(EID, UID, HID, comment, date_entry, Integer.parseInt(duration)));
                        dialog.dismiss();
                    }
                }
            });
        }
    }

}
