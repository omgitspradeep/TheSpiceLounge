package com.sujan.info.thespicelounge.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pradeep on 6/7/20.
 */

public class CustomerFeedback {
    int ratingstar,foodId;
    String custName,feedback;

    public int getRatingstar() {
        return ratingstar;
    }

    public void setRatingstar(int ratingstar) {
        this.ratingstar = ratingstar;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public CustomerFeedback(int foodId, String custName, String feedback,int ratingstar) {
        this.foodId = foodId;
        this.custName = custName;
        this.feedback = feedback;
        this.ratingstar = ratingstar;

    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("cust_name", getCustName() == null ? "" : getCustName());
        jsonObject.put("feedback", getFeedback() == null ? "" : getFeedback());
        jsonObject.put("food_id",  getFoodId());
        jsonObject.put("rating_star",getRatingstar());

        return jsonObject;

    }
}
