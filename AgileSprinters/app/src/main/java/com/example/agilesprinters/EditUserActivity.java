package com.example.agilesprinters;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class EditUserActivity extends AppCompatActivity {
    private User user;
    private String UID;
    private String nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        if (user == null) {
            user = (User) getIntent().getSerializableExtra("user");
            UID = user.getUser();
            nameStr = user.getFirstName()+ " " + user.getLastName();
        }

        TextView nameTextView = findViewById(R.id.userNameTextView);
        nameTextView.setText(nameStr);
    }

}