package com.example.agilesprinters;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class homeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Home> rule =
            new ActivityTestRule<>(Home.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * This test will check to make sure that when a habit is added it shows up in the list
     */
    @Test
    public void checkHabitList(){
        //checks to make sure we are in the right activity
        solo.assertCurrentActivity("Wrong Activity", Home.class);
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        //wait for add habit fragment to open
        solo.waitForDialogToOpen(1000);
        //enter data for habit
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), "Running");
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), "Run 3 hrs each week");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));
        solo.pressSpinnerItem(0, 0);

        //make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, "Public"));
        solo.clickOnView(solo.getView(R.id.Date));

        //wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);
        solo.setDatePicker(0, 2021, 11,10);
        solo.clickOnButton("Ok");
        solo.waitForDialogToClose(1000);

        //make sure that if a field is left empty that you will be presented with an error message
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("Please choose which days you would like this event to occur.", 1, 1000));
        solo.clickOnView(solo.getView(R.id.button_monday));
        solo.clickOnView(solo.getView(R.id.button_wednesday));
        solo.clickOnView(solo.getView(R.id.button_friday));

        //add habit
        solo.clickOnButton("Add");
        solo.waitForDialogToClose(1000);

        //make sure habit shows up in list
        assertTrue(solo.waitForText("Running", 1, 1000));
    }

    /**
     * This test will make sure that a habit can be edited and the edited version will show in the list
     */
    @Test
    public void checkEditViewList(){
        solo.assertCurrentActivity("Wrong Activity", Home.class);
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        solo.waitForDialogToOpen(1000);
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), "Running");
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), "Run 3 hrs each week");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));
        solo.pressSpinnerItem(0, 0);

        assertTrue(solo.isSpinnerTextSelected(0, "Public"));
        solo.clickOnView(solo.getView(R.id.Date));

        solo.waitForDialogToOpen(1000);
        solo.setDatePicker(0, 2021, 11,10);
        solo.clickOnButton("Ok");
        solo.waitForDialogToClose(1000);

        solo.clickOnView(solo.getView(R.id.button_monday));
        solo.clickOnView(solo.getView(R.id.button_wednesday));
        solo.clickOnView(solo.getView(R.id.button_friday));

        solo.clickOnButton("Add");
        solo.waitForDialogToClose(1000);

        assertTrue(solo.waitForText("Running", 1, 1000));

        //click on the running habit
        solo.clickInList(1);

        //wait for viewEditHabitFragment to open and change the reason
        solo.waitForDialogToOpen(1000);
        solo.enterText((EditText) solo.getView(R.id.view_edit_habit_reason_editText), "Run 1.5 hours per week");
        solo.clickOnButton("Save Changes");
        solo.waitForDialogToClose(1000);

        //make sure reason was actually changed and shows in the list
        assertTrue(solo.waitForText("Run 1.5 hours per week"));
    }

    /**
     * This test makes sure that a habit is deleted
     */
    @Test
    public void deleteHabit(){
        solo.assertCurrentActivity("Wrong Activity", Home.class);
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        solo.waitForDialogToOpen(1000);
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), "Running");
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), "Run 3 hrs each week");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));
        solo.pressSpinnerItem(0, 0);

        assertTrue(solo.isSpinnerTextSelected(0, "Public"));
        solo.clickOnView(solo.getView(R.id.Date));

        solo.waitForDialogToOpen(1000);
        solo.setDatePicker(0, 2021, 11,10);
        solo.clickOnButton("Ok");
        solo.waitForDialogToClose(1000);

        solo.clickOnView(solo.getView(R.id.button_monday));
        solo.clickOnView(solo.getView(R.id.button_wednesday));
        solo.clickOnView(solo.getView(R.id.button_friday));

        solo.clickOnButton("Add");
        solo.waitForDialogToClose(1000);

        assertTrue(solo.waitForText("Running", 1, 1000));

        //long click on habit
        solo.clickLongInList(1);

        //wait for deleteHabitFragment to open
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");

        // make sure that the deleted habit is no longer on screen
        assertTrue(solo.waitForText("Running", 0, 1000));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
