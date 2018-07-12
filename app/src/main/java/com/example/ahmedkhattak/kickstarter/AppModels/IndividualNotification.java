package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class IndividualNotification {

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("t_pin")
    @Expose
    private String transactionPin;

    @SerializedName("fund_target")
    @Expose
    private String fundTargetamount;

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


    public IndividualNotification() {
    }

    /**
     * @param image
     * @param transactionPin
     * @param fundTargetamount
     * @param authorID
     * @param backerID
     * @param projectID
     * @param amount
     * @param projectTitle
     * @param name
     * @param tempBackID
     */


    public IndividualNotification(String image, String transactionPin, String fundTargetamount, String authorID, String backerID, String projectID, String amount, String projectTitle, String name, String tempBackID) {
        super();
        this.image = image;
        this.transactionPin = transactionPin;
        this.fundTargetamount = fundTargetamount;
        this.authorID = authorID;
        this.backerID = backerID;
        this.projectID = projectID;
        this.amount = amount;
        this.projectTitle = projectTitle;
        this.name = name;
        this.tempBackID = tempBackID;
    }


    public String getImage() {
        return image;
    }

    public String getTransactionPin() {
        return transactionPin;
    }

    public String getFundTargetamount() {
        return fundTargetamount;
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
