package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjectBackersDatum {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("backers")
    @Expose
    private String backers;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProjectBackersDatum() {
    }

    /**
     *
     * @param amount
     * @param backers
     */
    public ProjectBackersDatum(String amount, String backers) {
        super();
        this.amount = amount;
        this.backers = backers;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBackers() {
        return backers;
    }

    public void setBackers(String backers) {
        this.backers = backers;
    }

}