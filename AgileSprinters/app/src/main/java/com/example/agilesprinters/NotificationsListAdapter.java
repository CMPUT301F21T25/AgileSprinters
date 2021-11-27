package com.example.agilesprinters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class is a list adapter for the Notification screens notification list. It will format an
 * item in the list to hold a user's full name and the string "has requested to follow you."
 *
 * @author Hannah Desmarais
 */
public class NotificationsListAdapter extends ArrayAdapter<User> {
    private final Context mContext;
    private final ArrayList<User> notificationsArrayList;

    /**
     * This function initializes the array list of users and the context of the screen.
     *
     * @param context The current screen.
     * @param objects The arraylist of object we are using our custom adapter on.
     */
    public NotificationsListAdapter(Context context, ArrayList<User> objects) {
        super(context, 0, objects);
        this.notificationsArrayList = objects;
        this.mContext = context;
    }

    /**
     * This method converts the view for the list into a custom view.
     *
     * @param position    Position of the user.
     * @param convertView The converted view.
     * @param parent      The parent of the view we are changing.
     * @return
     * Returns the view after it's been converted and it's values set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.notifications_content, parent, false);
        }

        User user = notificationsArrayList.get(position);

        TextView userName = view.findViewById(R.id.requesting_user);
        userName.setText(user.getFirstName() + " " + user.getLastName());

        return view;
    }
}