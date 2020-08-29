package com.sujan.info.thespicelounge.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pradeep on 2/7/20.
 */

public class User {
    private String empID, username, firstname, lastName, phone, email, address, post;

    public User(String empID, String username, String firstname, String lastName, String phone, String email, String address, String post) {
        this.empID = empID;
        this.username = username;
        this.firstname = firstname;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.post = post;
    }

    public User() {
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

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
