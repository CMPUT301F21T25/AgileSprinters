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

public class editHabitEventFragment extends DialogFragment {
    private int position;
    private CheckBox completed;
    private EditText optional_comment;
    private EditText input_date;
    private EditText input_duration;

    private editHabitEventFragment.OnFragmentInteractionListener listener;

    public static editHabitEventFragment newInstance(int position, HabitInstance habitInstance) {
        editHabitEventFragment fragment = new editHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("Habit instance", habitInstance);
        fragment.setArguments(args);

        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onEditSavePressed(HabitInstance instance, int position);
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
        completed = view.findViewById(R.id.checkBox_completed);
        optional_comment = view.findViewById(R.id.editText_comment);
        input_date = view.findViewById(R.id.editText_date);
        input_duration = view.findViewById(R.id.editText_duration);

        HabitInstance habitInstance = (HabitInstance) getArguments().getSerializable("Habit instance");
        position = getArguments().getInt("position");

        if (habitInstance.isStatus()) {
            completed.setChecked(true);
        }

        optional_comment.setText(habitInstance.getOpt_comment());
        input_date.setText(habitInstance.getDate());
        input_duration.setText(String.valueOf(habitInstance.getDuration()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View/Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean checked = completed.isChecked();
                        String comment = optional_comment.getText().toString();
                        String date = input_date.getText().toString();
                        int duration = Integer.parseInt(input_duration.getText().toString());

                        listener.onEditSavePressed(new HabitInstance(position, checked, comment, date, duration), position);
                    }
                }).create();

    }
}
