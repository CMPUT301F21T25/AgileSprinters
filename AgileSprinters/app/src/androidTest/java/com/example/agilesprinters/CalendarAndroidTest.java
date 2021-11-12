package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;

/**
 * This class provides testing for the calendar activity.
 * Please note that tests must be ran individually
 * and the cache must be cleared between each for them to work due to the app automatically
 * going to the home page after a single sign in on the device.
 *
 * @author Sai Rasazna Ajerla
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// Implementing our test cases
public class CalendarAndroidTest {
    // Instantiate obj from Solo class, to perform UI activities like tapping, writing
    private Solo solo;

    @Rule
    public ActivityTestRule<Login> rule = new ActivityTestRule< >
            (Login.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * This test will check to make sure
     * that user logs in happens successfully
     */
    @Test
    public void stage1_checkLogIn() {

        // checks to make sure we are in the right activity
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", Home.class);

    }

    /**
     * This test will check to make sure
     * when a habit is added it shows up in the list
     */
    @Test
    public void stage2_checkAddHabit() {

        // checks to make sure we are in the right activity
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        solo.clickOnView(solo.getView(R.id.add_habit_button));

        // wait for add habit fragment to open
        solo.waitForDialogToOpen(1000);

        // enter data for habit
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), "Walking");
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), "Walk 10,000 steps each week");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));
        solo.pressSpinnerItem(0, 0);

        // make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, "Public"));
        solo.clickOnView(solo.getView(R.id.Date));
        // wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);
        solo.setDatePicker(0, 2021, 9, 30);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(1000);

        //check that dialog doesn't days are not selected
        solo.clickOnButton("Add");
        assertTrue(solo.waitForText("Please choose which days you would like this event to occur.", 1, 1000));
        solo.clickOnView(solo.getView(R.id.button_monday));
        solo.clickOnView(solo.getView(R.id.button_Tuesday));
        solo.clickOnView(solo.getView(R.id.button_wednesday));
        solo.clickOnView(solo.getView(R.id.button_thursday));
        solo.clickOnView(solo.getView(R.id.button_friday));

        // add habit
        solo.clickOnButton("Add");
        solo.waitForDialogToClose(1000);

        // make sure habit shows up in list
        assertTrue(solo.waitForText("Walking", 1, 1000));


        //Adding another habit here
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        // wait for add habit fragment to open
        solo.waitForDialogToOpen(1000);
        // enter data for habit
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), "Running");
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), "Run a 5k");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));
        solo.pressSpinnerItem(0, 0);

        // make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, "Public"));
        solo.clickOnView(solo.getView(R.id.Date));

        // wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);

        solo.setDatePicker(0, 2021, 9, 30);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(1000);
        solo.clickOnView(solo.getView(R.id.button_saturday));
        solo.clickOnView(solo.getView(R.id.button_sunday));

        // add habit
        solo.clickOnButton("Add");

        solo.waitForDialogToClose(1000);

        // make sure habit shows up in list
        assertTrue(solo.waitForText("Running", 1, 1000));

    }

    /**
     * This test will check if the habits
     * planned for the day are displayed under to-do tasks and also checks
     * if adding an habit event will be stored or not  (User Story 2.01.01, 2.02.01)
     */
    @Test
    public void stage3_checkAddHabitEvent() {
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendar.class));

        String todayDay = LocalDate.now().getDayOfWeek().toString();

        // checks to make sure the habit planned to-do is displayed
        if (!todayDay.matches("SATURDAY") || todayDay.matches("SUNDAY")) {
            assertTrue(solo.waitForText("Walking", 1, 1000));
        }

        // wait for habit event fragment to open
        solo.clickInList(1, 0);
        solo.waitForDialogToOpen(1000);

        // enter the data for habit event
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "Walked 5000 steps");
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "50");

        // add habit event
        solo.clickOnButton("Save");
        solo.waitForDialogToClose(1000);

        // make sure habit event shows up in list
        assertTrue(solo.waitForText("Walked 5000 steps", 1, 1000));
    }

    /**
     * This test will check if edits made on the
     * habit events are updated properly in the UI/database
     * (User Story 2.04.01, 2.05.01)
     */
    @Test
    public void stage4_checkEditHabitEvent() {
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendar.class));

        // wait for habit event fragment to open
        solo.clickInList(1, 1);
        solo.waitForDialogToOpen(1000);

        // make sure that if a field is left empty that you will be presented with an error message
        solo.clearEditText((EditText) solo.getView(R.id.editText_comment));
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "Walked 7000 steps");

        solo.clearEditText((EditText) solo.getView(R.id.editText_duration));
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "");
        solo.clickOnButton("Save");

        // displaying the error
        assertTrue(solo.waitForText("This field cannot be blank", 1, 1000));

        solo.enterText((EditText) solo.getView(R.id.editText_duration), "70");

        //edit the habit
        solo.clickOnButton("Save");
        solo.waitForDialogToClose(1000);

        // make sure habit shows up in list
        assertTrue(solo.waitForText("Walked 7000 steps", 1, 1000));
    }

    /**
     * This test will check to see the planned
     * and completed events in the past. This will also check the
     * deletion of aa habit event
     * (2.04.01, 2.05.01, 2.06.01)
     */
    @Test
    public void stage5_checkViewHabitEvents() {
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendar.class));

        solo.clickOnView(solo.getView(R.id.calendar_button));

        // wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);

        solo.setDatePicker(0, 2021, 9, 31);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(1000);

        // checks to see if the planned habit is displayed in the past for that day
        assertTrue(solo.waitForText("Running", 1, 1000));

        solo.clickOnView(solo.getView(R.id.calendar_button));

        // wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);

        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnButton("OK");

        // wait for datePicker dialog to open
        solo.waitForDialogToClose(1000);

        // wait for habit event fragment to open
        solo.clickInList(1, 1);
        solo.waitForDialogToOpen(1000);

        // checks to see if the user is deleted
        solo.clickOnButton("Delete");
        solo.waitForDialogToClose(1000);
    }

    /**
     * This test will check to see if deleting a habit will delete
     * all of its events as well
     * (2.06.01)
     */
    @Test
    public void stage6_checkDeleteHabitEvents() {
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "test10@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "test10");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        solo.clickLongInList(1);

        //wait for deleteHabitFragment to open
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");
        solo.waitForDialogToClose(1000);

        solo.clickLongInList(1);

        //wait for deleteHabitFragment to open
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");
        solo.waitForDialogToClose(1000);

        solo.clickOnView(solo.getView(R.id.calendar));

        // checks to see if the
        assertTrue(solo.waitForActivity(UserCalendar.class));

        // checks if deleting the habit will remove it from to do list
        assertFalse(solo.waitForText("Walking", 1, 1000));
    }
}
