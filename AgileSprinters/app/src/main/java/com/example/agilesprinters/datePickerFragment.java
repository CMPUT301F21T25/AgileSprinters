package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class datePickerFragment extends DialogFragment {
    private DatePicker dateStarted;
    private String date;
    private datePickerFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onDatePickerOkPressed(String date);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof datePickerFragment.OnFragmentInteractionListener){
            listener = (datePickerFragment.OnFragmentInteractionListener) context;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_fragment, null);

        dateStarted = view.findViewById(R.id.date_started_DatePicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        date = String.valueOf(dateStarted.getYear()) + "/";
                        if (dateStarted.getMonth()+1 < 10) date += "0";
                        date += String.valueOf(dateStarted.getMonth() + 1) + "/";
                        if (dateStarted.getDayOfMonth() < 10 ) date += "0";
                        date += String.valueOf(dateStarted.getDayOfMonth());

                        listener.onDatePickerOkPressed(date);
                    }
                }).create();
    }

}