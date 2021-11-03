package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
 used
 */
public class LoginAndroidTest {
    private Solo solo;
    private boolean initialTouchMode = true;

    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);

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
     * Click sign in button when email field is empty and check for error message
     * then do the same for when password field is empty
     * then have both email and password be incorrect and check for error message
     */
    @Test
    public void checkEmptySignIn(){
        //Asserts that the current activity is the Login Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        //click the login button when email and password fields are empty
        Button signInBtn = (Button) solo.getView("loginBtn");
        solo.clickOnView(signInBtn); //Select SIGN IN Button
        /* True if there is a text: Email is required,
        wait at least 2 seconds and find one minimum match. */
        assertTrue(solo.waitForText("Email is required", 1, 2000));

        //click the login button when email is non empty and the password field is empty
        solo.enterText((EditText) solo.getView(R.id.email), "null");
        solo.clickOnView(signInBtn);
        /* True if there is a text: Password is required,
        wait at least 2 seconds and find one minimum match. */
        assertTrue(solo.waitForText("Password is required", 1, 2000));

        //click the login button when email and password do not match a user
        solo.enterText((EditText) solo.getView(R.id.password), "null");
        solo.clickOnView(signInBtn);
        /* True if there is a text: Email or password entered is incorrect
        , wait at least 2 seconds and find one minimum match. */
        assertTrue(solo.waitForText("Email or password entered is incorrect", 1, 2000));
    }

    /**
     * Function registers a new user then tests if they can be logged in successfully
     * tests it with an incorrect and then correct password
     */
    @Test
    public void registerSignInTest(){
        //Asserts that the current activity is the Login Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        //click register button
        TextView register = (TextView) solo.getView("register");
        solo.clickOnView(register); //Select register text
        //Asserts that the current activity is the Register Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Register.class);

        //add this to strings.xml
        //register a test user
        solo.enterText((EditText) solo.getView(R.id.LastName), "User");
        solo.enterText((EditText) solo.getView(R.id.FirstName), "Test");
        solo.enterText((EditText) solo.getView(R.id.EditTextEmail), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.TextPassword), "testPassword");
        solo.enterText((EditText) solo.getView(R.id.TextConfirmPassword), "testPassword");

        Button createAcc = (Button) solo.getView("createAccBtn");
        solo.clickOnView(createAcc); //Select register text
        //Asserts that the current activity is the Login Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        //login the user (using the wrong password)
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "WrongTestPassword");

        Button signInBtn = (Button) solo.getView("loginBtn");
        solo.clickOnView(signInBtn); //Select SIGN IN Button
        /* True if there is a text: Email or password entered is incorrect
        , wait at least 2 seconds and find one minimum match. */
        assertTrue(solo.waitForText("Email or password entered is incorrect", 1, 2000));

        solo.clearEditText((EditText) solo.getView(R.id.password)); //Clear the password EditText
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");  //enter the correct password
        solo.clickOnView(signInBtn); //Select SIGN IN Button (switches to home)
        //Asserts that the current activity is the Home Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Home.class);

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
