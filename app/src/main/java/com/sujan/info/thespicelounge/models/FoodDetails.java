package com.sujan.info.thespicelounge.models;

import android.media.Image;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pradeep on 2/7/20.
 */

public class FoodDetails {
    String itemCategory,itemName, serveType, description;
    int price,image,prepareTimeinMins,ratingStar,itemId;


    // Still array of feedback is yet to add.


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public FoodDetails(String itemCategory, String itemName, String serveType, String description, int price, int image, int prepareTimeinMins, int ratingStar,int itemID) {
        this.itemCategory = itemCategory;
        this.itemName = itemName;
        this.serveType = serveType;
        this.description = description;
        this.price = price;
        this.image = image;
        this.prepareTimeinMins = prepareTimeinMins;
        this.ratingStar = ratingStar;
        this.itemId=itemID;
    }

    public String getFullItemName(){
        return itemName+" "+itemCategory+" ("+serveType+" )";
    }
    public FoodDetails() {
    }


    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setServeType(String serveType) {
        this.serveType = serveType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setPrepareTimeinMins(int prepareTimeinMins) {
        this.prepareTimeinMins = prepareTimeinMins;
    }

    public void setRatingStar(int ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getItemCategory() {
        return itemCategory;
    }


    public String getItemName() {
        return itemName;
    }


    public String getServeType() {
        return serveType;
    }


    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }


    public int getImage() {
        return image;
    }


    public int getPrepareTimeinMins() {
        return prepareTimeinMins;
    }


    public int getRatingStar() {
        return ratingStar;
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("foodCategory", getItemCategory());
            jsonObject.put("foodName", getItemName());
            jsonObject.put("serveType", getServeType());
            jsonObject.put("Description", getDescription());
            jsonObject.put("price", getPrice());
            jsonObject.put("image",null);
            jsonObject.put("prepareTime", getPrepareTimeinMins());
            jsonObject.put("ratingStar", getRatingStar());
            jsonObject.put("foodID", getItemId());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

}
