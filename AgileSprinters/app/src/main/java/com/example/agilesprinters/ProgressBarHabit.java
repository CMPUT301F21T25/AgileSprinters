package com.example.agilesprinters;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressBarHabit {
    FirebaseFirestore db;
    private int totalEventsPlanned;
    private int eventsCompleted;

    public ProgressBarHabit() {
    }

    public int getTotalEventsPlanned() {
        return totalEventsPlanned;
    }

    public void setTotalEventsPlanned(HashMap<String, Boolean> weekdays) {
        int totalDays = 0;
        for (Boolean value : weekdays.values()) {
            if (value) {
                totalDays = totalDays + 1;
            }
        }
        this.totalEventsPlanned = totalDays * 12;
    }

    public int getEventsCompleted() {
        return eventsCompleted;
    }

    public void setEventsCompleted(Integer val) {
        this.eventsCompleted = val;
        /**db = FirebaseFirestore.getInstance();
        db.collection("HabitEvents").addSnapshotListener((value, error) -> {
            for(QueryDocumentSnapshot doc: value) {
                if (doc.getString("UID").equals(UID) && doc.getString("HID").equals(HID) ){
                    System.out.println("Printing ");
                    this.eventsCompleted += 1;
                }
            }
        });**/
    }

    public List<Integer> getProgressTuple() {
        List<Integer> progressTuple = new ArrayList<>();
        progressTuple.add(getEventsCompleted());
        progressTuple.add(getTotalEventsPlanned());
        return progressTuple;
    }
}