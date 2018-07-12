package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Explore {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("proj_we_love")
    @Expose
    private List<ProjWeLove> projWeLove = null;
    @SerializedName("proj_nearly_funded")
    @Expose
    private List<ProjNearlyFunded> projNearlyFunded = null;
    @SerializedName("proj_just_launched")
    @Expose
    private List<ProjJustLaunched> projJustLaunched = null;
    @SerializedName("proj_everything")
    @Expose
    private List<ProjEverything> projEverything = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Explore() {
    }

    /**
     *
     * @param projWeLove
     * @param projEverything
     * @param projNearlyFunded
     * @param categories
     * @param projJustLaunched
     */
    public Explore(List<Category> categories, List<ProjWeLove> projWeLove, List<ProjNearlyFunded> projNearlyFunded, List<ProjJustLaunched> projJustLaunched, List<ProjEverything> projEverything) {
        super();
        this.categories = categories;
        this.projWeLove = projWeLove;
        this.projNearlyFunded = projNearlyFunded;
        this.projJustLaunched = projJustLaunched;
        this.projEverything = projEverything;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<ProjWeLove> getProjWeLove() {
        return projWeLove;
    }

    public void setProjWeLove(List<ProjWeLove> projWeLove) {
        this.projWeLove = projWeLove;
    }

    public List<ProjNearlyFunded> getProjNearlyFunded() {
        return projNearlyFunded;
    }

    public void setProjNearlyFunded(List<ProjNearlyFunded> projNearlyFunded) {
        this.projNearlyFunded = projNearlyFunded;
    }

    public List<ProjJustLaunched> getProjJustLaunched() {
        return projJustLaunched;
    }

    public void setProjJustLaunched(List<ProjJustLaunched> projJustLaunched) {
        this.projJustLaunched = projJustLaunched;
    }

    public List<ProjEverything> getProjEverything() {
        return projEverything;
    }

    public void setProjEverything(List<ProjEverything> projEverything) {
        this.projEverything = projEverything;
    }

}