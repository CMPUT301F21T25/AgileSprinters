package com.example.agilesprinters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This class provides a custom layout for items in the completed events list on the user calendar page.
 */
public class completedEventsListAdapter extends ArrayAdapter<HabitInstance> {

    private final Context mContext;
    int mResource;
    FirebaseFirestore db;

    /**
     * This function initializes the current screen and the list of habits to be
     * displayed on the screen
     *
     * @param context  is the current screen
     * @param resource is the class path
     * @param events   are the list of completed events for the day
     * @author Sai Rasazna Ajerla and Riyaben Patel
     */
    public completedEventsListAdapter(Context context, int resource, ArrayList<HabitInstance> events) {
        super(context, resource, events);
        this.mContext = context;
        this.mResource = resource;
    }

    /**
     * This function converts the view into a custom view
     *
     * @param position    is the position of the habit
     * @param convertView is the view to be displayed in
     * @param parent      is the parent of the view that is being changed
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HabitInstance habitInstance = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        // attach and pass variables to the textview in the list
        TextView eventContent = convertView.findViewById(R.id.EventContent);
        TextView durationContent = convertView.findViewById(R.id.duration_content);
        TextView locationContent = convertView.findViewById(R.id.location_content);
        // Check if optional comment is empty or not and pass the content to TextView accordingly
        db = FirebaseFirestore.getInstance();
        if (habitInstance.getOpt_comment().matches("")) {
            db.collection("Habit").addSnapshotListener((value, error) -> {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc.getId().equals(habitInstance.getHID())) {
                        eventContent.setText(doc.getString("Title"));
                    }
                }
            });
        } else {
            eventContent.setText(habitInstance.getOpt_comment());
        }
        locationContent.setText(getDisplayLocStr(habitInstance.getOptLoc()));
        durationContent.setText(habitInstance.getDuration() + " minutes");
        return convertView;
    }
    private String getDisplayLocStr(String opt_loc){
        if (opt_loc==null) return "";
        System.out.println(opt_loc);
        if (opt_loc.equals("")) return "";

        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String[] latLng = opt_loc.split(",");
        System.out.println(latLng);
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]),1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        return city+", "+state+", "+country;
    }
}