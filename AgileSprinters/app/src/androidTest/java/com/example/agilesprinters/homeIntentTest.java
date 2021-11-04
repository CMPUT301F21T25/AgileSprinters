package com.example.agilesprinters;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
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

/**
 * This class provides testing for the home activity. Please note that tests must be ran individually
 * and the cache must be cleared between each for them to work due to the app automatically going
 * to the home page after a single sign in on the device.
 */
public class homeIntentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);

    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        //Login to test account
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");
        solo.clickOnView(solo.getView(R.id.login));

        //checks to make sure the activity has switched to the Home activity
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
        solo.clickOnButton("OK");
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
        //Login to test account
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");
        solo.clickOnView(solo.getView(R.id.login));

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
        solo.clickOnButton("OK");
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
        solo.clearEditText((EditText) solo.getView(R.id.view_edit_habit_reason_editText));
        solo.enterText((EditText) solo.getView(R.id.view_edit_habit_reason_editText), "Run 1.5 hours per week");
        solo.clickOnButton("Save Changes");
        solo.waitForDialogToClose(1000);

        //make sure reason was actually changed and shows in the list
        assertTrue(solo.waitForText("Run 1.5 hours per week"));
    }

    /*
    /**
     * This test makes sure that a habit is deleted

    @Test
    public void deleteHabit(){
        //Login to test account
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");
        solo.clickOnView(solo.getView(R.id.login));
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
    */


    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
