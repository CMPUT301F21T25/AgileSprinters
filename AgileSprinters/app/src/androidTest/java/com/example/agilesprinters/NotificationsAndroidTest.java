package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsAndroidTest {
    private Solo solo;
    private FirebaseFirestore db;
    private Database database = new Database();
    private static final String TAG = "users";
    private User user;

    public void updateUserDoc(User user) {
        db = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        String collectionPath = "users";

        data.put("UID", user.getUserID());
        data.put(solo.getString(R.string.EMAIL_ID_STR), user.getEmailId());
        data.put(solo.getString(R.string.FIRST_NAME_STR), user.getFirstName());
        data.put(solo.getString(R.string.LAST_NAME_STR), user.getLastName());
        data.put("followers", user.getFollowersList());
        data.put("following", user.getFollowingList());
        data.put("follow request list", user.getFollowRequestList());
        // Makes a call to the database which handles it
        database.updateData(collectionPath, user.getUserID(), data, TAG);
    }

    public void clearUserFollowFollowing(String UID){
        DocumentReference otherUsersDoc = db.collection("users").document(UID);
        otherUsersDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "onComplete: " + documentSnapshot.getData());
                        String otherUID = (String) documentSnapshot.get("UID");
                        String firstName = (String) documentSnapshot.get("First Name");
                        String lastName = (String) documentSnapshot.get("Last Name");
                        String emailId = (String) documentSnapshot.get("Email ID");
                        ArrayList<String> followRequest = (ArrayList<String>) documentSnapshot.get("follow request list");
                        ArrayList<String> followers = (ArrayList<String>) documentSnapshot.get("followers");
                        ArrayList<String> following = (ArrayList<String>) documentSnapshot.get("following");

                        followRequest.clear();
                        followers.clear();
                        following.clear();
                        updateUserDoc(new User(otherUID, firstName, lastName, emailId, followers, following, followRequest));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     */
    @Test
    public void stage1_start() {
        Activity activity = rule.getActivity();
    }

    @Test
    public void stage2_checkNotificationsList(){
        //Login to test account
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        //checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong Activity", Home.class);
        user = (User) solo.getCurrentActivity().getIntent().getSerializableExtra("user");
        ArrayList<String> followerRequests = new ArrayList<>();
        followerRequests.add("JQKGab7QkWShfVLr3lJEflrj9gn1");
        followerRequests.add("5yTc0GXKanXbMvPmHuZct3AYPBn1");
        user.setFollowRequestList(followerRequests);
        updateUserDoc(user);

        solo.clickOnView(solo.getView(R.id.notification));
        assertTrue(solo.waitForActivity(Notifications.class));

        assertTrue(solo.waitForText("Hannah Desm", 1, 1000));
        assertTrue(solo.waitForText("testing22 testing22", 1, 1000));


        followerRequests.clear();
        user.setFollowRequestList(followerRequests);
        updateUserDoc(user);
    }

    @Test
    public void stage3_testAccept(){
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        //checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong Activity", Home.class);
        user = (User) solo.getCurrentActivity().getIntent().getSerializableExtra("user");
        ArrayList<String> followerRequests = new ArrayList<>();
        followerRequests.add("JQKGab7QkWShfVLr3lJEflrj9gn1");
        user.setFollowRequestList(followerRequests);
        updateUserDoc(user);

        solo.clickOnView(solo.getView(R.id.notification));
        assertTrue(solo.waitForActivity(Notifications.class));

        solo.clickInList(1);

        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Accept");
        solo.waitForDialogToClose(1000);

        assertFalse(solo.waitForText("Hannah Desm", 1, 2000));

        solo.clickOnView(solo.getView(R.id.home));
        assertTrue(solo.waitForText("1",1, 1000));

        user.getFollowRequestList().clear();
        user.getFollowersList().clear();
        updateUserDoc(user);

        clearUserFollowFollowing("JQKGab7QkWShfVLr3lJEflrj9gn1");
    }

    @Test
    public void stage_testDecline(){
        solo.assertCurrentActivity("Wrong Activity", Login.class);
        solo.enterText((EditText) solo.getView(R.id.email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.password), "testPassword");
        solo.clickOnView(solo.getView(R.id.loginBtn));

        //checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong Activity", Home.class);
        user = (User) solo.getCurrentActivity().getIntent().getSerializableExtra("user");
        ArrayList<String> followerRequests = new ArrayList<>();
        followerRequests.add("JQKGab7QkWShfVLr3lJEflrj9gn1");
        user.setFollowRequestList(followerRequests);
        updateUserDoc(user);

        solo.clickOnView(solo.getView(R.id.notification));
        assertTrue(solo.waitForActivity(Notifications.class));

        solo.clickInList(1);

        solo.waitForDialogToOpen(1000);
        solo.clickOnButton("Decline");
        solo.waitForDialogToClose(1000);

        assertFalse(solo.waitForText("Hannah Desm", 1, 2000));

        solo.clickOnView(solo.getView(R.id.home));
        assertTrue(solo.waitForText("0",2, 1000));
    }

    /**
     * Closes the activity after each test
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

}
