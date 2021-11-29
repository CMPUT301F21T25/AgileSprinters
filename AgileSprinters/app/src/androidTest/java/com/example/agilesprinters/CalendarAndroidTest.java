package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.time.LocalDate;

/**
 * This class provides testing for the calendar activity.
 *
 * @author Sai Rasazna Ajerla, Leen Alzebdeh
 *
 * Note: there is no way, using robotium, to test adding an image thus it was not tested here
 * Note: there is no way to test editing the location (by dragging the marker), thus is was not tested
 * Note: there is no way to give permission to location permission using robotium. So when testing for
 *       the first time, please give the location permissions and the test or else the test cases will fail.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// Implementing our test cases
public class CalendarAndroidTest {
    // Instantiate obj from Solo class, to perform UI activities like tapping, writing
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>
            (LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        logInTestUser();
    }

    /**
     * if a user is logged in, sign them out then sign in the test user
     */
    private void logInTestUser() {

        try {
            solo.assertCurrentActivity("Wrong", LoginActivity.class);
        } catch (Exception e) {
            signOutUser();
        }

        // checks to make sure we are in the right activity
        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        // Log In to the test account
        solo.enterText((EditText) solo.getView(R.id.email), "sai@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", HomeActivity.class);
    }

    /**
     * This adds a habit
     * with the specified privacy setting
     */
    private void checkAddHabit(String privacy, int loc, String title) {

        solo.waitForDialogToOpen(1000);

        solo.clickOnView(solo.getView(R.id.add_habit_button));

        // wait for add habit fragment to open
        solo.waitForDialogToOpen(1000);

        // enter data to create a habit
        solo.enterText((EditText) solo.getView(R.id.habit_title_editText), title);
        solo.enterText((EditText) solo.getView(R.id.habit_reason_editText), title + " 10,000");
        solo.clickOnView(solo.getView(R.id.privacy_spinner));

        if (loc == 0) solo.pressSpinnerItem(0, loc);
        else solo.pressMenuItem(loc + 1);

        // make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, privacy));

        solo.clickOnView(solo.getView(R.id.Date));
        // wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);

        // set a date
        solo.setDatePicker(0, 2021, 10, 01);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(1000);

        int dayIds[] = {R.id.button_sunday, R.id.button_monday, R.id.button_Tuesday,
                R.id.button_wednesday, R.id.button_thursday,
                R.id.button_friday, R.id.button_saturday};

        for (int i = 0; i < dayIds.length; i++)
            solo.clickOnView(solo.getView(dayIds[i]));

        // Successfully adds a public habit
        solo.clickOnButton("Add");
        solo.waitForDialogToClose(1000);

        // make sure habit shows up in list
        assertTrue(solo.waitForText(title, 1, 1000));

        // Adding another habit which is private
    }

    /**
     * This test will check if the habits planned for the day are displayed under to-do tasks and
     * also checks if adding an habit event will be stored or not  (User Story 2.01.01, 2.02.01)
     */
    @Test
    public void testAddEvent() {

        checkAddHabit("Public", 1, "Walking");
        checkAddHabit("Private", 2, "Running");

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendarActivity.class));

        String todayDay = LocalDate.now().getDayOfWeek().toString();

        // checks to make sure that the habit planned for the day is displayed
        if (!todayDay.matches("SATURDAY") || todayDay.matches("SUNDAY")) {
            assertTrue(solo.waitForText("Walking", 1, 1000));
        }

        // wait for habit event fragment to open
        solo.clickOnText("Walking");
        solo.waitForDialogToOpen(1000);
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "Walked 5000 steps");
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "50");
        solo.clickOnView(solo.getView(R.id.duration_spinner));
        solo.pressMenuItem(2);

        // make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, "mins"));

        solo.clickOnView(solo.getView(R.id.add_location));
        solo.waitForDialogToOpen(5000);
        solo.clickOnButton("SAVE ADDRESS");

        // add habit event
        solo.clickOnButton("Save");
        solo.waitForDialogToClose(1000);

        // makes sure if the habit event is successfully added
        solo.clickOnText("Walked 5000 steps");
        //solo.waitForDialogToOpen(1000);
        assertTrue(solo.waitForText("Edmonton,Canada")
                | solo.waitForText("Mountain View, California, United States"));

        solo.clickOnButton("Save");

        // wait for habit event fragment to open
        solo.clickOnText("Running");
        solo.waitForDialogToOpen(1000);

        // enter the data for habit event
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "Evening run");
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "50");
        solo.clickOnView(solo.getView(R.id.duration_spinner));
        solo.pressMenuItem(2);

        // make sure that spinner is set to item selected
        assertTrue(solo.isSpinnerTextSelected(0, "mins"));

        solo.clickOnView(solo.getView(R.id.add_location));
        solo.waitForDialogToOpen(5000);
        solo.clickOnButton("SAVE ADDRESS");

        // add the private habit event
        solo.clickOnButton("Save");
        solo.waitForDialogToClose(500);

        solo.clickOnText("Evening run");
        //solo.waitForDialogToOpen(400);
        assertTrue(solo.waitForText("Edmonton,Canada")
                | solo.waitForText("Mountain View, California, United States"));

        solo.clickOnButton("Save");

        // makes sure if the tag for a private event shows up
        assertTrue(solo.waitForText("Private Event", 1, 1000));
    }

    /**
     * Checks that a public event gets added to the forum
     */
    @Test
    public void testPublicEventInForum() {

        testAddEvent();

        // makes sure the activity is switched to forum activity
        solo.clickOnView(solo.getView(R.id.forum));
        assertTrue(solo.waitForActivity(ForumActivity.class));

        // makes sure if the habit event is successfully added in forumn
        assertTrue(solo.waitForText("Walked 5000 steps", 1, 1000));

        assertTrue(solo.waitForText("Edmonton,Canada")
                | solo.waitForText("Mountain View, California, United States"));

    }

    /**
     * Checks that a private event is not in forum
     */
    @Test
    public void testPrivateEventNotInForum() {

        testAddEvent();

        // check to make sure the activity is switched to forum activity
        solo.clickOnView(solo.getView(R.id.forum));
        assertTrue(solo.waitForActivity(ForumActivity.class));

        // makes sure the private event is not added to forum
        assertFalse(solo.waitForText("Evening run", 1, 1000));
    }

    /**
     * test if an edited event is edited in forum as well
     */
    @Test
    public void testEditEvent() {

        testAddEvent();

        // check to make sure the activity is switched to calendar activity
        assertTrue(solo.waitForActivity(UserCalendarActivity.class));

        // wait for habit event fragment to open
        solo.clickOnText("Walked 5000 steps");
        solo.waitForDialogToOpen(1000);

        // make sure that if a field is given wrong input
        // it will present an error message
        solo.clearEditText((EditText) solo.getView(R.id.editText_comment));
        solo.enterText((EditText) solo.getView(R.id.editText_comment), "Walk with friend");

        solo.clearEditText((EditText) solo.getView(R.id.editText_duration));
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "70");
        solo.clickOnView(solo.getView(R.id.duration_spinner));
        solo.pressMenuItem(2);

        solo.clickOnView(solo.getView(R.id.editText_location));
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("DELETE");
        solo.waitForDialogToClose(1000);

        solo.clickOnButton("Save");

        // checks to make sure the error message is displayed
        assertTrue(solo.waitForText("minutes value must be between 0 and 60", 1, 1000));

        // enter the correct information and save
        solo.clearEditText((EditText) solo.getView(R.id.editText_duration));
        solo.enterText((EditText) solo.getView(R.id.editText_duration), "60");
        assertTrue(solo.waitForText("60", 1, 1000));

        solo.clickOnButton("Save");

        // waits for the dialog to close
        solo.waitForDialogToClose(1000);

        // makes sure edited content of the habit event shows up in list
        assertTrue(solo.waitForText("60 mins", 1, 1000));

        // check to make sure the activity is switched to forum activity
        solo.clickOnView(solo.getView(R.id.forum));
        assertTrue(solo.waitForActivity(ForumActivity.class));

        // check to make sure that the edits on the event are displayed in forum
        assertTrue(solo.waitForText("Walk with friend", 1, 1000));

    }

    /**
     * This test will check to see the planned
     * and completed events in the past.
     */
    @Test
    public void testCheckPastHabitEvents() {

        testAddEvent();

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendarActivity.class));

        // Opening a calendar to check previous events
        solo.clickOnView(solo.getView(R.id.title1));

        // wait for datePicker dialog to open
        solo.waitForDialogToOpen(1000);

        solo.setDatePicker(0, 2021, 10, 02);
        solo.clickOnButton("OK");
        solo.waitForDialogToClose(1000);

        // checks to see if the planned habit is displayed in the past for that day
        assertTrue(solo.waitForText("Walking", 1, 1000));
    }

    /**
     * This test will check if the private event
     * is displayed in forum when shared
     */
    @Test
    public void testSharePrivateHabitEvent() {

        testAddEvent();

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendarActivity.class));

        // wait for habit event fragment to open
        solo.clickOnText("Evening run");
        solo.waitForDialogToOpen(1000);

        // wait for share event fragment to open
        solo.clickOnButton("Share");
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");
        solo.waitForDialogToClose(1000);

        // check to make sure the activity is switched to forum activity
        solo.clickOnView(solo.getView(R.id.forum));
        assertTrue(solo.waitForActivity(ForumActivity.class));

        // makes sure the private event is added to forum
        assertTrue(solo.waitForText("Evening run", 1, 1000));

    }

    /**
     * This test will check to see the deleting
     * a habit event is successful or not (2.04.01)
     */
    @Test
    public void testDeleteHabitEvent() {

        testAddEvent();

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.calendar));
        assertTrue(solo.waitForActivity(UserCalendarActivity.class));

        // wait for habit event fragment to open
        solo.clickOnText("Walked 5000 steps");
        solo.waitForDialogToOpen(1000);

        // wait for delete event fragment to open
        solo.clickOnButton("Delete");
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");
        solo.waitForDialogToClose(1000);

        // checks to see if the planned habit is displayed in the past for that day
        assertFalse(solo.waitForText("Walked 5000 steps", 1, 1000));

        // checks to see if deleting a habit, will delete its forum events
        solo.clickOnView(solo.getView(R.id.forum));
        assertTrue(solo.waitForActivity(ForumActivity.class));
        // checks if deleting the habit will remove it from to do list
        assertFalse(solo.waitForText("Walked 5000 steps", 1, 1000));
    }

    /**
     * This test will check to see if deleting a
     * habit will delete all of its events in calendar
     * and forum as well
     */
    @Test
    public void testDeleteHabit() {

        testAddEvent();

        solo.clickOnView(solo.getView(R.id.home));

        solo.clickLongOnText("Walking");

        //wait for deleteHabitFragment to open
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");
        solo.waitForDialogToClose(1000);

        solo.clickLongOnText("Running");

        //wait for deleteHabitFragment to open
        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Yes");
        solo.waitForDialogToClose(1000);

        solo.clickOnView(solo.getView(R.id.calendar));

        // checks to see if deleting a habit, will delete its events
        assertTrue(solo.waitForActivity(UserCalendarActivity.class));

        // checks if deleting the habit will remove it from to do list
        assertFalse(solo.waitForText("Walking", 1, 1000));
        assertFalse(solo.waitForText("Evening run", 1, 1000));

        solo.clickOnView(solo.getView(R.id.forum));

        // checks to see if deleting a habit, will delete its forum events
        assertTrue(solo.waitForActivity(ForumActivity.class));
        // checks if deleting the habit will remove it from to do list
        assertFalse(solo.waitForText("Evening run", 1, 1000));

    }

    /**
     * delete habits after a test is run
     */
    private void deleteHabitEvents() {
        if(!solo.getCurrentActivity().equals(HomeActivity.class)){
            solo.clickOnView(solo.getView(R.id.home));
        }
        // checks to see if deleting a habit, will delete its events
        //assertTrue(solo.getCurrentActivity().equals(HomeActivity.class));
        solo.assertCurrentActivity("Home", HomeActivity.class);

        if (solo.waitForText("Walking")) {
            solo.clickLongOnText("Walking");
            //wait for deleteHabitFragment to open
            solo.waitForDialogToOpen(1000);
            solo.clickOnButton("Yes");
            solo.waitForDialogToClose(1000);
        }
        if (solo.waitForText("Running")) {
            solo.clickLongOnText("Running");
            //wait for deleteHabitFragment to open
            solo.waitForDialogToOpen(1000);
            solo.clickOnButton("Yes");
            solo.waitForDialogToClose(1000);
        }
    }

    /**
     * signs out the current user
     */
    private void signOutUser(){
        solo.clickOnView(solo.getView(R.id.home));
        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.homeUserButton));
        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.signOutbutton));
        solo.assertCurrentActivity("Wrong", LoginActivity.class);

    }
    /**
     * delete habits and sign out user
     */
    @After
    public void runAfterTest() {
        deleteHabitEvents();
        signOutUser();
        solo.finishOpenedActivities();

    }

}