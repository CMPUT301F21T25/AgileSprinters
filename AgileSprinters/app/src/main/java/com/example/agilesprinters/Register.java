package com.example.agilesprinters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class Register extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        EditText password = findViewById(R.id.TextPassword);
        EditText confirmPassword = findViewById(R.id.TextConfirmPassword);
        EditText email = findViewById(R.id.EditTextEmail);
        Button createAccountBtn = findViewById(R.id.createAccBtn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailStr = email.getText().toString();
                final String passwordStr = password.getText().toString();
                final String passwordConfirmStr = confirmPassword.getText().toString();
                Log.d("j", passwordConfirmStr+passwordStr);
                if(emailStr.isEmpty()){
                    String err = "email can not be empty";
                    updateUI(null, err);
                } else if(!passwordStr.equals(passwordConfirmStr)){
                    String err = "passwords do not match";
                    updateUI(null, err);
                } else if(passwordStr.isEmpty() || passwordConfirmStr.isEmpty()){
                    String err = "passwords can not be empty";
                    updateUI(null, err);
                } else if (passwordStr.equals(passwordConfirmStr)){
                    Log.d("j", passwordConfirmStr+emailStr);
                    CreateAccount(emailStr, passwordConfirmStr);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
    }


    private void CreateAccount(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user, "success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String err = task.getException().getLocalizedMessage();
                            updateUI(null, err);
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser user, String errOutput){
        if (user == null) {
            Toast.makeText(Register.this, errOutput,
                    Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Register.this, Login.class);
            if (null!=intent) startActivity(intent);
        }
    }
}