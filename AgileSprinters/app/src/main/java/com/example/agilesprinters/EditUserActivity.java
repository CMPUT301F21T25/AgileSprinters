package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

/**
 * This class handles sign out and delete user functionality
 *
 * @author Leen Alzebdeh, Hari Bheesetti
 *
 * Resources used:
 * google map documentation: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
 *
 */
public class EditUserActivity extends AppCompatActivity {

    /**
     * This variable contains the current user of the app
     */
    private User user;

    /**
     * This variable contains the user's ID
     */
    private String UID;

    /**
     * This variable contains the user's first and last name
     */
    private String nameStr;

    /**
     * This variable contains the tag used for the rest of the class (name of class)
     */
    private static final String TAG = EditUserActivity.class.getSimpleName();;

    /**
     * This variable contains the database class
     */
    private final Database database = new Database();

    /**
     * This variable contains an instance of FirebaseFireStore
     */
    FirebaseFirestore db =  FirebaseFirestore.getInstance();

    /**
     * This variable contains an instance of FirebaseAuth
     */
    private Intent intent;

    /**
     * This variable contains an instance of FirebaseAuth
     */
    private String collectionPath;

    /**
     * This function is called when the edit user activity starts
     *
     * @param savedInstanceState a reference to Bundle object that is passed into the onCreate method {@link Bundle } <br>
     *                           if null is passed an exception is thrown
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_edit_user);

        // sets the first and last name of the user
        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUserID();
            nameStr = user.getFirstName() + " " + user.getLastName();
        }

        TextView nameTextView = findViewById(R.id.userNameTextView);
        Button letterButton = findViewById(R.id.userButton);

        nameTextView.setText(nameStr);
        letterButton.setText(nameStr.substring(0, 1));

        Button signOutButton = findViewById(R.id.signOutbutton);
        Button deleteUserButton = findViewById(R.id.deleteUserButton);

        //handles click on sign out btn
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        //handles click on delete uaer btn
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUserAlert();
            }
        });
    }

    /**
     * This function handles a back press by making sure transition animations are not shown
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
    /**
     * This function creates an alert dialog to confirm if the yser wants to delete
     */
    private void deleteUserAlert() {
        new AlertDialog.Builder(EditUserActivity.this)
                .setTitle(getString(R.string.CONFIRM))
                .setMessage(getString(R.string.DEL_ALERT_MESSAGE))

                // deletes the user and everything associated
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser();
                        }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    /**
     * This method deletes a user from the database and all
     * its associated data and habits, events and forum posts.
     */
    private void deleteUser() {
            try {
                deleteUserHabits();
                deleteUserData();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, getString(R.string.USER_DEL_LOG));
                                    signOut();
                                } else {
                                    Log.d(TAG, getString(R.string.USER_NOT_DEL_LOG));
                                    Toast.makeText(EditUserActivity.this, getString(R.string.USER_NOT_DEL_LOG),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } catch (Exception e) {
                Log.d(TAG, getString(R.string.USER_NOT_DEL_LOG));
            }
    }
    /**
     * This method signs out a user and sends them to the login page
     */
    private void signOut(){
        user = null;
        try {
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(EditUserActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        } catch(Exception e){
            Toast.makeText(EditUserActivity.this, getString(R.string.USER_NOT_SIGN_OUT),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method deletes all data (email, password etc) associated with a user.
     */
    private void deleteUserData() {
        try {
            CollectionReference collectionReference = db.collection("users");
            collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
                //deletes the docs with matching UID
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) Objects.requireNonNull(doc.getData().get(getString(R.string.UID))))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = getString(R.string.USERS);
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(EditUserActivity.this, e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method deletes all of the user's habit and associated events when the user is deleted.
     */
    private void deleteUserHabits() {
        try {
            CollectionReference collectionReference = db.collection(getString(R.string.HABIT));
            collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    //deletes the hanits with matching UID
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = "Habit";
                            deleteHabitInstances(doc.getId());
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(EditUserActivity.this, e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function deletes all the habitInstances that are related to the user being deleted
     *
     * @param HID {@link String} Habits that need to be deleted.
     */
    public void deleteHabitInstances(String HID) {
        try {
            CollectionReference collectionReference = db.collection("HabitEvents");
            collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
                //deletes the docs with matching HID
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (HID.matches((String) Objects.requireNonNull(doc.getData().get("HID")))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = "HabitEvents";
                            deleteForumEventsDb((String) doc.getData().get("FID"));
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(EditUserActivity.this, e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This method deletes all forum posts associated with a user
     */
    public void deleteForumEventsDb(String FID) {
        try {
            CollectionReference collectionReference = db.collection("ForumPosts");
            collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
                //deletes the docs with matching FID
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (FID.matches((String) Objects.requireNonNull(doc.getData().get("FID")))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = "ForumPosts";
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(EditUserActivity.this, e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

}