package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This class is a fragment that asks the user if they wish to delete a habit and all of its events.
 */
public class deleteHabitFragment extends DialogFragment {
    private deleteHabitFragment.OnFragmentInteractionListener listener;

    /**
     * This function saves the position sent to the fragment for future manipulation
     * @param position is the item position that was tapped within the list
     * @return returns the fragment with the bundled parameters
     */
    public static deleteHabitFragment newInstance(int position) {
        //Keep track of item the user wishes to delete.
        deleteHabitFragment frag = new deleteHabitFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the Home class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onDeleteHabitYesPressed(int position);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the Home class.
     * @param context context of the current fragment
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if (context instanceof deleteHabitFragment.OnFragmentInteractionListener){
            listener = (deleteHabitFragment.OnFragmentInteractionListener) context;
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
     * Returns the dialog of the fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.delete_habit_fragment, null);

        // Get arguments from the bundle
        int position = getArguments().getInt("position");

        /**
         * This method builds the dialog of the fragment
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDeleteHabitYesPressed(position);
                    }
                }).create();
    }

}
