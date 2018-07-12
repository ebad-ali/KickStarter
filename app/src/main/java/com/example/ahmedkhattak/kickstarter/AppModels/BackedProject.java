package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 27/11/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BackedProject {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("project_id")
    @Expose
    private String projectId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("project_description")
    @Expose
    private String projectDescription;
    @SerializedName("category_name")
    @Expose
    private String categoryName;

    /**
     * No args constructor for use in serialization
     *
     */
    public BackedProject() {
    }

    /**
     *
     * @param amount
     * @param categoryName
     * @param projectDescription
     * @param image
     * @param projectId
     * @param projectTitle
     */
    public BackedProject(String image, String projectId, String amount, String projectTitle, String projectDescription, String categoryName) {
        super();
        this.image = image;
        this.projectId = projectId;
        this.amount = amount;
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}