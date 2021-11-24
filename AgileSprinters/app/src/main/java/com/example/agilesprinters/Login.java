package com.example.agilesprinters;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 *
 * This class represents a login activity, it is the first page for users who are not signed it
 * @author Leen Alzebdeh
 */
public class Login extends AppCompatActivity implements View.OnClickListener {

    /**
     * This variable contains an instance of FirebaseAuth
     */
    private FirebaseAuth auth;
    /**
     * this variable contains the edit text of the email field
     */
    private EditText emailEditText;
    /**
     * this variable contains the edit text of the password field
     */
    private EditText passwordEditText;
    /**
     * this variable contains the current user
     */
    private final User currentUser = new User();

    /**
     * This function is called when the login activity starts
     * @param savedInstanceState
     *   a reference to Bundle object that is passed into the onCreate method {@link Bundle } <br>
     *   if null is passed an exception is thrown
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView register;
        TextView resetPassword;
        Button loginBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // if the register text is clicked
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        // link the edit text vars to UI
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        // if the login button is clicked
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        resetPassword = findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if ( currentUser != null){
            //Do anything here which needs to be done after user is set is complete
            updateUI(currentUser);
        }
    }

    /**
     * This function handles sign in/ authentication when the user clicks the sign in button,
     * email and password fields must be non-empty
     */
    private void sendPassResetAlert() {

        AlertDialog.Builder resetDialog = new AlertDialog.Builder(Login.this);
        resetDialog.setTitle(getString(R.string.password_reset_request));

        final EditText emailInput = new EditText(Login.this);
        resetDialog.setView(emailInput);

        resetDialog.setPositiveButton(getString(R.string.send_email), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String resetEmail = emailInput.getText().toString();
                sendPasswordReset(resetEmail);
            }
        });
        resetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        resetDialog.show();
    }

    /**
     * This function sends an email to let the user update their password
     * @param emailAddress
     *  Give the email you want to send the email to <br>
     *      if null is passed, no email is sent {@link String}
     */
    private void sendPasswordReset(String emailAddress) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(Login.this, getString(R.string.email_sent),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /**
     * This function handles sign in/ authentication when the user clicks the sign in button,
     * email and password fields must be non-empty
     */
    private void signIn() {

        // get the email and password from respective fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //throw a message if the email is empty
        if (email.equals("")){
            Toast.makeText(Login.this, getString(R.string.empty_email),
                    Toast.LENGTH_SHORT).show();
        } //throw a message if the password is empty
        else if (password.equals("")){
            Toast.makeText(Login.this, getString(R.string.empty_password),
                    Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    updateUI(user);
                                }
                            } else {
                                // If sign in fails due to a wrong password or email
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login.this, getString(R.string.login_failed),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * This function directs the user to the home page
     * @param user
     * Give the firebase user that is logged in, if null is passed {@link FirebaseUser}
     */
    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(Login.this, Home.class);

        //pass in the unique user ID to home page
        String uId = user.getUid();
        currentUser.setUser(uId);
        intent.putExtra("user", currentUser);

        //go to home page and finish the login activity
        startActivity(intent);
        finish();
    }

    /**
     * This function handles different cases of view clicks
     * @param v
     *  Give the view that is clicked <br> if null is passed nothing happens {@link View}
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.register:  //if the register text is clicked, direct to the register page
                intent = new Intent(Login.this, Register.class);
                break;
            case R.id.loginBtn:  //if the login button is clicked, attempt to sign in
                signIn();
                break;
            case R.id.resetPassword:
                sendPassResetAlert();
                break;
            default:
                break;
        }
        if (null!=intent) startActivity(intent);
    }

}