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

public class NotificationsListAdapter extends ArrayAdapter<User> {
    private final Context mContext;
    private final ArrayList<User> notificationsArrayList;

    public NotificationsListAdapter(Context context, ArrayList<User> objects) {
        super(context, 0, objects);
        this.notificationsArrayList = objects;
        this.mContext = context;
    }

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