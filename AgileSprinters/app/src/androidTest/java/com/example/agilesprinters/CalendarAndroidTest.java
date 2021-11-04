package com.example.agilesprinters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Using solo class from robotium
 * library: imitate same function
 * (open an app and perform tapping function)
 */

// Implementing our test cases
public class CalendarAndroidTest {
    // Instantiate obj from Solo class, to perform UI activities like tapping, writing
    private Solo solo;

    /**
     * True: The Activity under test will be launched before each test
     * annotated with Test and before methods annotated with Before, and it will be
     * terminated after the test is completed and methods annotated with After are finished
     */
    @Rule
    public ActivityTestRule<Login> rule = new ActivityTestRule< >
            (Login.class, true, true);

    /**
     * Setup instrumentation registry (from robotium library)
     * holds all the elements from user interface
     * Rule obj interacts with user_calendar activity class, so it tells us which
     * activity to get info from
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * declaring another variable and using rule obj already created
     * trying to get activity that we already created
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * build up the app automatically, the solo does the writing part
     * inform solo obj that the activity that we are currently in is the
     * main activity class (solo should be created before each test)
     */
    @Test
    public void checkToDoHabitList() {
        // checks to make sure we are in the right activity
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.login));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", Home.class);
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        //wait for add habit fragment to open
        solo.waitForDialogToOpen(1000);
        //enter data for habit
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), "Walking");
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), "Walk 10,000 steps each week");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));
        solo.pressSpinnerItem(0, 0);

        //make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, "Public"));
        solo.clickOnView(solo.getView(R.id.Date));

        //wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);
        solo.setDatePicker(0, 2021, 10,04);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(1000);

        //make sure that if a field is left empty that you will be presented with an error message
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("Please choose which days you would like this event to occur.", 1, 1000));
        solo.clickOnView(solo.getView(R.id.button_monday));
        solo.clickOnView(solo.getView(R.id.button_thursday));
        solo.clickOnView(solo.getView(R.id.button_friday));

        //add habit
        solo.clickOnButton("Add");
        solo.waitForDialogToClose(1000);

        //make sure habit shows up in list
        assertTrue(solo.waitForText("Walking", 1, 1000));

        // checks to make sure the activity has switched to the Home activity
        solo.clickOnView(solo.getView(R.id.calendar));
        solo.assertCurrentActivity("Wrong", UserCalendar.class);

        // wait for habit event fragment to open
        solo.clickInList(1);
        solo.waitForDialogToOpen(1000);
        // enter the data for habit event
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "Walked 5000 steps");
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "50");
        solo.clickOnButton("Save");
        solo.waitForDialogToClose(1000);

        //solo.clickInList(2);
        //solo.waitForDialogToOpen(1000);
        //solo.enterText((EditText) solo.getView(R.id.editText_duration), "50");
        //solo.clickOnButton("Save");
        //solo.waitForDialogToClose(1000);

        // click on a habit in the to do list

    }

}
