package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class FollowFollowingAndroidTest {
    private Solo solo;
    private User user;
    private FirebaseFirestore db;


    @Rule
    public ActivityTestRule<Login> rule = new ActivityTestRule< >
            (Login.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong", Login.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "hari@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.hideSoftKeyboard();
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", Home.class);
    }

    @Test
    public void stage1_followers() {
        solo.assertCurrentActivity("Wrong", Home.class);

        solo.clickOnView(solo.getView(R.id.followerCount));
        solo.waitForDialogToOpen(1000);

        TextView textView = (TextView) solo.getView(R.id.titleTextView);
        String str = textView.getText().toString();

        assertEquals(str, "Followers");

        ArrayList<TextView> textViewArrayList = solo.clickInList(0, 0);
        String str1 = textViewArrayList.get(0).getText().toString();
        assertEquals(str1, "Leen Alzebdeh");

        solo.assertCurrentActivity("Wrong", OtherUserScreen.class);
    }

    @Test
    public void stage2_following() {
        solo.assertCurrentActivity("Wrong", Home.class);

        solo.clickOnView(solo.getView(R.id.followingCount));
        solo.waitForDialogToOpen(1000);

        TextView textView = (TextView) solo.getView(R.id.titleTextView);
        String str = textView.getText().toString();

        assertEquals(str, "Following");

        ArrayList<TextView> textViewArrayList = solo.clickInList(0, 0);
        String str1 = textViewArrayList.get(0).getText().toString();
        assertEquals(str1, "riya patel");

        solo.assertCurrentActivity("Wrong", OtherUserScreen.class);
    }

    @After
    public void tearDown() throws Exception {

        solo.goBack();
        solo.waitForDialogToOpen(1000);
        solo.assertCurrentActivity("Wrong", Home.class);

        solo.clickOnView(solo.getView(R.id.homeUserButton));

        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.signOutbutton));

        solo.assertCurrentActivity("Wrong", Login.class);

        solo.finishOpenedActivities();
    }
}
