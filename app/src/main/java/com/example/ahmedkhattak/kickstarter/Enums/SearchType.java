package com.example.ahmedkhattak.kickstarter.Enums;

/**
 * Created by Ahmed Khattak on 21/12/2017.
 */

public enum SearchType {

    // IMPORTANT !!!!
    // The order of  enums here should match the order of items in searchTypesArray other wise app will CRASH !!!

    ByProject,
    ByContentCreators,
    ByFunders,
    ByName;


    @Override
    public String toString() {
        switch (this){

            case ByName:
                return  "By Name";


            case ByFunders:
                return  "By Funders";


            case ByContentCreators:
                return  "By Content Creators";


            case ByProject:
                return  "By Project";

            default:

                // this shouldn't happen
                return null;

        }

    }
}