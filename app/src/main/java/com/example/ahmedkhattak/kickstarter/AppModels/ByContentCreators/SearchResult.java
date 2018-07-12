package com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators;

/**
 * Created by Ahmed Khattak on 21/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("project_description")
    @Expose
    private String projectDescription;

    /**
     * No args constructor for use in serialization
     *
     */
    public SearchResult() {
    }

    /**
     *
     * @param userId
     * @param name
     * @param projectDescription
     * @param image
     * @param projectId
     * @param projectTitle
     */
    public SearchResult(String name, String userId, String image, String projectId, String projectTitle, String projectDescription) {
        super();
        this.name = name;
        this.userId = userId;
        this.image = image;
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

}