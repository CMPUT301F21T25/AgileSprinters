package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

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

import java.util.ArrayList;

/**
 * This function tests the behavior of OtherUserScreen.
 */
public class OtherUserScreenActivityAndroidTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule< >
            (LoginActivity.class, true, true);

    /**
     * This function runs the setup for each test before the start of each test
     * @throws Exception
     */
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

    /**
     * This function handles the actions needed to properly finish the test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.goBack();

        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.homeUserButton));

        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.signOutbutton));

        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        solo.finishOpenedActivities();
    }

    /**
     * This function checks the info displayed is as expected.
     */
    @Test
    public void stage1FollowerFollowingCount() {
        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.followerCount));
        solo.waitForDialogToOpen(1000);

        TextView textView = (TextView) solo.getView(R.id.titleTextView);
        String str = textView.getText().toString();

        assertEquals(str, "Followers");

        ArrayList<TextView> textViewArrayList = solo.clickInList(0, 0);
        String str1 = textViewArrayList.get(0).getText().toString();
        assertEquals(str1, "HariTest User");

        solo.assertCurrentActivity("Wrong", OtherUserScreenActivity.class);

        TextView textView1 = (TextView) solo.getView(R.id.followingCount);
        String str2 = textView1.getText().toString();
        int i = Integer.parseInt(str2);

        assertEquals(i, 1);

        Button button = (Button) solo.getView(R.id.followButton);
        String str3 = button.getText().toString();
        assertEquals(str3, "Unfollow");
    }
}
