package com.example.ahmedkhattak.kickstarter.AppModels.SearchByFunders;

/**
 * Created by Ahmed Khattak on 21/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchResult() {
    }

    /**
     *
     * @param amount
     * @param userId
     * @param name
     * @param image
     * @param projectId
     * @param projectTitle
     */
    public SearchResult(String image, String name, String userId, String projectId, String amount, String projectTitle) {
        super();
        this.image = image;
        this.name = name;
        this.userId = userId;
        this.projectId = projectId;
        this.amount = amount;
        this.projectTitle = projectTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

}