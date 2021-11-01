package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;

public class UserCalendar extends AppCompatActivity implements addHabitEventFragment.OnFragmentInteractionListener,editHabitEventFragment.OnFragmentInteractionListener {
    private ListView toDoEventsList;
    private ListView completedEventsList;
    FirebaseFirestore db;
    private static final String TAG = "Instance";

    private final ArrayList<String> toDoEvents = new ArrayList<>();
    private final ArrayList<String> completedEvents = new ArrayList<>();

    private ArrayAdapter<String> toDoEventAdapter;
    private ArrayAdapter<String> completedEventAdapter;

    private final ArrayList<HabitInstance> habitEvents_list = new ArrayList<>();

    String selectedCompletedEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        TextView title1 = findViewById(R.id.title1);

        // Getting present date and day of the week
        LocalDate todayDate = LocalDate.now();
        String todayDay = todayDate.getDayOfWeek().toString();

        // Setting the current date to the first text view
        String formattedDate = todayDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        title1.setText(formattedDate + ")");

        // Create a list of habit events and initialize an adapter
        toDoEventsList = findViewById(R.id.toDoEventsList);
        completedEventsList = findViewById(R.id.completedEventsList);

        toDoEventAdapter = new ArrayAdapter<>(
                this, R.layout.calendar_content, toDoEvents);
        completedEventAdapter = new ArrayAdapter<>(
                this, R.layout.calendar_content, completedEvents);

        toDoEventsList.setAdapter(toDoEventAdapter);
        completedEventsList.setAdapter(completedEventAdapter);

        // Creating habits
        ArrayList<Habit> habits = createHabits();

        initialScreen(habits, todayDate, todayDay);

        toDoEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addHabitEventFragment dialog = new addHabitEventFragment();
                //dialog.setValue("Running");
                dialog.show(getSupportFragmentManager(), "EDIT");
            }
        });

        completedEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCompletedEvent = completedEvents.get(i);
                int pos = Integer.parseInt(selectedCompletedEvent.split("[)]")[0]);

                HabitInstance instanceToEdit = null;
                for (int k = 0; k < habitEvents_list.size(); k++) {
                    instanceToEdit = habitEvents_list.get(k);
                    if (instanceToEdit.getUniqueId() == pos) {
                        break;
                    }
                }

                editHabitEventFragment values =
                        new editHabitEventFragment().newInstance(i, instanceToEdit);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });
    }

    public ArrayList<Habit> createHabits() {
        ArrayList<String> days = new ArrayList<>();
        days.add("MONDAY");
        days.add("WEDNESDAY");
        days.add("SUNDAY");

        Habit habit1 = new Habit("Running", "To run a 5k", "2021-10-27", days, "Private");
        Habit habit2 = new Habit("Walking", "To stay healthy", "2021-10-05", days, "Private");

        ArrayList<Habit> habits = new ArrayList<>();
        habits.add(habit1);
        habits.add(habit2);

        return habits;
    }

    public void initialScreen(ArrayList<Habit> habits, LocalDate todayDate, String todayDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < habits.size(); i++) {
            Habit currentHabit = habits.get(i);

            // Checking if there are any habits assigned for today
            LocalDate startDate = LocalDate.parse(currentHabit.getDateToStart(), formatter);

            if (startDate.isBefore(todayDate) && currentHabit.getWeekdays().contains(todayDay)) {
                toDoEvents.add((i+1) + ") " + currentHabit.getTitle());
            }
        }

        toDoEventAdapter.notifyDataSetChanged();
    }

    public void displayCompletedEvents() {
        completedEvents.clear();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < habitEvents_list.size(); i++) {
            HabitInstance currentHabit = habitEvents_list.get(i);
            currentHabit.setUniqueId(i+1);

            //LocalDate doneDate = LocalDate.parse(currentHabit.getDate(), formatter);
            //String formattedDate = doneDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
            String toDisplay = currentHabit.getUniqueId() + ") " + currentHabit.getOpt_comment();
            completedEvents.add(toDisplay);
        }

        completedEventAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSavePressed(HabitInstance habitInstance) {
        habitEvents_list.add(habitInstance);
        addHabitEventDatabase(habitInstance);
        displayCompletedEvents();
    }

    @Override
    public void onEditSavePressed(HabitInstance instance, int position) {

        habitEvents_list.set(position, instance);

        displayCompletedEvents();
    }

    @Override
    public void onDeletePressed(HabitInstance instance) {

        habitEvents_list.remove(instance);

        displayCompletedEvents();
    }

    public void addHabitEventDatabase(HabitInstance instance){
        db  =  FirebaseFirestore.getInstance();
        final CollectionReference collectionReference  =  db.collection("Instances");
        DocumentReference newInstanceRef = db.collection("Instances").document();
        //String HabitId = instance.getHabitId;
        String instanceId = newInstanceRef.getId();
        HashMap<String, String> data = new HashMap<>();

        if (instanceId != null){
            data.put("Date", instance.getDate());
            data.put("Opt_comment",instance.getOpt_comment());
            data.put("Duration",String.valueOf(instance.getDuration()));
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

}