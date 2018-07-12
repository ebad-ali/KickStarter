package com.example.ahmedkhattak.kickstarter;

import android.net.Uri;

/**
 * Created by Ahmed Khattak on 27/11/2017.
 */

public class UserAppStateManager {
    private static final UserAppStateManager ourInstance = new UserAppStateManager();

    public static UserAppStateManager getInstance() {
        return ourInstance;
    }

    private UserAppStateManager() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private int userID;

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri imageUri = null;


}
