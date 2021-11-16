package com.example.agilesprinters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

/**
 * This class is a dialog fragment that allows a user to choose a date to go back and check past
 * habit events
 *
 * @author Hannah Desmarais
 */
public class datePickerFragment extends DialogFragment {

    /**
     * This function creates the dialog screen and listens for the date the user chooses
     * @param savedInstanceState is a reference to the most recent object
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getParentFragment(), year, month, day);
    }

}