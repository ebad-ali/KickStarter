package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("perc")
    @Expose
    private String perc;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("project_description")
    @Expose
    private String projectDescription;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("project_location")
    @Expose
    private String projectLocation;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("fund_target")
    @Expose
    private String fundTarget;
    @SerializedName("image")
    @Expose
    private String image;

    /**
     * No args constructor for use in serialization
     *
     */
    public Datum() {
    }

    /**
     *
     * @param amount
     * @param projectLocation
     * @param company
     * @param userId
     * @param projectDescription
     * @param image
     * @param projectId
     * @param perc
     * @param projectTitle
     * @param fundTarget
     */
    public Datum(String perc, String amount, String projectTitle, String projectDescription, String projectId, String userId, String projectLocation, String company, String fundTarget, String image) {
        super();
        this.perc = perc;
        this.amount = amount;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.projectId = projectId;
        this.userId = userId;
        this.projectLocation = projectLocation;
        this.company = company;
        this.fundTarget = fundTarget;
        this.image = image;
    }

    public String getPerc() {
        return perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFundTarget() {
        return fundTarget;
    }

    public void setFundTarget(String fundTarget) {
        this.fundTarget = fundTarget;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}