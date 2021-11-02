package com.example.agilesprinters;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 *
 * This class represents a login activity, it is the first page for users who are not signed it
 * @author Leen Alzebdeh
 * ccid alzebdeh
 * lab section
 */
public class Login extends AppCompatActivity implements View.OnClickListener {

    /**
     * This variable contains
     */

    private FirebaseAuth auth;
    /**
     * this variable contains
     */
    private EditText emailEditText;
    /**
     * this variable contains
     */
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView register;
        TextView resetPassword;
        Button loginBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        resetPassword = findViewById(R.id.resetPassword);

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
     * This function handles sign in when the user clicks the sign in button
     */
    private void signIn() {

        // get the email and password from respective fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            if (user !=  null) {updateUI(user);}
                        } else {
                            //exception: email field is empty
                            if (email.equals("")){
                                errMsg(task,"Email field is empty");
                            } //exception: password field is empty
                            else if (password.equals("")){
                               errMsg(task,"Password field is empty");
                            } // If sign in fails due to a wrong password or email
                            else {
                                errMsg(task,"Email or password entered is incorrect");
                            }
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    /**
     * This function displays to the user an error message when sign in fails
     * @param task
     * an API to represent if the sign in succeeded or the exception thrown {@link Task<AuthResult>}
     * @param errStr
     * Give the string you want displayed to the user {@link String}
     */
    private void errMsg(@NonNull Task<AuthResult> task, String errStr){
        Log.w(TAG, "signInWithEmail:failure", task.getException());
        Toast.makeText(Login.this, errStr,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * This function directs the user to the home page
     * @param user
     * Give the firebase user that is logged in {@link FirebaseUser}
     */
    private void updateUI(FirebaseUser user) {

        Intent intent = new Intent(Login.this, Home.class);

        Bundle bundle = new Bundle();
        //pass in the unique user ID to home page
        String UId = user.getUid();
        bundle.putString("userId", UId);
        intent.putExtras(bundle);
        //go to home page and finish the login activity
        startActivity(intent);
        finish();
    }


    /**
     *
     * should I??
     *
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){
            case R.id.register:
                intent = new Intent(Login.this, Register.class);
                break;
            case R.id.loginBtn:
                signIn();
                break;
            case R.id.resetPassword:
                //resetPassword();
                break;
            default:
                break;
        }
        if (null!=intent) startActivity(intent);
    }

}