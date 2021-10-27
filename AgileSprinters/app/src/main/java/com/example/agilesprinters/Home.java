package com.example.agilesprinters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener, viewEditHabitFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onAddPressed(Habit habit) {

    }

    @Override
    public void onEditViewOkPressed(Habit habit, int position) {

    }
}