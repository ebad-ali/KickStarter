package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjWeLove {

    @SerializedName("project_description")
    @Expose
    private String projectDescription;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("project_location")
    @Expose
    private String projectLocation;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("project_date")
    @Expose
    private String projectDate;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("fund_target")
    @Expose
    private String fundTarget;
    @SerializedName("avg(project_rating.rating)")
    @Expose
    private String avgProjectRatingRating;
    @SerializedName("amount")
    @Expose
    private String amount;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProjWeLove() {
    }

    /**
     *
     * @param amount
     * @param projectLocation
     * @param company
     * @param userId
     * @param categoryId
     * @param avgProjectRatingRating
     * @param projectDescription
     * @param image
     * @param projectId
     * @param projectTitle
     * @param fundTarget
     * @param projectDate
     */
    public ProjWeLove(String projectDescription, String categoryId, String userId, String projectLocation, String company, String image, String projectDate, String projectId, String projectTitle, String fundTarget, String avgProjectRatingRating, String amount) {
        super();
        this.projectDescription = projectDescription;
        this.categoryId = categoryId;
        this.userId = userId;
        this.projectLocation = projectLocation;
        this.company = company;
        this.image = image;
        this.projectDate = projectDate;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.fundTarget = fundTarget;
        this.avgProjectRatingRating = avgProjectRatingRating;
        this.amount = amount;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getFundTarget() {
        return fundTarget;
    }

    public void setFundTarget(String fundTarget) {
        this.fundTarget = fundTarget;
    }

    public String getAvgProjectRatingRating() {
        return avgProjectRatingRating;
    }

    public void setAvgProjectRatingRating(String avgProjectRatingRating) {
        this.avgProjectRatingRating = avgProjectRatingRating;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}