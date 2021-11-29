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
 * This class builds a list of users that is displayed in the followFollowing activity
 *
 * @author Hari
 */
public class CustomUserListAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public CustomUserListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }

    /**
     * This function takes the users and builds a list of their names
     *
     * @param position    {@link Integer} position in list
     * @param convertView {@link View} view passed from followFollowing
     * @param parent      {@link ViewGroup} the view of the parent
     * @return {@link View} returns the view that we built in this class
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_user, parent, false);
        }

        User user = users.get(position);

        TextView userFullNameTextView = view.findViewById(R.id.user_name_text_view);
        String temp = user.getFirstName() + " " + user.getLastName();
        userFullNameTextView.setText(temp);

        return view;
    }
}
