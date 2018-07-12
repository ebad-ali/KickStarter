package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class Bank {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("bank_acc")
    @Expose
    private String bankAccount;
    @SerializedName("bank_branch_no")
    @Expose
    private String bankBranchNo;


    public Bank() {
    }


    /**
     * @param userId
     * @param bankAccount
     * @param bankBranchNo
     */
    public Bank(String userId, String bankAccount, String bankBranchNo) {
        super();
        this.userId = userId;
        this.bankAccount = bankAccount;
        this.bankBranchNo = bankBranchNo;
    }

    public String getUserId() {
        return userId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankBranchNo() {
        return bankBranchNo;
    }
}
