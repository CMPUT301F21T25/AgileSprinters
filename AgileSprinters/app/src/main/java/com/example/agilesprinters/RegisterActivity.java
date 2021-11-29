package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used when a user is signing up for the app. It takes the user details and
 * creates an account fo them in the authentication database.
 *
 * @author Hari Bheesetti
 */

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final String TAG = "EmailPassword";
    FirebaseFirestore db;

    /**
     * This function is called when the Register activity starts
     *
     * @param savedInstanceState a reference to Bundle object that is passed into the onCreate method {@link Bundle } <br>
     *                           if null is passed an exception is thrown
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        EditText password = findViewById(R.id.TextPassword);
        EditText confirmPassword = findViewById(R.id.TextConfirmPassword);
        EditText email = findViewById(R.id.EditTextEmail);
        EditText firstName = findViewById(R.id.FirstName);
        EditText lastName = findViewById(R.id.LastName);
        Button createAccountBtn = findViewById(R.id.createAccBtn);

        createAccountBtn.setOnClickListener(view -> {
            final String emailStr = email.getText().toString();
            final String passwordStr = password.getText().toString();
            final String passwordConfirmStr = confirmPassword.getText().toString();
            final String firstNameStr = firstName.getText().toString();
            final String lastNameStr = lastName.getText().toString();

            stringValidation(emailStr, passwordStr, passwordConfirmStr, firstNameStr, lastNameStr);
        });
    }

    /**
     * This function takes given user input and checks if the given input is in the right format.
     *
     * @param emailStr           {@link String}, user's email. If null then it calls the updateUI function
     *                           and passes error message
     * @param passwordStr        {@link String}, user's password. If null then it calls the updateUI
     *                           function and passes error message
     * @param passwordConfirmStr {@link String}, user's password. If null then it calls the updateUI
     *                           function and passes error message
     * @param firstNameStr       {@link String}, user's first name. If null then it calls the updateUI
     *                           function and passes error message
     * @param lastNameStr        {@link String}, user's last name. If null then it calls the updateUI
     *                           function and passes error message
     */
    private void stringValidation(String emailStr, String passwordStr, String passwordConfirmStr, String firstNameStr, String lastNameStr) {
        emailStr = emailStr.trim();
        passwordStr = passwordStr.trim();
        passwordConfirmStr = passwordConfirmStr.trim();
        firstNameStr = firstNameStr.trim();
        lastNameStr = lastNameStr.trim();

        String err;

        if (firstNameStr.isEmpty()) {
            err = getString(R.string.FIRST_NAME_ERR);
            updateUI(null, err);
        } else if (lastNameStr.isEmpty()) {
            err = getString(R.string.LAST_NAME_ERR);
            updateUI(null, err);
        } else if (emailStr.isEmpty()) {
            err = getString(R.string.EMAIL_ERR);
            updateUI(null, err);
        } else if (passwordStr.isEmpty()) {
            err = getString(R.string.PASSWORD_ERR);
            updateUI(null, err);
        } else if (passwordConfirmStr.isEmpty()) {
            err = getString(R.string.CONFIRM_PASSWORD_ERR);
            updateUI(null, err);
        } else if (!passwordStr.equals(passwordConfirmStr)) {
            err = getString(R.string.PASSWORD_MATCH_ERR);
            updateUI(null, err);
        } else {
            Log.d(getString(R.string.SUCCESSFUL_INPUT_MSG),
                    passwordConfirmStr + emailStr);
            CreateAccount(emailStr, passwordConfirmStr, firstNameStr, lastNameStr);
        }
    }

    /**
     * This account take the validated user info and makes an API call which then creates a user in
     * the database. If the creating the user is unsuccessful due to an error it passes the error to
     * updateUI to be handled.
     *
     * @param email     {@link String}, User's email. Cannot be null
     * @param password  {@link String}, User's password. Cannot be null
     * @param firstName {@link String}, User's first name. Cannot be null
     * @param lastName  {@link String}, User's last name. Cannot be null
     */
    private void CreateAccount(String email, String password, String firstName, String lastName) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Register success, update UI accordingly
                            Log.d(TAG, getString(R.string.USER_CREATION_SUCCESS_MSG));
                            FirebaseUser user = auth.getCurrentUser();
                            System.out.println(user.getUid());
                            createUserDoc(user, firstName, lastName);
                            updateUI(user, null);
                        } else {
                            // If Register fails, display a message to the user.
                            Log.w(TAG, getString(R.string.USER_CREATION_FAILURE_MSG),
                                    task.getException());
                            String err = task.getException().getLocalizedMessage();
                            updateUI(null, err);
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This function checks if the user is created and if the user is created then switches the
     * activity to Login, if the user is not created it displays an error message.
     *
     * @param user      {@link FirebaseUser}, the user object that is created in the database. If null
     *                  the user creation did not succeed
     * @param errOutput {@link String}, contains the error message if the user creation is
     *                  unsuccessful. If null, doesn't display a message.
     */
    public void updateUI(FirebaseUser user, String errOutput) {
        if (user == null) {
            Toast.makeText(RegisterActivity.this, errOutput,
                    Toast.LENGTH_SHORT).show();
        } else {
            auth.signOut();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    /**
     * This function makes an API call which creates a document in the users collection in the
     * database which stores the user information.
     *
     * @param user      {@link FirebaseUser} this is the user object that is created when registering the
     *                  user. If null the API call wont be executed.
     * @param firstName {@link String} this is the first name of the user, checked if it is null
     *                  before passed in.
     * @param lastName  {@link String} this is the last name of the user, checked if it is null
     *                  before passed in.
     */
    public void createUserDoc(FirebaseUser user, String firstName, String lastName) {
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("users");

        String userId = user.getUid();
        String emailID = user.getEmail();
        HashMap<String, Object> data = new HashMap<>();
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> following = new ArrayList<>();
        ArrayList<String> followRequestList = new ArrayList<>();

        data.put("UID", userId);
        data.put(getString(R.string.EMAIL_ID_STR), emailID);
        data.put(getString(R.string.FIRST_NAME_STR), firstName);
        data.put(getString(R.string.LAST_NAME_STR), lastName);
        data.put("followers", followers);
        data.put("following", following);
        data.put("follow request list", followRequestList);

        try {
            collectionReference
                    .document(userId)
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    })
                    .addOnFailureListener(e -> {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    });
        } catch (Exception e) {
            Toast.makeText(RegisterActivity.this, e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

