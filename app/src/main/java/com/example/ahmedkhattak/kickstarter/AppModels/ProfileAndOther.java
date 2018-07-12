package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 12/12/2017.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileAndOther {

    @SerializedName("profile")
    @Expose
    private List<Profile> profile = null;
    @SerializedName("backedProjects")
    @Expose
    private List<BackedProject> backedProjects = null;
    @SerializedName("myProjects")
    @Expose
    private List<MyProject> myProjects = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProfileAndOther() {
    }

    /**
     *
     * @param backedProjects
     * @param myProjects
     * @param profile
     */
    public ProfileAndOther(List<Profile> profile, List<BackedProject> backedProjects, List<MyProject> myProjects) {
        super();
        this.profile = profile;
        this.backedProjects = backedProjects;
        this.myProjects = myProjects;
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

    public List<MyProject> getMyProjects() {
        return myProjects;
    }

    public void setMyProjects(List<MyProject> myProjects) {
        this.myProjects = myProjects;
    }

}