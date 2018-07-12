package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectRating {

    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("rating")
    @Expose
    private String rating;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProjectRating() {
    }

    /**
     *
     * @param rating
     * @param projectId
     */
    public ProjectRating(String projectId, String rating) {
        super();
        this.projectId = projectId;
        this.rating = rating;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}