package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.widget.AutoCompleteTextView;
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

/**
 * This class provides testing for the search bar in the forum activity
 *
 * @author Sai Rasazna Ajerla
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
// Implementing our test cases
public class SearchForUserAndroidTest {
    // Instantiate obj from Solo class, to perform UI activities like tapping, writing
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule< >
            (LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * This test will check if searching other users
     * from search bar shows up
     */
    @Test
    public void stage1_checkSearchBar() {
        // checks to make sure we are in the right activity
        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        // Log In to the test account
        solo.enterText((EditText) solo.getView(R.id.email), "sai@test.com");
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        // check to make sure the activity is switched to calendar activity
        solo.clickOnView(solo.getView(R.id.forum));
        assertTrue(solo.waitForActivity(ForumActivity.class));

        solo.clickOnView(solo.getView(R.id.users_list));

        solo.enterText((AutoCompleteTextView) solo.getView(R.id.users_list), "riya");

        assertTrue(solo.waitForText("riya@gmail.com", 1, 1000));

        solo.clickOnText("riya@gmail.com");

        assertTrue(solo.waitForText("Followers", 1, 1000));
        solo.goBack();
    }

    /**
     * Closes the activity after test
     */
    @After
    public void tearDown() {
        if(!solo.getCurrentActivity().equals(HomeActivity.class)){
            solo.clickOnView(solo.getView(R.id.home));
        }

        solo.clickOnView(solo.getView(R.id.homeUserButton));
        solo.waitForActivity(EditUserActivity.class);
        solo.clickOnView(solo.getView(R.id.signOutbutton));
        solo.finishOpenedActivities();
    }
}