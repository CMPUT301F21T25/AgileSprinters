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
    private CheckBox not_completed;
    private EditText optional_comment;
    private EditText input_date;
    private EditText input_duration;
    private TextView habitTitle;
    private String title;

    private editHabitEventFragment.OnFragmentInteractionListener listener;

    public static editHabitEventFragment newInstance(int position) {
        editHabitEventFragment fragment = new editHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onEditViewOkPressed(int position);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof addHabitFragment.OnFragmentInteractionListener){
            listener = (editHabitEventFragment.OnFragmentInteractionListener) context;
        }
        else{
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
