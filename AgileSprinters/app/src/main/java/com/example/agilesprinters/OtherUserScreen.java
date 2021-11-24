package com.example.agilesprinters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class OtherUserScreen extends AppCompatActivity {
    private String UID;
    private User user;
    private String nameStr;
    private ArrayList<Habit> habitArrayList;
    private ArrayAdapter<Habit> habitAdapter;
    private static final String TAG = "Habit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_other_user_screen);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference habitCollectionReference = db.collection("Habit");

        ListView habitList = findViewById(R.id.habit_list);

        habitArrayList = new ArrayList<>();
        habitAdapter = new habitListAdapter(this, habitArrayList);
        habitList.setAdapter(habitAdapter);

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
            nameStr = user.getFirstName()+ " " + user.getLastName();
        }

        setTextFields(nameStr);

        habitCollectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            // Clear the old list
            habitArrayList.clear();
            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                if (UID.matches((String) doc.getData().get("UID"))) {
                    String title = (String) doc.getData().get("Title");
                    String reason = (String) doc.getData().get("Reason");
                    String dateToStart = (String) doc.getData().get("Date to Start");
                    HashMap<String, Boolean> weekdays = (HashMap<String, Boolean>) doc.getData().get("Weekdays");
                    String privacySetting = (String) doc.getData().get("PrivacySetting");
                    int progress = Integer.parseInt(doc.get("Progress").toString());


                    habitArrayList.add(new Habit(doc.getId(), UID, title, reason, dateToStart, weekdays, privacySetting, progress));
                }
            }
            habitAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
            // from the cloud
        });

    }

    private void setTextFields( String firstNameStr) {
        Button userButton = findViewById(R.id.otherUserButton);

        userButton.setText(firstNameStr.substring(0,1));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        overridePendingTransition(0,0);
    }
}