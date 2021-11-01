package com.example.agilesprinters;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User {
    private FirebaseUser user;
    FirebaseAuth auth;
    public FirebaseUser getUser(){
        try {
            FirebaseUser currentUser = auth.getCurrentUser();
            return user;
        } catch (Exception e){
            Log.d("onStart", e.toString());
            return user = null;
        }
    }
}
