package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class EditUserScreenAndroidTests {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule< >
            (LoginActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "hari@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.hideSoftKeyboard();
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", HomeActivity.class);
    }

    @After
    public void tearDown() throws Exception {
        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        solo.finishOpenedActivities();
    }

    @Test
    public void test1(){
        solo.clickOnView(solo.getView(R.id.homeUserButton));

        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.signOutbutton));

        solo.assertCurrentActivity("wrong", LoginActivity.class);
    }

    /*
    @Test
    public void test2(){
        solo.clickOnView(solo.getView(R.id.homeUserButton));

        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.deleteUserButton));

        solo.assertCurrentActivity("wrong", Login.class);
    }
     */
}