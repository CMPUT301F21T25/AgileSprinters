package com.example.agilesprinters;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * This helper class that generalizes the database calls for add, update and delete which can be
 * accessed by other classes
 *
 * @author Hari
 */
public class Database {
    FirebaseFirestore db;

    /**
     * This is a generalized function that adds a new doc to the database according to the given
     * data
     *
     * @param collectionPath {@link String} This is a string that contains the name of the
     *                       collection in the database
     * @param objectId       {@link String} This is the id that is used when saving to the collection
     * @param data           {@link HashMap} This is the document that is being added to the database
     * @param TAG            {@link String} This is the TAG that is used for logging the success/failure message
     */
    public void addData(String collectionPath, String objectId, HashMap data, String TAG) {
        try {
            db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection(collectionPath);
            collectionReference
                    .document(objectId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, "Data has been added successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be added!" + e.toString());
                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This function updates a document in a given collection
     *
     * @param collectionPath {@link String} This is a string that contains the name of the
     *                       collection in the database
     * @param objectId       {@link String} This is the id that is used when updating the collection
     * @param data           {@link HashMap} This is the document that is being added to the database
     * @param TAG            {@link String} This is the TAG that is used for logging the success/failure message
     */
    public void updateData(String collectionPath, String objectId, HashMap data, String TAG) {
        try {
            db = FirebaseFirestore.getInstance();
            final CollectionReference collectionReference = db.collection(collectionPath);

            collectionReference
                    .document(objectId)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // These are a method which gets executed when the task is succeeded
                            Log.d(TAG, "Data has been updated successfully!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // These are a method which gets executed if there’s any problem
                            Log.d(TAG, "Data could not be updated!" + e.toString());
                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This function deletes a document from a given collection
     *
     * @param collectionPath {@link String} This is a string that contains the name of the
     *                       collection in the database
     * @param objectId       {@link String} This is the id that is used when d deleting from the collection
     * @param TAG            {@link String} This is the TAG that is used for logging the success/failure message
     */
    public void deleteData(String collectionPath, String objectId, String TAG) {
        try {
            db = FirebaseFirestore.getInstance();
            db.collection(collectionPath)
                    .document(objectId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This function adds an image to the firebase storage using the given path.
     *
     * @param path   {@link String} this is string which identifies the image.
     * @param bitmap {@link Bitmap} this is a Bitmap of the image being saved.
     */
    public void addImage(String path, Bitmap bitmap) {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child(path);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] bitmapData = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(bitmapData);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("failure in image");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("success in image");
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * This function deletes an image from the database.
     *
     * @param IID {@link String} This is Id that is used to identify the image.
     */
    public void deleteImg(String IID) {
        try {
            // Create a storage reference from our app
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            // Create a reference to the file to delete
            StorageReference desertRef = storageRef.child(IID);

            System.out.println("IID DEL" + desertRef);
            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    System.out.printf("SUCCESSSSS IMAGE IS GONE!!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    System.out.printf("Failure to delete");
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

