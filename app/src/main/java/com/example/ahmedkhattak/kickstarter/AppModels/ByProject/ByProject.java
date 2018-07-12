package com.example.ahmedkhattak.kickstarter.AppModels.ByProject;

/**
 * Created by Ahmed Khattak on 21/12/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ByProject {

    @SerializedName("searchResult")
    @Expose
    private List<SearchResult> searchResult = null;

    /**
     * No args constructor for use in serialization
     */
    public ByProject() {
    }

    /**
     * @param searchResult
     */
    public ByProject(List<SearchResult> searchResult) {
        super();
        this.searchResult = searchResult;
    }

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        this.searchResult = searchResult;
    }

}