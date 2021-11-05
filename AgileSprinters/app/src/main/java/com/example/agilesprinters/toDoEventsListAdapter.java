package com.example.agilesprinters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class provides a custom layout for items in the to do habit list on the user calendar page.
 */
public class toDoEventsListAdapter extends ArrayAdapter<Habit> {

    private Context mContext;
    private ArrayList<Habit> habits;

    public toDoEventsListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.mContext = context;
        this.habits = habits;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.todo_habits_content, parent,false);
        }

        Habit habit = habits.get(position);

        // attach and pass variables to the textviews in the list
        TextView habitTitle = convertView.findViewById(R.id.habit_text);
        habitTitle.setText(habit.getTitle());

        return convertView;
    }
}