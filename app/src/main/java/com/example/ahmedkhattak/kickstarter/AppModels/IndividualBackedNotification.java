package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class IndividualBackedNotification {

    @SerializedName("alreadyBacked")
    @Expose
    private boolean alreadyBacked;


    public IndividualBackedNotification() {
    }


    /**
     * @param alreadyBacked
     */

    public IndividualBackedNotification(boolean alreadyBacked) {
        super();
        this.alreadyBacked = alreadyBacked;

    }

}
