package com.sujan.info.thespicelounge.interfaceT;

import com.sujan.info.thespicelounge.models.FoodDetails;

import java.util.ArrayList;

/**
 * Created by pradeep on 3/8/20.
 */

public interface MenuItemsListener{
    void MenuItemsReceived(ArrayList<FoodDetails> foodDetails, boolean fileExists);
}