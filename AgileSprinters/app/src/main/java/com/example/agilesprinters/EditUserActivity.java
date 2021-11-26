package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;

public class EditUserActivity extends AppCompatActivity {
    private User user;
    private String UID;
    private String nameStr;
    private static final String TAG = "Habit";
    private Database database = new Database();
    FirebaseFirestore db;
    private Intent intent;
    private String collectionPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_user);

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


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(EditUserActivity.this, Login.class);
                user = null;
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
                user = null;
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(EditUserActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    /**
     * This method deletes a user from the database and all its associated data and habits and events.
     */
    private void deleteUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.USER_DEL_LOG));
                            deleteUserHabits();
                            deleteUserData();
                        } else {
                            Log.d(TAG, getString(R.string.USER_NOT_DEL_LOG));
                        }
                    }
                });
    }

    /**
     * This method deletes all data (email, password etc) associated with a user.
     */
    private void deleteUserData() {
        CollectionReference collectionReference = db.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = getString(R.string.USERS);
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            }
        });
    }

    /**
     * This method deletes all of the user's habit and associated events when the user is deleted.
     */
    private void deleteUserHabits() {
        CollectionReference collectionReference = db.collection(getString(R.string.HABIT));
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = "Habit";
                            deleteHabitInstances((String) doc.getId());
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            }
        });
        return;
    }

    public void deleteHabitInstances(String HID) {
        CollectionReference collectionReference = db.collection("HabitEvents");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get("UID")));
                    if (HID.matches((String) doc.getData().get("HID"))) {
                        if (doc.getId() == null) {
                            return;
                        } else {
                            collectionPath = "HabitEvents";
                            database.deleteData(collectionPath, doc.getId(), TAG);
                        }
                    }
                }
            }
        });
        return;
    }
}