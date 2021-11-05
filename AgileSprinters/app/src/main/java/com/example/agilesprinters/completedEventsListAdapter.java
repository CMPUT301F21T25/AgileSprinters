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

public class completedEventsListAdapter extends ArrayAdapter<HabitInstance> {

    private Context mContext;
    int mResource;

    public completedEventsListAdapter(Context context, int resource, ArrayList<HabitInstance> events) {
        super(context, resource, events);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        HabitInstance habitInstance = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView event_content = convertView.findViewById(R.id.EventContent);
        TextView duration_content = convertView.findViewById(R.id.duration_content);

        event_content.setText(habitInstance.getOpt_comment().toString());
        duration_content.setText(String.valueOf(habitInstance.getDuration()) + " minutes");

        return convertView;
    }
}