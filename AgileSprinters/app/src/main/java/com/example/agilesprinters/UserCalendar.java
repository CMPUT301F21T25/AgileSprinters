package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
        DatePickerDialog.OnDateSetListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        deleteHabitEventFragment.OnFragmentInteractionListener,
        shareHabitEventFragment.OnFragmentInteractionListener{

    private static final String TAG = "Instance";
    private ArrayAdapter<Habit> toDoEventAdapter;
    private final ArrayList<Habit> toDoEvents = new ArrayList<>();
    private ArrayAdapter<HabitInstance> completedEventAdapter;
    private final ArrayList<HabitInstance> completedEvents = new ArrayList<>();
    private final ArrayList<String> completedEventIds = new ArrayList<>();
    private final ArrayList<String> toDoEventIds = new ArrayList<>();
    private TextView title1;
    private FirebaseFirestore db;
    private String UID;
    private User user;
    private String collectionPath;
    private Database database = new Database();
    private String path = null;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.user_calendar);


        if (UID == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.calendar);

        ListView toDoEventsList = findViewById(R.id.toDoEventsList);
        ListView completedEventsList = findViewById(R.id.completedEventsList);

        title1 = findViewById(R.id.title1);

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
                DocumentReference newImageRef = db.collection("EventImageCollection").document();
                String IID = newImageRef.getId();

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
                System.out.println("image id is "+selectedHabitInstance.getIID());

                editHabitEventFragment values =
                        new editHabitEventFragment().newInstance(i, selectedHabitInstance);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        // When the past events button is clicked, it asks for a date input and shows
        // habits planned for that day and events actually completed
        title1.setOnClickListener(new View.OnClickListener() {
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
        title1.setText("Tasks for " + formattedDate);
    }

    /**
     * This function checks which day of the week is true
     * to see the habit days planned by the user
     *
     * @param weekdays This is a candidate map to check for the positive days
     * @return Return a ArrayList of strings
     */
    public ArrayList<String> getHabitDays(Map<String, Object> weekdays) {
        String[] days = new String[]{getString(R.string.SUNDAY_STR), getString(R.string.MONDAY_STR), getString(R.string.TUESDAY_STR),
                getString(R.string.WEDNESDAY_STR), getString(R.string.THURSDAY_STR), getString(R.string.FRIDAY_STR),
                getString(R.string.SATURDAY_STR)};
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
            for (QueryDocumentSnapshot doc : value) {
                Log.d(TAG, "Habits to do today " + String.valueOf(doc.getData().get("Title")));
                System.out.println(doc.getString("Date to Start"));
                // Gives the start date
                if (!String.valueOf(doc.getData().get("Title")).equals("dummy")) {
                    LocalDate startDate = LocalDate.parse(doc.getString("Date to Start"), formatter);

                    Map<String, Object> weekdays = (Map<String, Object>) doc.getData().get("Weekdays");
                    HashMap<String, Boolean> weekdays2 = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                    ArrayList<String> habitDays = getHabitDays(weekdays);

                    if (doc.getString("UID").equals(UID)
                            && (startDate.isBefore(currentDate) || startDate.isEqual(currentDate))
                            && (habitDays.contains(todayDay))) {
                        Habit newHabit = new Habit(doc.getId(), doc.getString("UID"), doc.getString("Title"), doc.getString("Reason"),
                                doc.getString("Date to Start"), weekdays2, doc.getString("PrivacySetting"),
                                Integer.parseInt(doc.get("Progress").toString()), Integer.parseInt(doc.get("List Position").toString()));
                        toDoEvents.add(newHabit); // Adding habits from Firestore
                        toDoEventIds.add(doc.getId());
                    }
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
                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Opt_comment")));
                    if (doc.getString("UID").equals(UID)) {
                        LocalDate eventDate = LocalDate.parse(doc.get("Date").toString(), formatter);
                        if ((eventDate.isEqual(currentDate))) {
                            HabitInstance newInstance = new HabitInstance(doc.getString("EID"), doc.getString("UID"), doc.getString("HID"),
                                    doc.getString("Opt_comment"), doc.getString("Date"), Integer.parseInt(doc.get("Duration").toString()), doc.getString("IID"), doc.getString("FID"),
                                    Boolean.parseBoolean((String) doc.getData().get("isShared")), doc.getString("Opt_Loc"));
                            completedEvents.add(newInstance);
                            completedEventIds.add(doc.getId()); // Adding habit events from Firestore
                        }

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
    public void onSavePressed(HabitInstance habitInstance, Bitmap bitmap) {
        DocumentReference newHabitRef = db.collection("Habit").document();
        String forumID = stringChange(newHabitRef.getId());

        addHabitEventDatabase(habitInstance, bitmap, forumID);
        updateForum(habitInstance, forumID, "ADD");
        completedEventsScreenSetup();

        updateProgressInDatabase(habitInstance, "ADD");

    }

    private void updateForum(HabitInstance habitInstance, String FID, String toDo) {
        String HID = habitInstance.getHID();
        String privacySetting = "";
        HashMap<String, String> data = new HashMap();

        for (int i = 0; i < toDoEvents.size(); i++){
            if (HID.matches(toDoEvents.get(i).getHID())){
                privacySetting = toDoEvents.get(i).getPrivacySetting();
            }
        }
        String duration = String.valueOf(habitInstance.getDuration());
        if ( privacySetting.matches("Public") || (
                (privacySetting.matches("Private")) && habitInstance.getShared()) ){
            data.put("Event Date", habitInstance.getDate());
            data.put("First Name", user.getFirstName());
            data.put("Last Name", user.getLastName());
            data.put("duration", duration);
            data.put("UID", habitInstance.getUID());
            data.put("Opt Cmt", habitInstance.getOpt_comment());
            data.put("EID", habitInstance.getEID());
            data.put("FID", FID);
            data.put("Opt_Loc", habitInstance.getDisplayLocStr(new Geocoder(this, Locale.getDefault())));
            data.put("IID", habitInstance.getIID());

            collectionPath = "ForumPosts";
            if (toDo.matches("EDIT")) {
                database.updateData(collectionPath, habitInstance.getFID(), data, TAG);
            } else if (toDo.matches("ADD")) {
                database.addData(collectionPath, FID, data, "Forum Post");
            }
        }
    }

    public String stringChange(String str) {
        for (int i = 0; i < 3; i++){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    /**
     * This function passes a habit instance to be updated once a user clicks
     * Save in the editHabitEventFragment dialog fragment.
     *
     * @param instance The habit instance object changed in the editHabitEventFragment
     */
    @Override
    public void onEditSavePressed(HabitInstance instance, Bitmap bitmap) {
        if (instance.getIID() == null){
            path = "images/"+System.currentTimeMillis() +".jpg";
        } else {
            path = instance.getIID();
        }

        if (bitmap != null) {
            database.addImage(path, bitmap);
            instance.setIID(path);
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("FID", instance.getFID());
        data.put("EID", instance.getEID());
        data.put("UID", instance.getUID());
        data.put("HID", instance.getHID());
        data.put("IID", instance.getIID());
        data.put("Date", instance.getDate());
        data.put("Opt_comment", instance.getOpt_comment());
        data.put("Duration", String.valueOf(instance.getDuration()));
        data.put("isShared", String.valueOf(instance.getShared()));
        data.put("Opt_Loc", instance.getOptLoc());

        // Makes a call to the database which handles it
        collectionPath = "HabitEvents";
        database.updateData(collectionPath, selectedHabitInstanceId, data, TAG);
        updateForum(instance, instance.getFID(),"EDIT");

        completedEventsScreenSetup();
    }

    /**
     * This function deletes a habit instance object from the database once a user clicks
     * Delete in the editHabitEventFragment dialog fragment.
     *
     * @param instance The habit instance object deleted in the editHabitEventFragment
     */
    @Override
    public void onDeletePressed(HabitInstance instance) {
        collectionPath = "HabitEvents";

        // Show up a fragment
        deleteHabitEventFragment delete = new deleteHabitEventFragment().newInstance(instance);
        delete.show(getSupportFragmentManager(), "DELETE");
    }

    @Override
    public void onSharePressed(HabitInstance instance) {
        collectionPath = "HabitEvents";

        // Show up a fragment
        shareHabitEventFragment share = new shareHabitEventFragment().newInstance(instance);
        share.show(getSupportFragmentManager(), "SHARE");

    }

    /**
     * This function deletes the habit event selected by the user in the list
     * in the deleteHabitEventFragment dialog fragment.
     *
     * @param instance The position of the object clicked in the list.
     */
    @Override
    public void onDeleteHabitEventYesPressed(HabitInstance instance) {
        // Makes a call to the database which handles it
        database.deleteData(collectionPath, instance.getEID(), TAG);
        getImageToDelete(instance);

        deleteForumElement(instance);
        database.deleteData(collectionPath, instance.getEID(), TAG);

        completedEventsScreenSetup();

        updateProgressInDatabase(instance, "DELETE");
    }

    private void getImageToDelete(HabitInstance instance) {
        db = FirebaseFirestore.getInstance();
        db.collection("HabitEvents").addSnapshotListener((value, error) -> {

            for (QueryDocumentSnapshot doc : value) {
                if (instance.getEID().matches(doc.getId())
                        && (instance.getHID().matches((String) doc.getData().get("HID")))
                        && (instance.getUID().matches((String) doc.getData().get("UID")))) {
                    System.out.println("hello form user");
                    database.deleteImg((String) doc.getData().get("IID"));

                }
            }
        });
    }

    private void deleteForumElement(HabitInstance instance) {
        String EID = instance.getEID();
        db.collection("ForumPosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (EID.matches((String) doc.getData().get("EID"))) {
                        System.out.println("ID is " + doc.getId());
                        if(doc.getId() == null){
                            return;
                        } else {
                            database.deleteData("ForumPosts", doc.getId(), TAG);
                        }
                        break;
                    }
                }
                // from the cloud
            }
        });
    }

    /**
     * This function adds a habit event/instance object to the database.
     * @param instance The habit instance that needs to be added to the database.
     */
    public void addHabitEventDatabase(HabitInstance instance, Bitmap bitmap, String FID) {
        final CollectionReference collectionReference = db.collection("HabitEvents");
        if (bitmap != null) {
            path = "images/"+System.currentTimeMillis() +".jpg";
            database.addImage(path, bitmap);
            instance.setIID(path);
        }

        String instanceId = instance.getEID();
        HashMap<String, Object> data = new HashMap<>();

        if (instanceId != null) {
            data.put("FID", FID);
            data.put("EID", instance.getEID());
            data.put("UID", instance.getUID());
            data.put("HID", instance.getHID());
            data.put("IID", instance.getIID());
            data.put("Date", instance.getDate());
            data.put("Opt_comment", instance.getOpt_comment());
            data.put("Duration", instance.getDuration());
            data.put("isShared", String.valueOf(instance.getShared()));
            data.put("Opt_Loc", instance.getOptLoc());

            // Makes a call to the database which handles it
            collectionPath = "HabitEvents";
            database.addData(collectionPath, instanceId, data, TAG);
        }
    }

    private Integer getNewProgress(HabitInstance instance, String toDo) {

        int completed = 0;

        for (Habit habit1 : toDoEvents) {
            if (habit1.getHID().equals(instance.getHID())) {
                if (toDo == "ADD") {
                    completed = habit1.getOverallProgress() + 1;
                    //updateHomePage(habit1);
                    break;
                } else if (toDo == "DELETE") {
                    completed = habit1.getOverallProgress() - 1;
                    break;
                }
            }
        }

        return completed;
    }

    public void updateProgressInDatabase(HabitInstance instance, String toDo){
        db.collection("Habit")
                .document(instance.getHID())
                .update("Progress", getNewProgress(instance, toDo));
    }

    /**
     * This function captures the date chosen by the user once they press ok on the datePicker
     * fragment.
     *
     * @param datePicker the datePicker dialog view
     * @param year       year of the date chosen by the user
     * @param month      month of the date chosen by the user
     * @param day        day of the month of the date chosen by the user
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        //make sure date is empty before setting it to the date picked
        String date = "";

        if (month + 1 < 10) date += "0";
        date += (month + 1) + "/";

        if (day < 10) date += "0";
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
     *
     * @param item This is the item selected by the user
     * @return Returns a boolean based on which activity the user is currently in and which item was
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
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.calendar:
                if (this instanceof UserCalendar) {
                    return true;
                } else {
                    Intent intent2 = new Intent(this, UserCalendar.class);
                    //add bundle to send data if need
                    startActivity(intent2);
                    finish();
                    overridePendingTransition(0,0);
                    break;
                }

            case R.id.notification:
                Intent intentNotification = new Intent(this, Notifications.class);
                intentNotification.putExtra("user", user);
                //add bundle to send data if need
                startActivity(intentNotification);
                finish();
                overridePendingTransition(0,0);
                break;

            case R.id.forum:
                Intent forumIntent = new Intent(this, ForumManager.class);
                forumIntent.putExtra("user", user);
                startActivity(forumIntent);
                finish();
                overridePendingTransition(0,0);
                break;

        }
        return false;
    }


    @Override
    public void onShareHabitEventYesPressed(HabitInstance eventToShare) {
        HashMap<String, String> data = new HashMap();
        String FID = eventToShare.getFID();

        String duration = String.valueOf(eventToShare.getDuration());

        data.put("Event Date", eventToShare.getDate());
        data.put("First Name", user.getFirstName());
        data.put("Last Name", user.getLastName());
        data.put("duration", duration);
        data.put("UID", eventToShare.getUID());
        data.put("Opt Cmt", eventToShare.getOpt_comment());
        data.put("EID", eventToShare.getEID());
        data.put("FID", FID);
        data.put("IID", eventToShare.getIID());
        data.put("Opt_Loc", eventToShare.getDisplayLocStr((new Geocoder(this, Locale.getDefault()))));

        collectionPath = "ForumPosts";
        database.addData(collectionPath, FID, data, "Forum Post");


        db.collection("HabitEvents")
                .document(eventToShare.getEID())
                .update("isShared", String.valueOf(true));
    }
}