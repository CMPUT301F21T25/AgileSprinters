package com.example.agilesprinters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

/**
 * This class is a fragment allows a user to view all the details of a habit and edit any details
 * they wish to change.
 *
 * @author Sai Rasazna Ajerla and Riyaben Patel
 */
public class editHabitEventFragment extends DialogFragment {
    private EditText optional_comment;
    private TextView input_date;
    private EditText input_duration;
    private String EID;
    private String UID;
    private String HID;
    private String IID;
    private ImageView imageContainer;
    private ImageView addCamPhotoBtn;
    private ImageView addGalPhotoBtn;
    private Uri selectedImg;
    private Bitmap bitmapOfImg;

    private editHabitEventFragment.OnFragmentInteractionListener listener;

    /**
     * This function saves the values sent to the fragment for future manipulation
     *
     * @param habitInstance is the item that was tapped within the list
     * @param position      is the position of the tapped item within the list
     * @return returns the fragment with the bundled parameters
     */
    public static editHabitEventFragment newInstance(int position, HabitInstance habitInstance) {
        editHabitEventFragment fragment = new editHabitEventFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("Habit instance", habitInstance);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * This interface listens for when dialog is ended and sends the information and the function
     * to the user calendar class for it to implement.
     */
    public interface OnFragmentInteractionListener {
        void onEditSavePressed(HabitInstance instance, Bitmap bitmapOfImg);

        void onDeletePressed(HabitInstance instance);
    }

    /**
     * This function attaches the fragment to the activity and keeps track of the context of the
     * fragment so the listener knows what to listen to. Ensures that the proper methods are
     * implemented by the Home class.
     *
     * @param context context of the current fragment
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof addHabitEventFragment.OnFragmentInteractionListener) {
            listener = (editHabitEventFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This function creates the actual dialog on the screen and listens for user input, returning
     * the information through the listener based on which button is clicked.
     *
     * @param savedInstanceState is the reference to the most recent object
     * @return the dialog of the fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit_event_fragment, null);

        // Display the calendar
        optional_comment = view.findViewById(R.id.editText_comment);
        input_date = view.findViewById(R.id.editText_date);
        input_duration = view.findViewById(R.id.editText_duration);

        HabitInstance habitInstance = (HabitInstance) getArguments().getSerializable("Habit instance");

        optional_comment.setText(habitInstance.getOpt_comment());
        input_date.setText(habitInstance.getDate());
        input_duration.setText(String.valueOf(habitInstance.getDuration()));
        imageContainer = view.findViewById(R.id.imageContainer);
        addCamPhotoBtn = view.findViewById(R.id.add_Cam_Photo);
        addGalPhotoBtn = view.findViewById(R.id.add_Gal_Photo);

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

        EID = habitInstance.getEID();
        UID = habitInstance.getUID();
        HID = habitInstance.getHID();
        IID = habitInstance.getIID();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("View/Edit Habit Event")
                .setNegativeButton("Delete", (dialog, id) -> listener.onDeletePressed(habitInstance))
                .setPositiveButton("Save", (dialogInterface, i) -> {
                    /* Do not implement anything here in order to override the button
                     * to only call the listener once all the information required has been
                     * filled out and display error messages if they have been left blank.
                     */
                }).create();

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
                        bitmapOfImg = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImg);
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

    /**
     * This function overrides the buttons clicked in order to only allow the dialog to be dismissed
     * when all requirements have been met.
     */
    @Override
    public void onResume() {
        super.onResume();

        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positive = dialog.getButton(Dialog.BUTTON_POSITIVE);

            positive.setOnClickListener(view -> {
                // Boolean tracks when the all the fields have been filled out. Will turn to false
                // if anything has been left blank.
                boolean readyToClose = true;

                String comment = optional_comment.getText().toString();
                String date = input_date.getText().toString();
                String duration = input_duration.getText().toString();

                if (optional_comment.length() > 20) {
                    readyToClose = false;
                    optional_comment.setError("This field cannot have more than 20 chars");
                }

                if (date.matches("")) {
                    readyToClose = false;
                    input_date.setError("This field cannot be blank");
                }

                if (duration.matches("")) {
                    readyToClose = false;
                    input_duration.setError("This field cannot be blank");
                }

                // If everything has been filled out, call the listener and send the edited
                // habit back to the Home class and dismiss the dialog.
                if (readyToClose) {
                    listener.onEditSavePressed(new HabitInstance(EID, UID, HID, comment, date, Integer.parseInt(duration), IID), bitmapOfImg);
                    dialog.dismiss();
                }
            });
        }
    }
}
