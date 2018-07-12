package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class Notifications {

    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;

    public Notifications() {
    }


    /**
     * @param notifications
     */
    public Notifications(List<Notification> notifications) {
        super();
        this.notifications = notifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}

