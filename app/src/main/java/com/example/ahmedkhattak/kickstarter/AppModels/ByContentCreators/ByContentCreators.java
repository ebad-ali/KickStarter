package com.example.ahmedkhattak.kickstarter.AppModels.ByContentCreators;

/**
 * Created by Ahmed Khattak on 21/12/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ByContentCreators {

    @SerializedName("searchResult")
    @Expose
    private List<SearchResult> searchResult = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public ByContentCreators() {
    }

    /**
     *
     * @param searchResult
     */
    public ByContentCreators(List<SearchResult> searchResult) {
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