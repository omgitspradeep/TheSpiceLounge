package com.sujan.info.thespicelounge.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pradeep on 2/7/20.
 */

public class User {
    private String id, firstName, lastName, phone, email, street, title;
    private Date birthday;


    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
/*
        jsonObject.put("first_name", getFirstName() == null ? "" : getFirstName());
        jsonObject.put("last_name", getLastName() == null ? "" : getLastName().contains(" ") ? getLastName().replace(" ", "") : getLastName());
        jsonObject.put("phone", getPhone() == null ? "" : getPhone());
        jsonObject.put("id", getId() == null ? "" : getId());
        jsonObject.put("email", getEmail() == null ? "" : getEmail());
        jsonObject.put("title", getTitle() == null ? "" : getTitle());
        jsonObject.put("city", getCity() == null ? "" : getCity().toJSON());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        jsonObject.put("date", getBirthday() == null ? "" : sdf.format(getBirthday()));
        */

        return jsonObject;

    }

}
