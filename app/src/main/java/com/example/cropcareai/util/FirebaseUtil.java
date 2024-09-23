package com.example.cropcareai.util;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserDetails() != null)
        {
            return true;
        }
        return false;
    }
    public static DocumentReference currentUserDetails()
    {
        // i have made changes here
        String userId = currentUserId();
        if (userId != null) {
            return FirebaseFirestore.getInstance().collection("users").document(userId);
        } else {
            return null;
        }
    }
    public static CollectionReference allUserCollectionReferences(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static String timestamptostring(Timestamp timestamp)
    {
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }

}
