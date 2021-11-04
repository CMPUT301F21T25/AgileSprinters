package com.example.agilesprinters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class completedEventsListAdapter extends ArrayAdapter<HabitInstance> {

    private Context mContext;
    int mResource;
    FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();
        if (habitInstance.getOpt_comment().matches("")) {
            db.collection("Habit").addSnapshotListener((value, error) -> {
                for(QueryDocumentSnapshot doc: value) {
                    if (doc.getId().equals(habitInstance.getHID())){
                        event_content.setText(doc.getString("Title"));
                    }
                }
            });
        } else {
            event_content.setText(habitInstance.getOpt_comment());
        }

        duration_content.setText(String.valueOf(habitInstance.getDuration()) + " minutes");

        return convertView;
    }
}