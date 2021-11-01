package com.example.agilesprinters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides a custom layout for items in the habit list on the home page.
 */
public class habitListAdapter extends ArrayAdapter<Habit> {
    private Context mContext;
    int mResource;

    public habitListAdapter(Context context, int resource, ArrayList<Habit> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get information about the habit
        String title = getItem(position).getTitle();
        String reason = getItem(position).getReason();
        String dateStarted = getItem(position).getDateToStart();
        String privacy = getItem(position).getPrivacySetting();
        HashMap<String,Boolean> weekdays = getItem(position).getWeekdays();

        //create habit object with that information
        Habit habit = new Habit(title, reason, dateStarted, weekdays, privacy);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //attach variables to the textViews in the list
        TextView tvDate = (TextView) convertView.findViewById(R.id.habit_date_textView);
        TextView tvPrivacy = (TextView) convertView.findViewById(R.id.privacy_setting_list);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.habit_list_title_textView);
        TextView tvReason = (TextView) convertView.findViewById(R.id.habit_list_reason_textView);

        //pass values to variables
        tvDate.setText("  Date Started: " + String.valueOf(dateStarted));
        tvTitle.setText(String.valueOf(title));
        tvPrivacy.setText(String.valueOf(privacy));
        tvReason.setText(String.valueOf(reason));

        return convertView; //return the converted view
    }
}
