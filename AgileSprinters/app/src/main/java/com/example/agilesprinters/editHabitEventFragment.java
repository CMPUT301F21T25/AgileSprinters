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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class editHabitEventFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private int position;
    private EditText optional_comment;
    private TextView input_date;
    private EditText input_duration;
    private String EID;
    private String UID;
    private String HID;

    private String date = "";

    private editHabitEventFragment.OnFragmentInteractionListener listener;

    public static editHabitEventFragment newInstance(int position, HabitInstance habitInstance) {
        editHabitEventFragment fragment = new editHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("Habit instance", habitInstance);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //make sure date is empty before setting it to the date picked
        date = "";
        if(month+1 < 10) date+= "0";
        date += String.valueOf(month + 1) + "/";
        if (dayOfMonth < 10 ) date += "0";
        date += String.valueOf(dayOfMonth + "/");
        date += String.valueOf(year);
        input_date.setText(date);
    }

    public interface OnFragmentInteractionListener {
        void onEditSavePressed(HabitInstance instance);
        void onDeletePressed(HabitInstance instance);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof addHabitEventFragment.OnFragmentInteractionListener){
            listener = (editHabitEventFragment.OnFragmentInteractionListener) context;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        // Display the calendar
        optional_comment = view.findViewById(R.id.editText_comment);
        input_date = view.findViewById(R.id.editText_date);
        input_duration = view.findViewById(R.id.editText_duration);

        HabitInstance habitInstance = (HabitInstance) getArguments().getSerializable("Habit instance");

        optional_comment.setText(habitInstance.getOpt_comment());
        input_date.setText(habitInstance.getDate());
        input_duration.setText(String.valueOf(habitInstance.getDuration()));
        date = habitInstance.getDate();

        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datePickerFragment();
                datePicker.show(getChildFragmentManager(), "DATE PICKER");
            }
        });

        EID = habitInstance.getEID();
        UID = habitInstance.getUID();
        HID = habitInstance.getHID();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View/Edit Habit Event")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDeletePressed(habitInstance);
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

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
                    // Boolean tracks when the all the fields have been filled out. Will turn to false
                    // if anything has been left blank.
                    Boolean readyToClose = true;

                    String comment = optional_comment.getText().toString();
                    //String date = input_date.getText().toString();
                    String duration = input_duration.getText().toString();

                    if (optional_comment.length() > 20) {
                        readyToClose = false;
                        optional_comment.setError("This field cannot have more than 20 chars");
                    }

                    if (date.matches("")) {
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
                        listener.onEditSavePressed(new HabitInstance(EID,UID,HID,comment, date, Integer.parseInt(duration)));
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
