package com.example.agilesprinters;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.Map;

public class UserCalendar extends AppCompatActivity
        implements addHabitEventFragment.OnFragmentInteractionListener,
        editHabitEventFragment.OnFragmentInteractionListener,
        DatePickerDialog.OnDateSetListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "Instance";

    private ArrayAdapter<Habit> toDoEventAdapter;
    private final ArrayList<Habit> toDoEvents = new ArrayList<>();

    private ArrayAdapter<HabitInstance> completedEventAdapter;
    private final ArrayList<HabitInstance> completedEvents = new ArrayList<>();
    private final ArrayList<String> completedEventIds = new ArrayList<>();
    private final ArrayList<String> toDoEventIds = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private TextView title1;
    FirebaseFirestore db;
    private String UID;
    private User user;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private HabitInstance selectedHabitInstance;
    private String selectedHabitInstanceId;
    LocalDate currentDate = LocalDate.now();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);


        if (UID == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ListView toDoEventsList = findViewById(R.id.toDoEventsList);
        ListView completedEventsList = findViewById(R.id.completedEventsList);

        title1 = findViewById(R.id.title1);
        Button calendar_button = findViewById(R.id.calendar_button);

        db = FirebaseFirestore.getInstance();

        screenSetup();
        completedEventsScreenSetup();

        toDoEventsList.setOnItemClickListener((adapterView, view, i, l) -> {

            if (currentDate.isEqual(LocalDate.now())) {
                selectedHabitInstanceId = toDoEventIds.get(i);

                DocumentReference newInstanceRef = db.collection("HabitEvents").document();
                String instanceId = newInstanceRef.getId();

                // get hid here
                addHabitEventFragment values =
                        new addHabitEventFragment().newInstance(i, UID, selectedHabitInstanceId, instanceId);
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
        title1.setText("Tasks for " + formattedDate + ")");
    }

    public ArrayList<String> getHabitDays(Map<String, Object> weekdays) {
        String[] days = new String[]{ getString(R.string.mondayStr), getString(R.string.tuesdayStr),
                getString(R.string.wednesdayStr), getString(R.string.thursdayStr), getString(R.string.fridayStr),
                getString(R.string.saturdayStr), getString(R.string.sundayStr)};
        ArrayList<String> habitDays = new ArrayList<>();

        for (String day : days) {
            if (weekdays.get(day).equals(true)) {
                habitDays.add(day);
                System.out.println("The habits are " + day);
            }
        }

        return habitDays;
    }

    public void screenSetup() {
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
                    Log.d(TAG, "Habits to do today " + String.valueOf(doc.getData().get("Title")));

                    // Gives the start date
                    LocalDate startDate = LocalDate.parse(doc.getString("Data to Start"), formatter);
                    Map<String, Object> weekdays = (Map<String, Object>) doc.getData().get("Weekdays");
                    HashMap<String,Boolean> weekdays2 = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                    ArrayList<String> habitDays = getHabitDays(weekdays);

                    if (doc.getString("UID").equals(UID)
                            && (startDate.isBefore(currentDate) || startDate.isEqual(currentDate))
                            && (habitDays.contains(todayDay))){
                        Habit newHabit = new Habit(doc.getId(),doc.getString("UID"),doc.getString("Title"), doc.getString("Reason"),
                                doc.getString("Data to Start"), weekdays2, doc.getString("PrivacySetting"));
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

        db.collection("HabitEvents").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                completedEvents.clear();
                completedEventIds.clear();
                for(QueryDocumentSnapshot doc: value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Opt_comment")));

                    LocalDate eventDate = LocalDate.parse(doc.get("Date").toString(), formatter);

                    if (doc.getString("UID").equals(UID) && (eventDate.isEqual(currentDate)) ){
                        HabitInstance newInstance = new HabitInstance(doc.getString("EID"), doc.getString("UID"), doc.getString("HID"),
                                doc.getString("Opt_comment"), doc.getString("Date"), Integer.parseInt(doc.get("Duration").toString()));
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

        completedEventsScreenSetup();

    }

    @Override
    public void onEditSavePressed(HabitInstance instance) {
        HashMap<String, String> data = new HashMap<>();
        data.put("EID", instance.getEID());
        data.put("UID", instance.getUID());
        data.put("HID", instance.getHID());
        data.put("Date", instance.getDate());
        data.put("Opt_comment",instance.getOpt_comment());
        data.put("Duration",String.valueOf(instance.getDuration()));

        db.collection("HabitEvents")
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

        completedEventsScreenSetup();
    }

    @Override
    public void onDeletePressed(HabitInstance instance) {

        db.collection("HabitEvents")
                .document(instance.getEID())
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

        completedEventsScreenSetup();
    }

    public void addHabitEventDatabase(HabitInstance instance){
        final CollectionReference collectionReference  =  db.collection("HabitEvents");

        String instanceId = instance.getEID();
        HashMap<String, Object> data = new HashMap<>();

        if (instanceId != null){
            data.put("EID", instance.getEID());
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(this, Home.class);
                intent.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intent);
                break;

            case R.id.calendar:
                if(this instanceof UserCalendar){
                    return true;
                } else {
                    Intent intent2 = new Intent(this, UserCalendar.class);
                    //add bundle to send data if need
                    startActivity(intent2);
                    break;
                }

            case R.id.forumn:
                break;

        }
        return false;
    }


}