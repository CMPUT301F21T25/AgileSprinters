package com.example.agilesprinters;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * This is a test class for the Register activity class. Provides various UI tests to ensure all
 * functions and UI elements work as intended.
 *
 * @author Hari Bheesetti
 */
public class RegisterAndroidTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Register> rule =
            new ActivityTestRule<>(Register.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Click sign in button when email field is empty and check for error message
     * then do the same for when password field is empty
     * then have both email and password be incorrect and check for error message
     */
    @Test
    public void checkEmptySignIn() {
        //Asserts that the current activity is the Register Activity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity(solo.getString(R.string.WRONG_ACTIVITY), Register.class);

        //click the Register button when email and password fields are empty
        tryRegister(R.id.FirstName);
        tryRegister(R.id.FirstName);
        tryRegister(R.id.LastName);
        tryRegister(R.id.EditTextEmail);
        tryRegister(R.id.TextPassword);
        tryRegister(R.id.TextConfirmPassword);
    }

    /**
     * This function sets all the fields to a non empty string and then changes one of them to be
     * an empty string. If the activity switches when the register button is clicked that means the
     * test failed.
     * @param id
     *  Give the id of field that needs to be null
     *
     */

    private void tryRegister(int id){
        String registerStr = solo.getString(R.string.CREATE_ACCOUNT);  //string of sign in button

        solo.enterText((EditText) solo.getView(R.id.FirstName), solo.getString(R.string.EMPTY_STRING));
        solo.enterText((EditText) solo.getView(R.id.LastName), solo.getString(R.string.EMPTY_STRING));
        solo.enterText((EditText) solo.getView(R.id.EditTextEmail), solo.getString(R.string.EMPTY_STRING));
        solo.enterText((EditText) solo.getView(R.id.TextPassword), solo.getString(R.string.EMPTY_STRING));
        solo.enterText((EditText) solo.getView(R.id.TextConfirmPassword), solo.getString(R.string.EMPTY_STRING));

        solo.enterText((EditText) solo.getView(id), "");  //enter input into edit text

        solo.clickOnButton(registerStr);
        /* True if there is a text as given in input on the screen
        , wait at least 2 seconds and find one minimum match. */
        solo.assertCurrentActivity(solo.getString(R.string.WRONG_ACTIVITY), Register.class);
    }

    /**
     * This test enters valid entries for all the field in register and checks if the activity switches
     * and for a log which is printed when a user is successfully added to the database. In this case
     * I am looking for a failure message since I am trying to add the same user again and again.
     */
    @Test
    public void registerTestUser() {
        //Asserts that the current activity is the Register Activity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity(solo.getString(R.string.WRONG_ACTIVITY), Register.class);

        //register a test user
        solo.enterText((EditText) solo.getView(R.id.LastName), solo.getString(R.string.LAST_TEST));
        solo.enterText((EditText) solo.getView(R.id.FirstName), solo.getString(R.string.FIRST_TEST));
        solo.enterText((EditText) solo.getView(R.id.EditTextEmail), solo.getString(R.string.EMAIL_TEST));
        solo.enterText((EditText) solo.getView(R.id.TextPassword), solo.getString(R.string.PASSWORD_TEST));
        solo.enterText((EditText) solo.getView(R.id.TextConfirmPassword), solo.getString(R.string.PASSWORD_TEST));

        solo.clickOnButton(solo.getString(R.string.CREATE_ACCOUNT)); //Select register text
        //Asserts that the current activity is the Register Activity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity(solo.getString(R.string.WRONG_ACTIVITY), Register.class);
    }


    /**
     * Close activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

}
