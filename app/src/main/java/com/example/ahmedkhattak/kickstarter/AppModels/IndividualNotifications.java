package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class IndividualNotifications {


    @SerializedName("data")
    @Expose
    private List<IndividualNotification> individual = null;
    @SerializedName("alreadyBacked")
    @Expose
    private boolean alreadyBackedProject;

    public IndividualNotifications() {
    }


    /**
     *
     * @param individual
     * @param alreadyBackedProject
     */
    public IndividualNotifications(List<IndividualNotification> individual, boolean alreadyBackedProject) {
        super();
        this.individual = individual;
        this.alreadyBackedProject = alreadyBackedProject;
    }

    public List<IndividualNotification> getIndividual() {
        return individual;
    }

    public boolean isAlreadyBackedProject() {
        return alreadyBackedProject;
    }
}
