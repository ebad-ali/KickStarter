package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjEverything {

    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("project_description")
    @Expose
    private String projectDescription;
    @SerializedName("project_location")
    @Expose
    private String projectLocation;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("fund_target")
    @Expose
    private String fundTarget;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("project_date")
    @Expose
    private String projectDate;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProjEverything() {
    }

    /**
     *
     * @param projectLocation
     * @param company
     * @param userId
     * @param categoryId
     * @param projectDescription
     * @param image
     * @param projectId
     * @param projectTitle
     * @param video
     * @param projectDate
     * @param fundTarget
     */
    public ProjEverything(String projectId, String categoryId, String userId, String projectTitle, String projectDescription, String projectLocation, String company, String fundTarget, String video, String image, String projectDate) {
        super();
        this.projectId = projectId;
        this.categoryId = categoryId;
        this.userId = userId;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.projectLocation = projectLocation;
        this.company = company;
        this.fundTarget = fundTarget;
        this.video = video;
        this.image = image;
        this.projectDate = projectDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(String projectDate) {
        this.projectDate = projectDate;
    }

}