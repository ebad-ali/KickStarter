package com.example.ahmedkhattak.kickstarter.Interfaces;

import com.example.ahmedkhattak.kickstarter.Utils.ToolbarOperations;

/**
 * Created by Ahmed Khattak on 02/12/2017.
 */

public interface OnToolBarChanged {

    void onToolBarChanged(String  tag, Object data, ToolbarOperations operations);
}
