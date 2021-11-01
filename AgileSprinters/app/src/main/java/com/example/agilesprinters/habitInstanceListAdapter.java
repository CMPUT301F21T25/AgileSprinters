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

public class habitInstanceListAdapter {
    /**
    private Context mContext;
    int mResource;

    public habitInstanceListAdapter(Context context, int resource, ArrayList<HabitInstance> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get information about the habit event
        int uniqueID = getItem(position).getUniqueId();
        boolean status = getItem(position).isStatus();
        String opt_comment = getItem(position).getOpt_comment();
        String date = getItem(position).getDate();
        int duration = getItem(position).getDuration();

        //create habit instance object with that information
        HabitInstance habitInstance = new HabitInstance(uniqueID, status, opt_comment, date, duration);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //attach variables to the textViews in the list
        CheckBox box_status = (CheckBox) convertView.findViewById(R.id.checkBox_completed);
        EditText comment = (EditText) convertView.findViewById(R.id.editText_comment);
        EditText box_date = (EditText) convertView.findViewById(R.id.editText_date);
        EditText box_duration = (EditText) convertView.findViewById(R.id.editText_duration);

        //pass values to variables
        if (status) {
            box_status.setChecked(true);
        }

        comment.setText(String.valueOf(opt_comment));
        box_date.setText(String.valueOf(date));
        box_duration.setText(String.valueOf(duration));

        return convertView; //return the converted view
    }**/
}