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
 * This class provides a custom layout for items in the to do habit list on the user calendar page.
 */
public class toDoEventsListAdapter extends ArrayAdapter<Habit> {

    private final Context mContext;
    private final ArrayList<Habit> habits;

    /**
     * This function initializes the current screen and the list of habits to be
     * displayed on the screen
     * @param context is the current screen
     * @param habits are the list of planned habits for the day
     */
    public toDoEventsListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.mContext = context;
        this.habits = habits;
    }

    /**
     * This function converts the view into a custom view
     * @param position is the position of the habit
     * @param convertView is the view to be displayed in
     * @param parent is the parent of the view that is being changed
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.todo_habits_content, parent, false);
        }

        Habit habit = habits.get(position);

        // attach and pass variables to the textview in the list
        TextView habitTitle = convertView.findViewById(R.id.habit_text);
        habitTitle.setText(habit.getTitle());

        return convertView;
    }
}