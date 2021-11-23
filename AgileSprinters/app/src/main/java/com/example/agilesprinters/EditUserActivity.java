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
    private ImageView imageContainer;
    private ImageView addCamPhotoBtn;
    private ImageView addGalPhotoBtn;
    private Uri selectedImg;
    private Bitmap bitmapOfImg;

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

        Button signOutButton = findViewById(R.id.signOutbutton);
        Button deleteUserButton = findViewById(R.id.deleteUserButton);
        imageContainer = findViewById(R.id.userImageView);
        addCamPhotoBtn = findViewById(R.id.add_Cam_Photo);
        addGalPhotoBtn = findViewById(R.id.add_Gal_Photo);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(EditUserActivity.this, Login.class);
                user = null;
                startActivity(intent);
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
                finish();
            }
        });

        addCamPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(runtimePermissionForCamera()){
                getCameraPicture();
                //}
            }
        });

        addGalPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGalleryPicture();
                //hides alert dialog after gallery func is finished
            }
        });
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
                        }
                        else {
                            Log.d(TAG, getString(R.string.USER_NOT_DEL_LOG));
                        }
                    }
                });
    }

    /**
     * This method deletes all data (email, password etc) associated with a user.
     */
    private void deleteUserData(){
        CollectionReference collectionReference = db.collection("users");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getData().get(getString(R.string.UID))));
                    if (UID.matches((String) doc.getData().get(getString(R.string.UID)))) {
                        if(doc.getId() == null){
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
                        if(doc.getId() == null){
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

    //switches view to gallery and allows user to pick photo
    private void getGalleryPicture() {
        //allow user to pick a photo from gallery
        Intent pickFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickFromGallery, 1);

    }

    private void getCameraPicture(){
        //have to give permission to app to use camera
        //android manifest give permission and then take permission at runtime from user
        //switch view to camera view
        Intent cameraView = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraView, 2);
    }


    //overrides the method when activity is returning data (prev intent on line 82)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){

            case 1:
                if(resultCode == -1){
                    //URI is string of characters used to identify a resource (either by location name or both)
                    //use android net uri
                    selectedImg =  data.getData();
                    try {
                        bitmapOfImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImg);
                    } catch (IOException e) {
                        System.out.println(e+"I error");
                    }
                    //set the original placeholder img to be the selected img
                    imageContainer.setImageBitmap(bitmapOfImg);
                }
                break;

            case 2:
                Log.d("CAMERA", "case 2 for camera return result ");
                if(resultCode == -1 && data != null){
                    //retrieve data sent back from activity thru bundle
                    Bundle bundle = data.getExtras();

                    //bitmap of the image matrix of dots (each dot corresponds to pixel)
                    //grabs img data from extra
                    bitmapOfImg = (Bitmap) bundle.get("data");

                    //set placeholder to bitmap of the img taken by camera
                    imageContainer.setImageBitmap(bitmapOfImg);

                }
                break;
        }
    }

}