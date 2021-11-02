package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserCalendar extends AppCompatActivity
        implements addHabitEventFragment.OnFragmentInteractionListener,
        editHabitEventFragment.OnFragmentInteractionListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = "Instance";

    private ListView toDoEventsList;
    private ArrayAdapter<Habit> toDoEventAdapter;
    private ArrayList<Habit> toDoEvents = new ArrayList<>();

    private ListView completedEventsList;
    private ArrayAdapter<HabitInstance> completedEventAdapter;
    private ArrayList<HabitInstance> completedEvents = new ArrayList<>();
    private ArrayList<String> completedEventIds = new ArrayList<>();
    private ArrayList<String> toDoEventIds = new ArrayList<>();

    private TextView title1;
    private Button calendar_button;
    FirebaseFirestore db;
    private String loggedInId = "nXmcIP2McwOw89GpWW10xt02JzG2";

    private final ArrayList<HabitInstance> habitEvents_list = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private Habit selectedHabit;
    private HabitInstance selectedHabitInstance;
    private String selectedHabitInstanceId;
    LocalDate currentDate = LocalDate.now();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        toDoEventsList = findViewById(R.id.toDoEventsList);
        completedEventsList = findViewById(R.id.completedEventsList);

        title1 = findViewById(R.id.title1);
        calendar_button = findViewById(R.id.calendar_button);

        db = FirebaseFirestore.getInstance();

        screenSetup();
        completedEventsScreenSetup();

        toDoEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHabit = toDoEvents.get(i);
                selectedHabitInstanceId = toDoEventIds.get(i);

                // get hid here
                addHabitEventFragment values =
                        new addHabitEventFragment().newInstance(i, loggedInId, selectedHabitInstanceId);
                values.show(getSupportFragmentManager(), "ADD");
            }
        });

        completedEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHabitInstance = completedEvents.get(i);
                selectedHabitInstanceId = completedEventIds.get(i);

                editHabitEventFragment values =
                        new editHabitEventFragment().newInstance(i, selectedHabitInstance);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new datePickerCalendar();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        toDoEventAdapter = new toDoEventsListAdapter(this, toDoEvents);
        completedEventAdapter = new completedEventsListAdapter(this, R.layout.completed_habits_content, completedEvents);

        toDoEventsList.setAdapter(toDoEventAdapter);
        completedEventsList.setAdapter(completedEventAdapter);
    }

    public void setDate() {
        String formattedDate = currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        title1.setText(formattedDate + ")");
    }

    public void screenSetup() {
        HashMap<String, Boolean > days = new HashMap<>();
        days.put("MONDAY", true);
        days.put("WEDNESDAY", false);
        days.put("SUNDAY", true);
        setDate();


        // get habits to do today (need to use habits collection to do this)

        // Gives the day of the week
        String todayDay = currentDate.getDayOfWeek().toString();

        // Get a list of habits
        // of the user logged in, start date before today
        // add days properly

        db.collection("Habit").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                toDoEvents.clear();
                toDoEventIds.clear();
                for(QueryDocumentSnapshot doc: value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Title")));

                    // Gives the start date
                    LocalDate startDate = LocalDate.parse(doc.getString("Data to Start"), formatter);

                    if (doc.getString("UID").equals(loggedInId)
                            && (startDate.isBefore(currentDate) || startDate.isEqual(currentDate)) ){
                        Habit newHabit = new Habit(doc.getString("Title"), doc.getString("Reason"),
                                doc.getString("Data to Start"), days, doc.getString("PrivacySetting"));
                        toDoEvents.add(newHabit); // Adding habits from Firestore
                        toDoEventIds.add(doc.getId());
                    }
                }

                toDoEventAdapter.notifyDataSetChanged();
            }
        });
        
    }

    public void completedEventsScreenSetup() {
        //completedEvents.clear();
        // get completed habits for today (need to use habit instances collection to do this)

        // Get a list of habit events
        // of the user logged in, on this day

        db.collection("Instances").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                completedEvents.clear();
                completedEventIds.clear();
                for(QueryDocumentSnapshot doc: value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Opt_comment")));

                    LocalDate eventDate = LocalDate.parse(doc.getString("Date"), formatter);

                    if (doc.getString("UID").equals(loggedInId) && (eventDate.isEqual(currentDate)) ){
                        HabitInstance newInstance = new HabitInstance(doc.getString("UID"), doc.getString("HID"),
                                doc.getString("Opt_comment"), doc.getString("Date"), Integer.parseInt(doc.getString("Duration")));
                        completedEventAdapter.add(newInstance);
                        completedEventIds.add(doc.getId()); // Adding habit events from Firestore
                    }
                }

                completedEventAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSavePressed(HabitInstance habitInstance) {

        addHabitEventDatabase(habitInstance);
        LocalDate todayDate = LocalDate.now();

        completedEventsScreenSetup();

    }

    @Override
    public void onEditSavePressed(HabitInstance instance) {
        HashMap<String, String> data = new HashMap<>();
        data.put("UID", instance.getUID());
        data.put("HID", instance.getHID());
        data.put("Date", instance.getDate());
        data.put("Opt_comment",instance.getOpt_comment());
        data.put("Duration",String.valueOf(instance.getDuration()));

        db.collection("Instances")
                .document(selectedHabitInstanceId)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        LocalDate todayDate = LocalDate.now();
        completedEventsScreenSetup();
    }

    @Override
    public void onDeletePressed(HabitInstance instance) {

        db.collection("Instances")
                .document(selectedHabitInstanceId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        LocalDate todayDate = LocalDate.now();
        completedEventsScreenSetup();
    }

    public void addHabitEventDatabase(HabitInstance instance){
        final CollectionReference collectionReference  =  db.collection("Instances");
        DocumentReference newInstanceRef = db.collection("Instances").document();

        String instanceId = newInstanceRef.getId();
        HashMap<String, Object> data = new HashMap<>();

        if (instanceId != null){
            data.put("UID", instance.getUID());
            data.put("HID", instance.getHID());
            data.put("Date", instance.getDate());
            data.put("Opt_comment",instance.getOpt_comment());
            data.put("Duration",instance.getDuration());
            collectionReference
                    .document(instanceId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log. d (TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log. d (TAG, "Data could not be added!" + e.toString());
                        }
                    });
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        //make sure date is empty before setting it to the date picked
        String date = "";

        if(month+1 < 10) date+= "0";
        date += String.valueOf(month + 1) + "/";

        if (day < 10 ) date += "0";
        date += String.valueOf(day + "/");

        date += String.valueOf(year);

        currentDate = LocalDate.parse(date, formatter);
        setDate();
        screenSetup();
        completedEventsScreenSetup();
    }
}