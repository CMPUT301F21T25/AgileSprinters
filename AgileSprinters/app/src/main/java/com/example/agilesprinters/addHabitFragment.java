package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a dialog fragment that allows the user to add a new habit.
 */
public class addHabitFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private EditText habitTitle;
    private EditText habitReason;
    private EditText dateEditText;
    private TextView buttonError;
    private Button sundayBtn;
    private Button mondayBtn;
    private Button tuesdayBtn;
    private Button wednesdayBtn;
    private Button thursdayBtn;
    private Button fridayBtn;
    private Button saturdayBtn;
    private String date = "";
    private HashMap<String, Boolean> weekdaysHashMap;
    private HashMap<String, Integer> progressSoFar;
    private Spinner privacySpinner;
    private int position;
    private String UID;
    private addHabitFragment.OnFragmentInteractionListener fragmentListener;
    FirebaseFirestore db;

    /**
     * This function saves the values sent to the fragment for future manipulation
     * @param UID is the id of the user
     * @return returns the fragment with the bundled parameters
     */
    public static addHabitFragment newInstance(String UID, int listSize) {
        addHabitFragment frag = new addHabitFragment();
        Bundle args = new Bundle();
        args.putString(String.valueOf(R.string.HID), UID);
        args.putInt("listSize", listSize);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This function captures the date chosen by the user once they press ok on the datePicker
     * fragment.
     *
     * @param view       the datePicker dialog view
     * @param year       year of the date chosen by the user
     * @param month      month of the date chosen by the user
     * @param dayOfMonth day of the month of the date chosen by the user
     */
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
        dateEditText.setText(date);
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the Home class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onAddPressed(Habit habit);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the Home class.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            fragmentListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * This function creates the actual dialog on the screen and listens for user input, returning
     * the information through the listener based on which button is clicked.
     *
     * @param savedInstanceState The instance state of the fragment.
     * @return Returns the Dialog created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment, null);

        UID = getArguments().getString(getString(R.string.HID));
        position = getArguments().getInt(getString(R.string.HABIT_LIST_SIZE));

        String[] weekdayStrArray = new String[]{getString(R.string.SUNDAY_STR), getString(R.string.MONDAY_STR), getString(R.string.TUESDAY_STR),
                getString(R.string.WEDNESDAY_STR), getString(R.string.THURSDAY_STR), getString(R.string.FRIDAY_STR),
                getString(R.string.SATURDAY_STR)};

        weekdaysHashMap = new HashMap<String, Boolean>();

        for (int i = 0; i < weekdayStrArray.length; i++) {
            weekdaysHashMap.put(weekdayStrArray[i], false);
        }

        habitTitle = view.findViewById(R.id.habit_title_editText);
        habitReason = view.findViewById(R.id.habit_reason_editText);
        dateEditText = view.findViewById(R.id.Date);
        privacySpinner = view.findViewById(R.id.privacy_spinner);
        buttonError = view.findViewById(R.id.add_habit_button_error);

        //set weekday buttons
        sundayBtn = view.findViewById(R.id.button_sunday);
        mondayBtn = view.findViewById(R.id.button_monday);
        tuesdayBtn = view.findViewById(R.id.button_Tuesday);
        wednesdayBtn = view.findViewById(R.id.button_wednesday);
        thursdayBtn = view.findViewById(R.id.button_thursday);
        fridayBtn = view.findViewById(R.id.button_friday);
        saturdayBtn = view.findViewById(R.id.button_saturday);

        //set on click listeners for all weekday buttons and the editText for the date started
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datePickerFragment();
                datePicker.show(getChildFragmentManager(), getString(R.string.DATE_PICKER_STR));
            }
        });

        Button[] weekdayButtonArray = new Button[]{sundayBtn, mondayBtn, tuesdayBtn, wednesdayBtn, thursdayBtn, fridayBtn, saturdayBtn};

        for (int i = 0; i < weekdayButtonArray.length; i++) {
            int finalI = i;
            weekdayButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (weekdaysHashMap.get(weekdayStrArray[finalI]) == false) {
                        weekdayButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.ORANGE_HEX_CODE)));
                        weekdaysHashMap.replace(weekdayStrArray[finalI], false, true);
                    } else {
                        weekdayButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.GREY_HEX_CODE)));
                        weekdaysHashMap.replace(weekdayStrArray[finalI], true, false);
                    }
                }
            });
        }

        /**
         * This method builds the dialog of the fragment
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(getString(R.string.ADD_HABIT_FRAG_TITLE))
                .setNegativeButton(getString(R.string.CANCEL_BTN_STR), null)
                .setPositiveButton(getString(R.string.ADD_BTN_STR), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* Do not implement anything here in order to override the button
                         * to only call the listener once all the information required has been
                         * filled out and display error messages if they have been left blank.
                         */
                    }
                }).create();

    }

    /**
     * This function overrides the buttons clicked in order to only allow the dialog to be dismissed
     * when all requirements have been met.
     */
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

                    String habit_title = habitTitle.getText().toString();
                    String habit_reason = habitReason.getText().toString();
                    String privacySetting = privacySpinner.getSelectedItem().toString();

                    if (habit_title.matches("")) {
                        readyToClose = false;
                        habitTitle.setError(getString(R.string.EMPTY_FIELD_ERR_MSG));
                    }
                    if (habit_reason.matches("")) {
                        readyToClose = false;
                        habitReason.setError(getString(R.string.EMPTY_FIELD_ERR_MSG));
                    }
                    if (privacySetting.matches("")) {
                        readyToClose = false;
                    }
                    if (date.matches("")) {
                        readyToClose = false;
                        dateEditText.setError(getString(R.string.EMPTY_FIELD_ERR_MSG));
                    }

                    Boolean weekdayCheck = false;
                    for (String i : weekdaysHashMap.keySet()) {
                        if (weekdaysHashMap.get(i)) {
                            weekdayCheck = true;
                            break;
                        }
                    }
                    if (!weekdayCheck) {
                        readyToClose = false;
                        buttonError.setText(getString(R.string.WEEKDAY_BTN_ERR_MSG));
                    }

                    // If everything has been filled out, call the listener and send the edited
                    // habit back to the Home class and dismiss the dialog.
                    if (readyToClose) {
                        User user = new User();
                        db = FirebaseFirestore.getInstance();
                        DocumentReference newHabitRef = db.collection(getString(R.string.HABIT_COLLECTION_PATH)).document();
                        fragmentListener.onAddPressed(new Habit(newHabitRef.getId(), user.getUserID(), habit_title
                                , habit_reason, date, weekdaysHashMap, privacySetting, 0, position));
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}