package com.example.ahmedkhattak.kickstarter.AppModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ebad Ali on 4/12/2017.
 */

public class TempBackedProject {

    @SerializedName("bank")
    @Expose
    private List<Bank> bank = null;
    @SerializedName("easypaise")
    @Expose
    private List<EasyPaisa> easyPaisa = null;


    public TempBackedProject() {
    }

    /**
     *
     * @param bank
     * @param easyPaisa
     */
    public TempBackedProject(List<Bank> bank, List<EasyPaisa> easyPaisa) {
        super();
        this.bank = bank;
        this.easyPaisa = easyPaisa;
    }

    public List<Bank> getBank() {
        return bank;
    }

    public List<EasyPaisa> getEasyPaisa() {
        return easyPaisa;
    }
}
