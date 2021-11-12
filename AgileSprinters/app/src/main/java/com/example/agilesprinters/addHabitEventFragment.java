package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is a dialog fragment that allows the user to add a new habit event.
 */
public class addHabitEventFragment extends DialogFragment{
    private int position;
    private String EID;
    private String UID;
    private String HID;

    private EditText optional_comment;
    private TextView input_date;
    private EditText input_duration;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private addHabitEventFragment.OnFragmentInteractionListener listener;

    /**
     * This function saves the values sent to the fragment for future manipulation
     * @param UID is the id of the user
     * @param HID is the id of the habit
     * @param EID is the id of the instance
     * @param position is the selected item position
     * @return returns the fragment with the bundled parameters
     */
    public static addHabitEventFragment newInstance(int position, String UID, String HID, String EID) {
        addHabitEventFragment fragment = new addHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("UID", UID);
        args.putString("HID", HID);
        args.putString("EID", EID);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the User Calendar class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onSavePressed(HabitInstance habitInstance);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the User calendar class.
     * @param context is the current screen
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This function creates the actual dialog on the screen and listens for user input, returning
     * the information through the listener based on which button is clicked.
     * @param savedInstanceState is a reference to the most recent object
     * @return
     * Returns the Dialog created
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        // Display the calendar
        optional_comment = view.findViewById(R.id.editText_comment);
        input_date = view.findViewById(R.id.editText_date);
        input_duration = view.findViewById(R.id.editText_duration);

        UID = getArguments().getString(getString(R.string.uidKey));
        HID = getArguments().getString(getString(R.string.hidKey));
        EID = getArguments().getString(getString(R.string.eidKey));

        LocalDate currentDate = LocalDate.now();
        input_date.setText(currentDate.format(formatter));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    /* Do not implement anything here in order to override the button
                     * to only call the listener once all the information required has been
                     * filled out and display error messages if they have been left blank.
                     */
                }).create();

    }

    /**
     * This function overrides the buttons clicked in order to only allow the dialog to be dismissed
     * when all requirements have been met.
     */
    @Override
    public void onResume(){
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();
        if(dialog != null){
            Button positive = dialog.getButton(Dialog.BUTTON_POSITIVE);

            positive.setOnClickListener(view -> {
                // Boolean tracks when the all the fields have been filled out. Will turn to false
                // if anything has been left blank.
                boolean readyToClose = true;

                String comment = optional_comment.getText().toString();
                String date_entry = input_date.getText().toString();
                String duration = input_duration.getText().toString();

                if (comment.length() > 20) {
                    readyToClose = false;
                    optional_comment.setError("This field cannot have more than 20 chars");
                }

                if (date_entry.matches("")) {
                    readyToClose = false;
                    input_date.setError("This field cannot be blank");
                }


                if (duration.matches("")) {
                    readyToClose = false;
                    input_duration.setError("This field cannot be blank");
                }

                // If everything has been filled out, call the listener and send the edited
                // habit back to the Home class and dismiss the dialog.
                if(readyToClose){
                    listener.onSavePressed(new HabitInstance(EID, UID, HID, comment, date_entry,
                            Integer.parseInt(duration)));
                    dialog.dismiss();
                }
            });
        }
    }

}
