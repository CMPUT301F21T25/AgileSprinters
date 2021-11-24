package com.example.agilesprinters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a custom layout for items in the habit list on the home page.
 *
 * @author Hannah Desmarais
 */
public class habitListAdapter extends ArrayAdapter<Habit> {
    private final Context mContext;
    private final ArrayList<Habit> habitArrayList;

    /**
     * This function initializes the array list of habits and the context of the screen.
     *
     * @param context The current screen.
     * @param objects The arraylist of object we are using our custom adapter on.
     */
    public habitListAdapter(Context context, ArrayList<Habit> objects) {
        super(context, 0, objects);
        this.habitArrayList = objects;
        this.mContext = context;
    }

    /**
     * This method converts the view for the list into a custom view.
     *
     * @param position    Position of the habit.
     * @param convertView The converted view.
     * @param parent      The parent of the view we are changing.
     * @return Returns the view after it's been converted and it's values set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.home_list_content, parent, false);
        }

        Habit habit = habitArrayList.get(position);

        //attach variables to the textViews in the list
        TextView dateText = view.findViewById(R.id.habit_date_textView);
        TextView privacyText = view.findViewById(R.id.privacy_setting_list);
        TextView titleText = view.findViewById(R.id.habit_list_title_textView);
        TextView reasonText = view.findViewById(R.id.habit_list_reason_textView);
        ProgressBar progressSoFar = view.findViewById(R.id.habit_list_progress_bar);

        // Calculate progress

        // Getting the start date and end date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startDate = LocalDate.parse(habit.getDateToStart(), formatter);
        LocalDate endDate = LocalDate.now();

        // Getting the days of the week the habit is scheduled for
        ArrayList<String> chosenWeekDays = new ArrayList<>();
        for (Map.Entry me : habit.getWeekdays().entrySet()) {
            if (me.getValue().equals(true)) {
                chosenWeekDays.add((String) me.getKey());
            }
        }

        // Getting the total number of events planned between start and end date
        double totalEvents = 0;
        for (String weekday : chosenWeekDays) {
            LocalDate day = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(weekday)));
            while (day.isBefore(endDate)){
                totalEvents++;
                day = day.plusWeeks(1);
            }
        }

//        System.out.println("Overall progress " + habit.getOverallProgress());
//        System.out.println("Total events are " + totalEvents);
//        System.out.println("Progress is " + progress);
//        System.out.println("Progress percent " + progressPercent);

        // Calculating the progress percentage
        double progress = (double) habit.getOverallProgress() / (totalEvents);
        double progressPercent = progress * 100;

        //pass values to variables
        dateText.setText(mContext.getString(R.string.DATE_STARTED) + habit.getDateToStart());
        privacyText.setText(habit.getPrivacySetting());
        titleText.setText(habit.getTitle());
        reasonText.setText(habit.getReason());
        progressSoFar.setProgress((int) progressPercent);

        return view;
    }
}