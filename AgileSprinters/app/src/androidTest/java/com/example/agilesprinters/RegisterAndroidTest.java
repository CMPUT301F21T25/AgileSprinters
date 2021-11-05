package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegisterAndroidTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Register> rule =
            new ActivityTestRule<>(Register.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
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
        solo.assertCurrentActivity(solo.getString(R.string.wrong_activity), Register.class);

        //click the Register button when email and password fields are empty
        tryRegister(R.id.FirstName, "", solo.getString(R.string.firstNameErr));
        tryRegister(R.id.FirstName, solo.getString(R.string.emptyString), solo.getString(R.string.lastNameErr));
        tryRegister(R.id.LastName, solo.getString(R.string.emptyString), solo.getString(R.string.emailErr));
        tryRegister(R.id.EditTextEmail, solo.getString(R.string.emptyString), solo.getString(R.string.passwordErr));
        tryRegister(R.id.TextPassword, solo.getString(R.string.emptyString), solo.getString(R.string.confirmPasswordErr));
        tryRegister(R.id.TextConfirmPassword, "null", solo.getString(R.string.passwordMatchErr));
    }

    /**
     * This function enters given text into an editText if desired,
     * then sign in is pressed and a message is checked for on screen
     * @param id
     *  Give the edittext field to have text entered into,
     * if -1 then no field will be updated {@see int}
     * @param input
     *  Give the string to be entered into the specified editText {@See String}
     * @param resultText
     *  Give the expected text that is expected to appear on screen after sign in is clicked
     */
    private void tryRegister(int id, String input, String resultText){
        String signInStr = solo.getString(R.string.create_account);  //string of sign in button

        if (id!=-1) {
            solo.enterText((EditText) solo.getView(id), input);  //enter input into edit text
        }
        solo.clickOnButton(signInStr);
        /* True if there is a text as given in input on the screen
        , wait at least 2 seconds and find one minimum match. */
        assertTrue(solo.waitForText(resultText, 1, 2000));
    }

    @Test
    public void registerTestUser() {
        //Asserts that the current activity is the Register Activity. Otherwise, show "Wrong Activity"
        solo.assertCurrentActivity(solo.getString(R.string.wrong_activity), Register.class);

        //register a test user
        solo.enterText((EditText) solo.getView(R.id.LastName), solo.getString(R.string.last_test));
        solo.enterText((EditText) solo.getView(R.id.FirstName), solo.getString(R.string.first_test));
        solo.enterText((EditText) solo.getView(R.id.EditTextEmail), solo.getString(R.string.email_test));
        solo.enterText((EditText) solo.getView(R.id.TextPassword), solo.getString(R.string.password_test));
        solo.enterText((EditText) solo.getView(R.id.TextConfirmPassword), solo.getString(R.string.password_test));

        solo.clickOnButton(solo.getString(R.string.create_account)); //Select register text
        //Asserts that the current activity is the Register Activity. Otherwise, show "Wrong Activity"
        assertTrue(solo.waitForText(solo.getString(R.string.userExistsErr), 1, 2000));
    }


    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}