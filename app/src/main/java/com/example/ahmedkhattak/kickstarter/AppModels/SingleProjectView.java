package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleProjectView {

    @SerializedName("project_data")
    @Expose
    private List<ProjectDatum> projectData = null;
    @SerializedName("project_backers_data")
    @Expose
    private List<ProjectBackersDatum> projectBackersData = null;
    @SerializedName("project_rating")
    @Expose
    private List<ProjectRating> projectRating = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public SingleProjectView() {
    }

    /**
     *
     * @param projectBackersData
     * @param projectRating
     * @param projectData
     */
    public SingleProjectView(List<ProjectDatum> projectData, List<ProjectBackersDatum> projectBackersData, List<ProjectRating> projectRating) {
        super();
        this.projectData = projectData;
        this.projectBackersData = projectBackersData;
        this.projectRating = projectRating;
    }

    public List<ProjectDatum> getProjectData() {
        return projectData;
    }

    public void setProjectData(List<ProjectDatum> projectData) {
        this.projectData = projectData;
    }

    public List<ProjectBackersDatum> getProjectBackersData() {
        return projectBackersData;
    }

    public void setProjectBackersData(List<ProjectBackersDatum> projectBackersData) {
        this.projectBackersData = projectBackersData;
    }

    public List<ProjectRating> getProjectRating() {
        return projectRating;
    }

    public void setProjectRating(List<ProjectRating> projectRating) {
        this.projectRating = projectRating;
    }

}