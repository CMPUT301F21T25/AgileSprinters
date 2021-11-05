package com.example.agilesprinters;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private FirebaseAuth auth;
    private static final String TAG = "EmailPassword";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        EditText password = findViewById(R.id.TextPassword);
        EditText confirmPassword = findViewById(R.id.TextConfirmPassword);
        EditText email = findViewById(R.id.EditTextEmail);
        EditText FirstName = findViewById(R.id.FirstName);
        EditText LastName = findViewById(R.id.LastName);
        Button createAccountBtn = findViewById(R.id.createAccBtn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailStr = email.getText().toString();
                final String passwordStr = password.getText().toString();
                final String passwordConfirmStr = confirmPassword.getText().toString();
                final String firstNameStr = FirstName.getText().toString();
                final String lastNameStr = LastName.getText().toString();

                emailStr.trim();
                passwordStr.trim();
                passwordConfirmStr.trim();
                firstNameStr.trim();
                lastNameStr.trim();

                if(firstNameStr.isEmpty()){
                    String err = "First name can not be empty";
                    updateUI(null, err);
                } else if(lastNameStr.isEmpty()) {
                    String err = "Last name can not be empty";
                    updateUI(null, err);
                } else if(emailStr.isEmpty()) {
                    String err = "email can not be empty";
                    updateUI(null, err);
                } else if(passwordStr.isEmpty()){
                    String err = "password can not be empty";
                    updateUI(null, err);
                } else if(passwordConfirmStr.isEmpty()){
                    String err = "confirm password can not be empty";
                    updateUI(null, err);
                } else if(!passwordStr.equals(passwordConfirmStr)) {
                    String err = "passwords do not match";
                    updateUI(null, err);
                } else if (passwordStr.equals(passwordConfirmStr)){
                    Log.d("j", passwordConfirmStr+emailStr);
                    CreateAccount(emailStr, passwordConfirmStr, firstNameStr, lastNameStr);
                }
            }
        });
    }


    private void CreateAccount(String email, String password, String firstName, String lastName){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();

                            System.out.println(user.getUid().toString());
                            createUserDoc(user, firstName, lastName);
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
            auth.signOut();
            Intent intent = new Intent(Register.this, Login.class);
            if (null!=intent) startActivity(intent);
        }
    }

    public void createUserDoc(FirebaseUser user, String firstName, String lastName){
        db  =  FirebaseFirestore.getInstance();
        final CollectionReference collectionReference  =  db.collection("users");

        String Uid = user.getUid();
        String emailID = user.getEmail();
        HashMap<String, String> data = new HashMap<>();

        if (Uid != null){
            data.put("Email ID", emailID);
            data.put("First Name", firstName);
            data.put("Last Name", lastName);
            collectionReference
                    .document(Uid)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log. d (TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log. d (TAG, "Data could not be added!" + e.toString());
                        }
                    });
        }
    }
}

