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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This class is a dialog fragment which allows a user to choose to either accept or decline a
 * follow request from an other user.
 *
 * @author Hannah Desmarais
 */
public class AcceptDeclineFollowRequestFragment extends DialogFragment {
    private AcceptDeclineFollowRequestFragment.OnFragmentInteractionListener fragmentListener;
    private User followRequestUser;

    /**
     * This function saves the values sent to the fragment for future manipulation
     * @param user The user has sent the follow request.
     * @return
     * Returns the fragment with the bundled parameters.
     */
    public static AcceptDeclineFollowRequestFragment newInstance(User user) {
        AcceptDeclineFollowRequestFragment frag = new AcceptDeclineFollowRequestFragment();
        Bundle args = new Bundle();
        args.putSerializable("followRequestUser", user);
        frag.setArguments(args);

        return frag;
    }

    /**
     * This is an interface which implements a function in the Notifications class based on what
     * button the user has pressed.
     */
    public interface OnFragmentInteractionListener {
        void onAcceptPressed(User user);
        void onDeclinePressed(User user);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the Notifications class.
     * @param context The current screen.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof AcceptDeclineFollowRequestFragment.OnFragmentInteractionListener) {
            fragmentListener = (AcceptDeclineFollowRequestFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.FRAG_ERROR_MSG));
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.accept_decline_follow_request_fragment, null);

        //Get user who is requesting to follow from the bundle
        followRequestUser = (User) getArguments().getSerializable("followRequestUser");

        //Set the text view with the requesting users name
        TextView requestingUserTV = view.findViewById(R.id.accept_decline_user_TextView);
        requestingUserTV.setText(followRequestUser.getFirstName() + " " + followRequestUser.getLastName());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setView(view)
                .setTitle("Follow Request")
                .setNegativeButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fragmentListener.onAcceptPressed(followRequestUser);
                    }
                })
                .setPositiveButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fragmentListener.onDeclinePressed(followRequestUser);
                    }
                });

        //Set the positive and negative buttons to a custom look
        AlertDialog alert = builder.create();
        alert.show();
        Button accept = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        accept.setBackgroundColor(Color.parseColor(getString(R.string.ACCEPT_BUTTON_COLOR)));
        accept.setTextColor(Color.BLACK);
        accept.setMinWidth(435);

        Button decline = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        decline.setBackgroundColor(Color.parseColor(getString(R.string.DECLINE_BUTTON_COLOR)));
        decline.setTextColor(Color.BLACK);
        decline.setMinWidth(435);

        return alert;
    }

}
