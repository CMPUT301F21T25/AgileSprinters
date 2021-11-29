package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is a fragment allows a user to view all the details of a habit and edit any details
 * they wish to change.
 */
public class ViewEditHabitFragment extends DialogFragment {
    private int position;
    private int listSize;
    private String currentDate;
    private EditText currentHabitTitle;
    private EditText currentHabitReason;
    private TextView currentDateTextView;
    private TextView buttonError;
    private Button sundayButton;
    private Button mondayButton;
    private Button tuesdayButton;
    private Button wednesdayButton;
    private Button thursdayButton;
    private Button fridayButton;
    private Button saturdayButton;
    private Spinner privacySpinner;
    private Spinner positionSpinner;
    private Button[] weekdayEditButtonArray;
    private Integer progress;
    private HashMap<String, Boolean> weekdaysHashMap;
    private HashMap<String, Boolean> originalWeekdaysHashMap;
    private ViewEditHabitFragment.OnFragmentInteractionListener editFragmentListener;
    private String HID;
    private String UID;


    /**
     * This function saves the values sent to the fragment for future manipulation
     *
     * @param habit    is the item that was tapped within the list
     * @param position is the position of the item clicked
     * @return returns the fragment with the bundled parameters
     */
    public static ViewEditHabitFragment newInstance(Habit habit, int position, int listSize) {
        ViewEditHabitFragment frag = new ViewEditHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);
        args.putInt("position", position);
        args.putInt("listSize", listSize);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the Home class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onEditViewSaveChangesPressed(Habit habit, int position);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the Home class.
     *
     * @param context context of the current fragment
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AddHabitFragment.OnFragmentInteractionListener) {
            editFragmentListener = (ViewEditHabitFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.FRAG_ERROR_MSG));
        }
    }

    /**
     * This function creates the actual dialog on the screen and listens for user input, returning
     * the information through the listener based on which button is clicked.
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_edit_habit, null);

        // Connect all the elements of the screen to their appropriate counterparts
        currentHabitTitle = view.findViewById(R.id.view_edit_habit_title_editText);
        currentHabitReason = view.findViewById(R.id.view_edit_habit_reason_editText);
        currentDateTextView = view.findViewById(R.id.view_edit_habit_date);
        privacySpinner = view.findViewById(R.id.view_edit_privacy_spinner);
        positionSpinner = view.findViewById(R.id.view_edit_position_spinner);
        buttonError = view.findViewById(R.id.view_edit_habit_button_error);

        // Get weekday buttons and make sure they are set to blank
        sundayButton = view.findViewById(R.id.view_edit_button_sunday);
        mondayButton = view.findViewById(R.id.view_edit_button_monday);
        tuesdayButton = view.findViewById(R.id.view_edit_button_Tuesday);
        wednesdayButton = view.findViewById(R.id.view_edit_button_wednesday);
        thursdayButton = view.findViewById(R.id.view_edit_button_thursday);
        fridayButton = view.findViewById(R.id.view_edit_button_friday);
        saturdayButton = view.findViewById(R.id.view_edit_button_saturday);

        // Get the arguments that were stored in the bundle
        Habit habit = (Habit) getArguments().getSerializable(getString(R.string.HABIT_TEXT));
        position = getArguments().getInt(getString(R.string.POSITION_TEXT));
        listSize = getArguments().getInt(getString(R.string.HABIT_LIST_SIZE));

        HID = habit.getHID();
        UID = habit.getUID();


        // Set all of the screen elements to their original habit values for viewing
        currentHabitTitle.setText(habit.getTitle());
        currentHabitReason.setText(habit.getReason());
        currentDateTextView.setText("Date Started: " + habit.getDateToStart());
        originalWeekdaysHashMap = new HashMap<>();
        originalWeekdaysHashMap.putAll(habit.getWeekdays());
        weekdaysHashMap = new HashMap<>();
        weekdaysHashMap.putAll(habit.getWeekdays());
        currentDate = habit.getDateToStart();
        progress = habit.getOverallProgress();

        // Make sure spinner for privacy settings is set to the correct option
        if (habit.getPrivacySetting().equals(getString(R.string.PRIVATE_TEXT))) {
            privacySpinner.setSelection(1);
        } else {
            privacySpinner.setSelection(0);
        }

        //set up elements for position spinner and set the spinner to the habits current position
        ArrayList<Integer> listPositions = new ArrayList<Integer>();
        for (int i = 1; i < listSize + 1; i++) {
            listPositions.add(i);
        }

        ArrayAdapter<Integer> positionsAdapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, listPositions);
        positionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(positionsAdapter);
        positionSpinner.setSelection(position);


        // Array with all the Edit buttons for weekdays
        weekdayEditButtonArray = new Button[]{sundayButton, mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridayButton, saturdayButton};
        // Array with all the string values for weekdays
        String[] weekdayStrArray = new String[]{getString(R.string.SUNDAY_STR), getString(R.string.MONDAY_STR), getString(R.string.TUESDAY_STR),
                getString(R.string.WEDNESDAY_STR), getString(R.string.THURSDAY_STR), getString(R.string.FRIDAY_STR),
                getString(R.string.SATURDAY_STR)};

        // Set weekday buttons to proper colors based on the habit object passed in
        // and initialize the trackers for buttons pressed
        for (int i = 0; i < weekdayStrArray.length; i++) {
            if (weekdaysHashMap.get(weekdayStrArray[i])) {
                weekdayEditButtonArray[i].setBackgroundColor(Color.parseColor(getString(R.string.ORANGE_HEX_CODE)));
            }
        }

        //Set on click listeners for all weekday buttons
        for (int i = 0; i < weekdayEditButtonArray.length; i++) {
            int finalI = i;
            int finalI1 = i;
            weekdayEditButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (weekdaysHashMap.get(weekdayStrArray[finalI1]) == false) {
                        weekdayEditButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.ORANGE_HEX_CODE)));
                        weekdaysHashMap.replace(weekdayStrArray[finalI1], false, true);
                    } else {
                        weekdayEditButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.GREY_HEX_CODE)));
                        weekdaysHashMap.replace(weekdayStrArray[finalI1], true, false);
                    }
                }
            });
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(getString(R.string.VIEW_EDIT_HABIT_FRAG_TITLE))
                .setNegativeButton(getString(R.string.CANCEL_BTN_STR), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*
                         * Do not do anything here and instead define what to do below in overridden
                         * negative button.
                         */
                    }
                })
                .setPositiveButton(getString(R.string.SAVE_BTN_STR), new DialogInterface.OnClickListener() {
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
            Button negative = (Button) dialog.getButton(Dialog.BUTTON_NEGATIVE);

            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int j = 0;
                    for (String key : weekdaysHashMap.keySet()) {
                        if (weekdaysHashMap.get(key) == true && originalWeekdaysHashMap.get(key) == false) {
                            weekdayEditButtonArray[j].setBackgroundColor(Color.parseColor(getString(R.string.greyHexCode)));
                        } else if (weekdaysHashMap.get(key) == false && originalWeekdaysHashMap.get(key) == true) {
                            weekdayEditButtonArray[j].setBackgroundColor(Color.parseColor(getString(R.string.orangeHexCode)));
                        }
                        j++;
                    }

                    dialog.dismiss();
                }
            });

            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Boolean tracks when the all the fields have been filled out. Will turn to false
                    // if anything has been left blank.
                    Boolean readyToClose = true;

                    String newHabitTitle = currentHabitTitle.getText().toString();
                    String newHabitReason = currentHabitReason.getText().toString();
                    String newPrivacySetting = privacySpinner.getSelectedItem().toString();
                    int newPosition = positionSpinner.getSelectedItemPosition();

                    if (newHabitTitle.matches("")) {
                        readyToClose = false;
                        currentHabitTitle.setError(getString(R.string.EMPTY_FIELD_ERR_MSG));
                    }
                    if (newHabitReason.matches("")) {
                        readyToClose = false;
                        currentHabitReason.setError(getString(R.string.EMPTY_FIELD_ERR_MSG));
                    }
                    if (newPrivacySetting.matches("")) {
                        readyToClose = false;
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
                        editFragmentListener.onEditViewSaveChangesPressed(
                                new Habit(HID, UID, newHabitTitle, newHabitReason, currentDate,
                                        weekdaysHashMap, newPrivacySetting, progress, newPosition), position);
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
