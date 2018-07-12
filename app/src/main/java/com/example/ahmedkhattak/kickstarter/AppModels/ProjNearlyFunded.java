package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProjNearlyFunded {

    @SerializedName("perc")
    @Expose
    private String perc;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("project_title")
    @Expose
    private String projectTitle;
    @SerializedName("fund_target")
    @Expose
    private String fundTarget;

    /**
     * No args constructor for use in serialization
     *
     */
    public ProjNearlyFunded() {
    }

    /**
     *
     * @param amount
     * @param perc
     * @param projectTitle
     * @param fundTarget
     */
    public ProjNearlyFunded(String perc, String amount, String projectTitle, String fundTarget) {
        super();
        this.perc = perc;
        this.amount = amount;
        this.projectTitle = projectTitle;
        this.fundTarget = fundTarget;
    }

    public String getPerc() {
        return perc;
    }

    public void setPerc(String perc) {
        this.perc = perc;
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

    public String getFundTarget() {
        return fundTarget;
    }

    public void setFundTarget(String fundTarget) {
        this.fundTarget = fundTarget;
    }

}