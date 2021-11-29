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
 * This class does the android testing for EditUserScreen.
 */
public class EditUserScreenAndroidTests {
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

    @After
    public void tearDown() throws Exception {
        solo.assertCurrentActivity("Wrong", Login.class);

        solo.finishOpenedActivities();
    }

    /**
     * This test checks if the sign out functionality works properly.
     */
    @Test
    public void test1(){
        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "hari@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.hideSoftKeyboard();
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", Home.class);

        solo.clickOnView(solo.getView(R.id.homeUserButton));

        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.signOutbutton));

        solo.assertCurrentActivity("wrong", Login.class);
    }
}
