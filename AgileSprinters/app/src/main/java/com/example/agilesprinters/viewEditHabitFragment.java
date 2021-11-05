package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
 *
 * @author Hannah Desmarais and Hari Bheesetti
 */
public class viewEditHabitFragment extends DialogFragment {
    private int position;
    private String date;
    private EditText habitTitle;
    private EditText habitReason;
    private TextView date_textView;
    private TextView buttonError;
    private Button sunday;
    private Button monday;
    private Button tuesday;
    private Button wednesday;
    private Button thursday;
    private Button friday;
    private Button saturday;
    private Spinner privacy;
    private ArrayList<String> originalWeekdays;
    private HashMap<String, Boolean> weekdays;
    private viewEditHabitFragment.OnFragmentInteractionListener listener;
    private String HID;
    private String UID;


    /**
     * This function saves the values sent to the fragment for future manipulation

     * @param habit is the item that was tapped within the list
     * @return returns the fragment with the bundled parameters
     */
    public static viewEditHabitFragment newInstance(Habit habit) {
        viewEditHabitFragment frag = new viewEditHabitFragment();
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the Home class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onEditViewSaveChangesPressed(Habit habit);
        void onEditViewCancelPressed();
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

        if (context instanceof addHabitFragment.OnFragmentInteractionListener) {
            listener = (viewEditHabitFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_edit_habit_fragment, null);

        // Connect all the elements of the screen to their appropriate counterparts
        habitTitle = view.findViewById(R.id.view_edit_habit_title_editText);
        habitReason = view.findViewById(R.id.view_edit_habit_reason_editText);
        date_textView = view.findViewById(R.id.view_edit_habit_date);
        privacy = view.findViewById(R.id.view_edit_privacy_spinner);
        buttonError = view.findViewById(R.id.view_edit_habit_button_error);

        // Get weekday buttons and make sure they are set to blank
        sunday = view.findViewById(R.id.view_edit_button_sunday);
        monday = view.findViewById(R.id.view_edit_button_monday);
        tuesday = view.findViewById(R.id.view_edit_button_Tuesday);
        wednesday = view.findViewById(R.id.view_edit_button_wednesday);
        thursday = view.findViewById(R.id.view_edit_button_thursday);
        friday = view.findViewById(R.id.view_edit_button_friday);
        saturday = view.findViewById(R.id.view_edit_button_saturday);

        // Get the arguments that were stored in the bundle
        Habit habit = (Habit) getArguments().getSerializable("habit");
        position = getArguments().getInt("position");

        HID = habit.getHID();
        UID = habit.getUID();


        // Set all of the screen elements to their original habit values for viewing
        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        date_textView.setText("Date Started: " + habit.getDateToStart());
        weekdays = habit.getWeekdays();
        date = habit.getDateToStart();


        // Make sure spinner for privacy settings is set to the correct option
        if (habit.getPrivacySetting().equals("Private")) {
            privacy.setSelection(1);
        }
        // Array with all the Edit buttons for weekdays
        Button[]  weekdayEditButtonArray = new Button[]{sunday, monday, tuesday, wednesday, thursday, friday, saturday};
        // Array with all the string values for weekdays
        String[] weekdayStrArray = new String[]{ getString(R.string.mondayStr), getString(R.string.tuesdayStr),
                getString(R.string.wednesdayStr), getString(R.string.thursdayStr), getString(R.string.fridayStr),
                getString(R.string.saturdayStr), getString(R.string.sundayStr)};

        // Set weekday buttons to proper colors based on the habit object passed in
        // and initialize the trackers for buttons pressed
        for(int i = 0; i < weekdayStrArray.length; i++){
            if(weekdays.get(weekdayStrArray[i])) {
                weekdayEditButtonArray[i].setBackgroundColor(Color.parseColor(getString(R.string.orangeHexCode)));
            }
        }

        //Set on click listeners for all weekday buttons
        for( int i = 0; i < weekdayEditButtonArray.length; i++){
            int finalI = i;
            int finalI1 = i;
            weekdayEditButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (weekdays.get(weekdayStrArray[finalI1]) == false) {
                        weekdayEditButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.orangeHexCode)));
                        weekdays.replace(weekdayStrArray[finalI1], false, true);
                    } else {
                        weekdayEditButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.greyHexCode)));
                        weekdays.replace(weekdayStrArray[finalI1], true, false);
                    }
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View/Edit Habit")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
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
                    String privacySetting = privacy.getSelectedItem().toString();

                    if (habit_title.matches("")) {
                        readyToClose = false;
                        habitTitle.setError("This field cannot be blank");
                    }
                    if (habit_reason.matches("")) {
                        readyToClose = false;
                        habitReason.setError("This field cannot be blank");
                    }
                    if (privacySetting.matches("")) {
                        readyToClose = false;
                    }

                    Boolean weekdayCheck = false;
                    for (String i : weekdays.keySet()) {
                        if (weekdays.get(i)) {
                            weekdayCheck = true;
                            break;
                        }
                    }
                    if (!weekdayCheck) {
                        readyToClose = false;
                        buttonError.setText("Please choose which days you would like this event to occur.");
                    }

                    // If everything has been filled out, call the listener and send the edited
                    // habit back to the Home class and dismiss the dialog.
                    if (readyToClose) {
                        listener.onEditViewSaveChangesPressed(new Habit(HID, UID, habit_title, habit_reason, date, weekdays, privacySetting));
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}

