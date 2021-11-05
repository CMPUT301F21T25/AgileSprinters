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

public class addHabitEventFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private int position;
    private String EID;
    private String UID;
    private String HID;
    private String date = "";

    private EditText optional_comment;
    private TextView input_date;
    private EditText input_duration;

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //make sure date is empty before setting it to the date picked
        date = "";
        if (month + 1 < 10) date += "0";
        date += String.valueOf(month + 1) + "/";
        if (dayOfMonth < 10) date += "0";
        date += String.valueOf(dayOfMonth + "/");
        date += String.valueOf(year);
        input_date.setText(date);
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

        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datePickerFragment();
                datePicker.show(getChildFragmentManager(), getString(R.string.datePickerTag));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(getString(R.string.AddHabitInstanceFragmentTitle))
                .setNegativeButton(R.string.cancelBtnStr, null)
                .setPositiveButton(getString(R.string.saveBtnStr), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
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
                    //String date_entry = input_date.getText().toString();
                    String duration = input_duration.getText().toString();

                    if (comment.length() > 20) {
                        readyToClose = false;
                        optional_comment.setError(getString(R.string.optCmtLenErr));
                    }

                    if (comment.matches("")) {
                        readyToClose = false;
                        optional_comment.setError(getString(R.string.fieldEmptyErr));
                    }

                    if (date.matches("")) {
                        readyToClose = false;
                        input_date.setError(getString(R.string.fieldEmptyErr));
                    }

                    LocalDate currentDate = LocalDate.now();
                    LocalDate eventDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(getString(R.string.dateFormatStr)));

                    if (!eventDate.isEqual(currentDate)) {
                        readyToClose = false;
                        input_date.setError(getString(R.string.currentDateErr));
                    }

                    if (duration.matches("")) {
                        readyToClose = false;
                        input_duration.setError(getString(R.string.fieldEmptyErr));
                    }

                    // If everything has been filled out, call the listener and send the edited
                    // habit back to the Home class and dismiss the dialog.
                    if (readyToClose) {
                        listener.onSavePressed(new HabitInstance(EID, UID, HID, comment, date, Integer.parseInt(duration)));
                        dialog.dismiss();
                    }
                }
            });
        }
    }

}
