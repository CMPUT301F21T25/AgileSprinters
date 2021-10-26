package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addHabitFragment extends DialogFragment {
    private EditText habitTitle;
    private EditText habitReason;
    private EditText date_editText;
    private OnFragmentInteractionListener listener;

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

        habitTitle = view.findViewById(R.id.habit_title_editText);
        habitReason = view.findViewById(R.id.habit_reason_editText);
        date_editText = view.findViewById(R.id.Date);


        date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        /*
                         * This section in on click captures the users data and checks it to see
                         * whether they have added something or not. If they left the field blank,
                         * it is set to a default value. Input types are accounted for by the
                         * XML file called add_medicine_fragment. Each editText has a specifier for the
                         * type of data it can have. For example, medicineName will only ever take
                         * up to 40 characters due to the line "android:maxLength="40"" under the
                         * EditText named med_name_editText.
                         */

                        String habit_title = habitTitle.getText().toString();
                        String habit_reason = habitReason.getText().toString();

                        listener.onAddPressed(new Habit(habit_title,habit_reason,date_editText.getText().toString()));
                    }
                }).create();
    }

}
