package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class EasyPaisa {

    @SerializedName("easypaise_id")
    @Expose
    private String easyPaisaID;
    @SerializedName("user_id")
    @Expose
    private String userID;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_cnic")
    @Expose
    private String userCNIC;

    public EasyPaisa(){

    }


    /**
     * @param easyPaisaID
     * @param userID
     * @param userPhone
     * @param userCNIC
     */


    public EasyPaisa(String easyPaisaID, String userID, String userPhone, String userCNIC) {
        super();
        this.easyPaisaID = easyPaisaID;
        this.userID = userID;
        this.userPhone = userPhone;
        this.userCNIC = userCNIC;
    }

    public String getEasyPaisaID() {
        return easyPaisaID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserCNIC() {
        return userCNIC;
    }
}

