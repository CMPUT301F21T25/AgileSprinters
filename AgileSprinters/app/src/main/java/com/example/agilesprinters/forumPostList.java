package com.example.agilesprinters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * This class provides a custom layout for items in the forum list on the forum page.
 *
 * @author Sai Rasazna Ajerla
 */
public class forumPostList extends ArrayAdapter<Forum> {
    private final ArrayList<Forum> forumPost;
    private final Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * This function initializes the array list of forum events and the context of the screen.
     *
     * @param context The current screen.
     * @param forumPost The arraylist of object we are using our custom adapter on.
     */
    public forumPostList(Context context, ArrayList<Forum> forumPost) {
        super(context, 0, forumPost);
        this.forumPost = forumPost;
        this.context = context;
    }

    /**
     * This method converts the view for the list into a custom view.
     *
     * @param position    Position of the habit.
     * @param convertView The converted view.
     * @param parent      The parent of the view we are changing.
     * @return Returns the view after it's been converted and it's values set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.forum_list_content, parent, false);
        }

        Forum forumItem = forumPost.get(position);

        // attach and pass variables to the textview in the list
        Button user_circle = convertView.findViewById(R.id.forum_user);
        TextView userFullNameTextView = convertView.findViewById(R.id.forum_name_text_view);
        TextView event_date = convertView.findViewById(R.id.event_date_text_view);
        TextView opt_cmt = convertView.findViewById(R.id.forum_comment);
        TextView location = convertView.findViewById(R.id.forum_location);
        ImageView image = convertView.findViewById(R.id.forum_image_container);

        // Setting the user profile button
        user_circle.setText(forumItem.getFirstName().substring(0,1));
        String temp = forumItem.getFirstName()+" "+ forumItem.getLastName().substring(0,1) + ".";
        userFullNameTextView.setText(temp);

        // Setting the date of the event
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate eventDate = LocalDate.parse(forumItem.getEventDate(), formatter);
        String formattedDate = eventDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));

        event_date.setText(formattedDate);

        // Check if optional comment is empty or not and pass the content to TextView accordingly
        if (!forumItem.getComment().matches("")) {
            opt_cmt.setText(forumItem.getComment());
        } else {
            String day = eventDate.getDayOfWeek().name().toLowerCase(Locale.ROOT);
            opt_cmt.setText(day.substring(0,1).toUpperCase(Locale.ROOT) + day.substring(1) + " activity");
        }

        // If present, setting the image to the forum event in UI
        setImageToDialog(forumItem.getImage(), image);

        // If present, setting the location to the forum event in UI
        if (!forumItem.getLocation().matches("")) {
            location.setVisibility(View.VISIBLE);
            location.setText(forumItem.getLocation());
        } else {
            location.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    /**
     * This function receives the img path and the image container elem of the forum where the image
     * is to be set
     * It retrieves the img from firebase storage as a byte array using the img path
     * Then converts byte array to bitmap which is used to set the imageview
     * @param iid                 is the image id of the event
     * @param imageContainer      is the container to set the image to
     */
    private void setImageToDialog(String iid, ImageView imageContainer) {
        if (iid != null){
        imageContainer.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            StorageReference islandRef = storageRef.child(iid);

            final long ONE_MEGABYTE = 2*(1024 * 1024);
            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    //convert bytes to bitmap
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);

                    //set image to bitmap data
                    imageContainer.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }
}
