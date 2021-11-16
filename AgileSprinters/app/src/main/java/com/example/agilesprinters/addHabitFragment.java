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

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * This class is a dialog fragment that allows the user to add a new habit.
 *
 * @author Hannah Desmarais and Hari Bheesetti
 */
public class addHabitFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private EditText habitTitle;
    private EditText habitReason;
    private EditText date_editText;
    private TextView buttonError;
    private Button sunday;
    private Button monday;
    private Button tuesday;
    private Button wednesday;
    private Button thursday;
    private Button friday;
    private Button saturday;
    private String date = "";
    private HashMap<String, Boolean> weekdays;
    private Spinner privacy;
    private String UID;
    private addHabitFragment.OnFragmentInteractionListener listener;
    FirebaseFirestore db;

    /**
     * This function saves the values sent to the fragment for future manipulation
     * @param UID is the id of the user
     * @return returns the fragment with the bundled parameters
     */
    public static addHabitFragment newInstance(String UID) {
        addHabitFragment frag = new addHabitFragment();
        Bundle args = new Bundle();
        args.putString("UID", UID);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This function captures the date chosen by the user once they press ok on the datePicker
     * fragment.
     * @param view the datePicker dialog view
     * @param year year of the date chosen by the user
     * @param month month of the date chosen by the user
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
        if(month+1 < 10) date+= "0";
        date += String.valueOf(month + 1) + "/";
        if (dayOfMonth < 10 ) date += "0";
        date += String.valueOf(dayOfMonth + "/");
        date += String.valueOf(year);
        date_editText.setText(date);
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
     * @param context
     */
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


    /**
     * This function creates the actual dialog on the screen and listens for user input, returning
     * the information through the listener based on which button is clicked.
     * @param savedInstanceState The instance state of the fragment.
     * @return
     * Returns the Dialog created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_fragment, null);

        UID = getArguments().getString("UID");

        String[] weekdayStrArray = new String[]{ getString(R.string.mondayStr), getString(R.string.tuesdayStr),
                getString(R.string.wednesdayStr), getString(R.string.thursdayStr), getString(R.string.fridayStr),
                getString(R.string.saturdayStr), getString(R.string.sundayStr)};

        weekdays = new HashMap<String, Boolean>();

        for(int i = 0; i < weekdayStrArray.length; i++){
            weekdays.put(weekdayStrArray[i], false);
        }

        habitTitle = view.findViewById(R.id.habit_title_editText);
        habitReason = view.findViewById(R.id.habit_reason_editText);
        date_editText = view.findViewById(R.id.Date);
        privacy = view.findViewById(R.id.privacy_spinner);
        buttonError = view.findViewById(R.id.add_habit_button_error);

        //set weekday buttons
        sunday = view.findViewById(R.id.button_sunday);
        monday = view.findViewById(R.id.button_monday);
        tuesday = view.findViewById(R.id.button_Tuesday);
        wednesday = view.findViewById(R.id.button_wednesday);
        thursday = view.findViewById(R.id.button_thursday);
        friday = view.findViewById(R.id.button_friday);
        saturday = view.findViewById(R.id.button_saturday);

        //set on click listeners for all weekday buttons and the editText for the date started
        date_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datePickerFragment();
                datePicker.show(getChildFragmentManager(), "DATE PICKER");
            }
        });

        Button[]  weekdayButtonArray = new Button[]{sunday, monday, tuesday, wednesday, thursday, friday, saturday};

        for( int i = 0; i < weekdayButtonArray.length; i++){
            int finalI = i;
            int finalI1 = i;
            weekdayButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (weekdays.get(weekdayStrArray[finalI1]) == false) {
                        weekdayButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.orangeHexCode)));
                        weekdays.replace(weekdayStrArray[finalI1], false, true);
                    } else {
                        weekdayButtonArray[finalI].setBackgroundColor(Color.parseColor(getString(R.string.greyHexCode)));
                        weekdays.replace(weekdayStrArray[finalI1], true, false);
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
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
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
                    if (date.matches("")) {
                        readyToClose = false;
                        date_editText.setError("This field cannot be blank");
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
                        User user = new User();
                        db = FirebaseFirestore.getInstance();
                        DocumentReference newHabitRef = db.collection("Habit").document();
                        listener.onAddPressed(new Habit(newHabitRef.getId(),user.getUser(),habit_title
                                ,habit_reason,date, weekdays, privacySetting));
                        dialog.dismiss();
                    }
                }
            });
        }
    }
}
