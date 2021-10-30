package com.example.agilesprinters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements addHabitFragment.OnFragmentInteractionListener,
        viewEditHabitFragment.OnFragmentInteractionListener, deleteHabitFragment.OnFragmentInteractionListener{
    ArrayList<Habit> habitArrayList;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        habitList = findViewById(R.id.habit_list);
        habitArrayList = new ArrayList<Habit>();

        habitAdapter = new habitListAdapter(this, R.layout.home_list_content, habitArrayList);
        habitList.setAdapter(habitAdapter);

        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Habit habit = (Habit) adapterView.getItemAtPosition(i);
                viewEditHabitFragment values = new viewEditHabitFragment().newInstance(i, habit);
                values.show(getSupportFragmentManager(), "VIEW/EDIT");
            }
        });

        final FloatingActionButton addMedicineButton = findViewById(R.id.add_habit_button);
        addMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new addHabitFragment().show(getSupportFragmentManager(), "ADD");
            }
        });

        habitList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteHabitFragment delete = new deleteHabitFragment().newInstance(i);
                delete.show(getSupportFragmentManager(), "DELETE");

                // return true so that it overrides a regular item click and the view/edit fragment does not pop up
                return true;
            }
        });

    }

    /**
     * This function adds a habit to the list once the user clicks add on the addHabitFragment
     * dialog fragment
     * @param habit The habit object created by the addHabitFragment
     */
    @Override
    public void onAddPressed(Habit habit) {
        habitAdapter.add(habit);

        habitAdapter.notifyDataSetChanged();
    }

    /**
     * This function edits a habit in the list by taking the habit created when the "Save Changes"
     * button is pressed and replacing the old habit object in the list with the one supplied
     * to the function.
     * @param habit The habit object created by the editViewHabitFragment
     * @param position The position of the original habit object in the list
     */
    @Override
    public void onEditViewSaveChangesPressed(Habit habit, int position) {
        Habit original = habitAdapter.getItem(position);
        original.setTitle(habit.getTitle());
        original.setReason(habit.getReason());
        original.setDateToStart(habit.getDateToStart());
        original.setWeekdays(habit.getWeekdays());
        original.setPrivacySetting(habit.getPrivacySetting());
        habitAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEditViewCancelPressed(Habit habit, int position) {
        Habit original = habitAdapter.getItem(position);
        original.setWeekdays(habit.getWeekdays());
        habitAdapter.notifyDataSetChanged();
    }

    /**
     * This function deletes the object selected by the user in the list after a user clicks "Yes"
     * in the deleteHabitFragment dialog fragment.
     * @param position The position of the object clicked in the list.
     */
    @Override
    public void onDeleteHabitYesPressed(int position) {
        Habit habit = habitAdapter.getItem(position);
        habitAdapter.remove(habit);
        habitAdapter.notifyDataSetChanged();
    }
}