package com.example.ahmedkhattak.kickstarter.AppModels;

/**
 * Created by Ahmed Khattak on 03/12/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndividualCategory {

    @SerializedName("ind_category")
    @Expose
    private List<IndCategory> indCategory = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public IndividualCategory() {
    }

    /**
     *
     * @param indCategory
     */
    public IndividualCategory(List<IndCategory> indCategory) {
        super();
        this.indCategory = indCategory;
    }

    public List<IndCategory> getIndCategory() {
        return indCategory;
    }

    public void setIndCategory(List<IndCategory> indCategory) {
        this.indCategory = indCategory;
    }

}