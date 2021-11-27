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
 * This class is a fragment that asks the user if they wish to share a
 * private habit event to the forum.
 */
public class shareHabitEventFragment extends DialogFragment {
    private shareHabitEventFragment.OnFragmentInteractionListener listener;

    /**
     * This function saves the position sent to the fragment for future manipulation
     *
     * @param instance is the item position that was tapped within the list
     * @return returns the fragment with the bundled parameters
     */
    public static shareHabitEventFragment newInstance(HabitInstance instance) {
        //Keep track of item the user wishes to delete.
        shareHabitEventFragment frag = new shareHabitEventFragment();
        Bundle args = new Bundle();
        args.putSerializable("Event", instance);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the User calendar class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onShareHabitEventYesPressed(HabitInstance eventToShare);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the User calendar class.
     *
     * @param context context of the current fragment
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof shareHabitEventFragment.OnFragmentInteractionListener) {
            listener = (shareHabitEventFragment.OnFragmentInteractionListener) context;
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.share_habit_event_fragment, null);

        // Get arguments from the bundle
        HabitInstance habitInstance = (HabitInstance) getArguments().getSerializable("Event");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton(getString(R.string.NO_STR), null)
                .setPositiveButton(getString(R.string.YES_STR), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onShareHabitEventYesPressed(habitInstance);
                    }
                }).create();
    }

}