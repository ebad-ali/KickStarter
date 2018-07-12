package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class Notification {

    @SerializedName("status_msg")
    @Expose
    private String statusMsg;
    @SerializedName("author_id")
    @Expose
    private String authorID;
    @SerializedName("backer_id")
    @Expose
    private String backerID;
    @SerializedName("project_id")
    @Expose
    private String projectID;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("temp_back_id")
    @Expose
    private String tempBackID;


    public Notification() {
    }

    /**
     *
     * @param statusMsg
     * @param authorID
     * @param backerID
     * @param projectID
     * @param amount
     * @param projectTitle
     * @param name
     * @param tempBackID
     */


    public Notification(String statusMsg, String authorID, String backerID, String projectID, String amount, String projectTitle, String name, String tempBackID) {
        super();
        this.statusMsg = statusMsg;
        this.authorID = authorID;
        this.backerID = backerID;
        this.projectID = projectID;
        this.amount = amount;
        this.projectTitle = projectTitle;
        this.name = name;
        this.tempBackID = tempBackID;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getBackerID() {
        return backerID;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getAmount() {
        return amount;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getName() {
        return name;
    }

    public String getTempBackID() {
        return tempBackID;
    }
}

