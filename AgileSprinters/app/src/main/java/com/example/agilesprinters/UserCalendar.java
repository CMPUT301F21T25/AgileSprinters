package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

/**
 * The user calendar class is an activity which displays the habits planned by the user for that day.
 * From here a user may click on the habit to add a completed habit event, tap on the event to edit,
 * view or delete it. There is also a 'past view events' button, through which the user can see
 * what habits were actually planned for a specific day in the past and what events were completed
 * regarding that. There is a navigation bar on the bottom that the user may click
 * to go to either home, forum, or notifications.
 *
 * @author Sai Rasazna Ajerla and Riyaben Patel
 */
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
    private String collectionPath;
    private Database database = new Database();


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private HabitInstance selectedHabitInstance;
    private String selectedHabitInstanceId;
    LocalDate currentDate = LocalDate.now();

    /**
     * This function creates the UI on the screen and listens for user input
     * @param savedInstanceState the instance state
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_calendar);


        if (UID == null){
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

        // When a item in the to do events is clicked, input is taken,
        // a habit event object is created and added to the database
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

        // When a item in the completed events is clicked updated input is taken,
        // or an event object is deleted
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

        // When the past events button is clicked, it asks for a date input and shows
        // habits planned for that day and events actually completed
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

    /**
     * This function sets the date on the UI according to the date selected by the user
     */
    public void setDate() {
        String formattedDate = currentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
        title1.setText("Tasks for " + formattedDate + ")");
    }

    /**
     * This function checks which day of the week is true
     * to see the habit days planned by the user
     * @param weekdays
     * This is a candidate map to check for the positive days
     * @return
     * Return a ArrayList of strings
     */
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

    /**
     * This function sets the to-do tasks part of the screen on the UI
     * according to the habits retrieved from the database
     */
    public void screenSetup() {
        setDate();

        // Gives the day of the week
        String todayDay = currentDate.getDayOfWeek().toString();

        // Display the habits which are scheduled for the current day (checking date and day)
        db.collection("Habit").addSnapshotListener((value, error) -> {
            toDoEvents.clear();
            toDoEventIds.clear();
            for(QueryDocumentSnapshot doc: value) {
                Log.d(TAG, "Habits to do today " + String.valueOf(doc.getData().get("Title")));
                System.out.println(doc.getString("Date to Start"));
                // Gives the start date
                LocalDate startDate = LocalDate.parse(doc.getString("Date to Start"), formatter);
                Map<String, Object> weekdays = (Map<String, Object>) doc.getData().get("Weekdays");
                HashMap<String,Boolean> weekdays2 = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                ArrayList<String> habitDays = getHabitDays(weekdays);

                if (doc.getString("UID").equals(UID)
                        && (startDate.isBefore(currentDate) || startDate.isEqual(currentDate))
                        && (habitDays.contains(todayDay))){
                    Habit newHabit = new Habit(doc.getId(),doc.getString("UID"),doc.getString("Title"), doc.getString("Reason"),
                            doc.getString("Date to Start"), weekdays2, doc.getString("PrivacySetting"));
                    toDoEvents.add(newHabit); // Adding habits from Firestore
                    toDoEventIds.add(doc.getId());
                }
            }

            toDoEventAdapter.notifyDataSetChanged();
        });
        
    }

    /**
     * This function sets the completed tasks part of the screen on the UI
     * according to the habit events retrieved from the database
     */
    public void completedEventsScreenSetup() {
        // Get a list of habit events of the user logged in on the current day
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

    /**
     * This function passes a habit instance to be added to the database once
     * the user clicks save on the addHabitEventFragment dialog fragment
     *
     * @param habitInstance The instance object created by the addHabitEventFragment
     */
    @Override
    public void onSavePressed(HabitInstance habitInstance) {

        addHabitEventDatabase(habitInstance);

        completedEventsScreenSetup();

    }

    /**
     * This function passes a habit instance to be updated once a user clicks
     * Save in the editHabitEventFragment dialog fragment.
     * @param instance The habit instance object changed in the editHabitEventFragment
     */
    @Override
    public void onEditSavePressed(HabitInstance instance) {
            HashMap<String, String> data = new HashMap<>();
            data.put("EID", instance.getEID());
            data.put("UID", instance.getUID());
            data.put("HID", instance.getHID());
            data.put("Date", instance.getDate());
            data.put("Opt_comment",instance.getOpt_comment());
            data.put("Duration",String.valueOf(instance.getDuration()));

            // Makes a call to the database which handles it
            collectionPath = "HabitEvents";
            database.updateData(collectionPath, selectedHabitInstanceId, data, TAG);

            completedEventsScreenSetup();
        }


    /**
     * This function deletes a habit instance object from the database once a user clicks
     * Delete in the editHabitEventFragment dialog fragment.
     * @param instance The habit instance object deleted in the editHabitEventFragment
     */
    @Override
    public void onDeletePressed(HabitInstance instance) {
            collectionPath = "HabitEvents";
            // Makes a call to the database which handles it
            database.deleteData(collectionPath, instance.getEID(), TAG);

            completedEventsScreenSetup();
        }

    /**
     * This function adds a habit event/instance object to the database.
     * @param instance The habit instance that needs to be added to the database.
     */
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

                // Makes a call to the database which handles it
                collectionPath = "HabitEvents";
                database.addData(collectionPath, instanceId, data, TAG);
            }
        }


    /**
     * This function captures the date chosen by the user once they press ok on the datePicker
     * fragment.
     * @param datePicker the datePicker dialog view
     * @param year year of the date chosen by the user
     * @param month month of the date chosen by the user
     * @param day day of the month of the date chosen by the user
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        //make sure date is empty before setting it to the date picked
        String date = "";

        if(month+1 < 10) date+= "0";
        date += (month + 1) + "/";

        if (day < 10 ) date += "0";
        date += day + "/";

        date += String.valueOf(year);

        currentDate = LocalDate.parse(date, formatter);
        setDate();
        screenSetup();
        completedEventsScreenSetup();
    }

    /**
     * This method contains the logic for switching screens by selecting an item from the navigation
     * bar.
     * @param item This is the item selected by the user
     * @return
     * Returns a boolean based on which activity the user is currently in and which item was
     * clicked.
     */
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