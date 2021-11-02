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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;

public class UserCalendar extends AppCompatActivity implements addHabitEventFragment.OnFragmentInteractionListener,editHabitEventFragment.OnFragmentInteractionListener {

    private static final String TAG = "Instance";

    private ListView toDoEventsList;
    private ArrayAdapter<Habit> toDoEventAdapter;
    private final ArrayList<Habit> toDoEvents = new ArrayList<>();

    private ListView completedEventsList;
    private ArrayAdapter<HabitInstance> completedEventAdapter;
    private ArrayList<HabitInstance> completedEvents;

    private TextView title1;
    FirebaseFirestore db;
    private String loggedInId = "nXmcIP2McwOw89GpWW10xt02JzG2";

    /**
    private ListView toDoEventsList;
    private ListView completedEventsList;

    private final ArrayList<String> toDoEvents = new ArrayList<>();
    private final ArrayList<String> completedEvents = new ArrayList<>();

    private ArrayAdapter<String> toDoEventAdapter;
    private ArrayAdapter<String> completedEventAdapter; **/

    private final ArrayList<HabitInstance> habitEvents_list = new ArrayList<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private Habit selectedHabit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);

        toDoEventsList = findViewById(R.id.toDoEventsList);
        completedEventsList = findViewById(R.id.completedEventsList);

        title1 = findViewById(R.id.title1);

        db = FirebaseFirestore.getInstance();
        //final CollectionReference collectionReference = db.collection("Instances");

        //toDoEvents = new ArrayList<>();

        // Getting present date and day of the week
        LocalDate todayDate = LocalDate.now();
        screenSetup(todayDate);

        toDoEventAdapter = new habitInstanceListAdapter(this, toDoEvents);

        toDoEventsList.setAdapter(toDoEventAdapter);
        toDoEventAdapter.notifyDataSetChanged();

        toDoEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHabit = toDoEvents.get(i);

                addHabitEventFragment dialog = new addHabitEventFragment();
                dialog.show(getSupportFragmentManager(), "ADD");
            }
        });


        /**
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
        });**/
    }

    public void setDate(LocalDate current_date) {
        String formattedDate = current_date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        title1.setText(formattedDate + ")");
    }

    public void screenSetup(LocalDate dateToDisplay) {
        HashMap<String, Boolean > days = new HashMap<>();
        days.put("MONDAY", true);
        days.put("WEDNESDAY", false);
        days.put("SUNDAY", true);
        setDate(dateToDisplay);

        // get habits to do today (need to use habits collection to do this)

        // Gives the day of the week
        String todayDay = dateToDisplay.getDayOfWeek().toString();

        // Get a list of habits
        // of the user logged in, start date before today
        // add days properly

        db.collection("Habit").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Title")));

                    LocalDate startDate = LocalDate.parse(doc.getData().get("Data to Start").toString(), formatter);

                    if (doc.getString("UID").equals(loggedInId)
                            && (startDate.isBefore(dateToDisplay) || startDate.isEqual(dateToDisplay)) ){
                        Habit newHabit = new Habit(doc.getString("Title"), doc.getString("Reason"),
                                doc.getString("Data to Start"), days, doc.getString("PrivacySetting"));
                        toDoEvents.add(newHabit); // Adding habits from Firestore
                    }
                }

                toDoEventAdapter.notifyDataSetChanged();
            }
        });
        
    }

    /**
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
    }**/

    @Override
    public void onSavePressed(HabitInstance habitInstance) {
        habitEvents_list.add(habitInstance);

        //displayCompletedEvents();
    }

    @Override
    public void onEditSavePressed(HabitInstance instance, int position) {

        habitEvents_list.set(position, instance);

        //displayCompletedEvents();
    }

    @Override
    public void onDeletePressed(HabitInstance instance) {

        habitEvents_list.remove(instance);

        //displayCompletedEvents();
    }

}