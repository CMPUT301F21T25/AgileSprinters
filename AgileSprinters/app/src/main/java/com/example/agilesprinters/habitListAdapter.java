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
    private ArrayList<Habit> habits;

    public habitListAdapter(Context context,  ArrayList<Habit> objects) {
        super(context,0, objects);
        this.habits = objects;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.home_list_content, parent, false);
        }

        Habit habit = habits.get(position);

        //attach variables to the textViews in the list
        TextView tvDate = view.findViewById(R.id.habit_date_textView);
        TextView tvPrivacy = view.findViewById(R.id.privacy_setting_list);
        TextView tvTitle = view.findViewById(R.id.habit_list_title_textView);
        TextView tvReason = view.findViewById(R.id.habit_list_reason_textView);

        //pass values to variables
        tvDate.setText("  Date Started: " + habit.getDateToStart());
        tvTitle.setText(habit.getTitle());
        tvPrivacy.setText(habit.getPrivacySetting());
        tvReason.setText(habit.getReason());

        return view;
    }
}
