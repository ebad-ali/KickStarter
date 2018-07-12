package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 27/11/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileProjects {

    @SerializedName("profile")
    @Expose
    private List<Profile> profile = null;
    @SerializedName("backedProjects")
    @Expose
    private List<BackedProject> backedProjects = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileProjects() {
    }

    /**
     *
     * @param backedProjects
     * @param profile
     */
    public ProfileProjects(List<Profile> profile, List<BackedProject> backedProjects) {
        super();
        this.profile = profile;
        this.backedProjects = backedProjects;
    }

    public List<Profile> getProfile() {
        return profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public List<BackedProject> getBackedProjects() {
        return backedProjects;
    }

    public void setBackedProjects(List<BackedProject> backedProjects) {
        this.backedProjects = backedProjects;
    }

}