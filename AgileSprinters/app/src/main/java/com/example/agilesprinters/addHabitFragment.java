package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dpro.widgets.OnWeekdaysChangeListener;
import com.dpro.widgets.WeekdaysPicker;

import java.util.List;


public class addHabitFragment extends DialogFragment implements datePickerFragment.OnFragmentInteractionListener{
    private EditText habitTitle;
    private EditText habitReason;
    private EditText date_editText;
    private Spinner privacy;
    private WeekdaysPicker widget;
    private List weekdays;
    private addHabitFragment.OnFragmentInteractionListener listener;

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
        privacy = view.findViewById(R.id.privacy_spinner);
        widget = view.findViewById(R.id.weekdays);


        date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new datePickerFragment().show(getFragmentManager(), "DATE PICKER");
            }
        });

        widget.setOnWeekdaysChangeListener(new OnWeekdaysChangeListener() {
            @Override
            public void onChange(View view, int clickedDayOfWeek, List<Integer> selectedDays) {
                widget.setBackgroundColor(Color.RED);
                weekdays = selectedDays;
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

                        listener.onAddPressed(new Habit(habit_title,habit_reason,date, weekdays, privacySetting));
                    }
                }).create();
    }

    @Override
    public void onDatePickerOkPressed(String date){
        date_editText.setText(date);

    }
}
